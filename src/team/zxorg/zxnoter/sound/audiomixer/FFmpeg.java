package team.zxorg.zxnoter.sound.audiomixer;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVCodecParameters;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVChannelLayout;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.avutil.AVDictionaryEntry;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.ffmpeg.global.swresample;
import org.bytedeco.ffmpeg.swresample.SwrContext;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.HashMap;


public class FFmpeg {
    public static boolean DEBUG_WRITE_PCM = false;

    /**
     * 读取音频文件并解码为浮点数组 (32-bit Float, Interleaved Stereo)
     */
    public static float[] read(Path file, int sampleRate) {
        AVFormatContext formatContext = null;
        AVCodecContext codecContext = null;
        SwrContext swrContext = null;
        AVPacket avPacket = null;
        AVFrame decodedFrame = null;
        ByteArrayOutputStream rawOutStream = new ByteArrayOutputStream();

        // 调试文件流
        FileOutputStream debugFileStream = null;

        try {
            if (DEBUG_WRITE_PCM) {
                debugFileStream = new FileOutputStream("debug_audio_dump.pcm", true);
                System.out.println("[FFmpeg] 调试模式开启，数据写入 debug_audio_dump.pcm");
            }

            // 1. 打开输入文件
            formatContext = avformat.avformat_alloc_context();
            if (avformat.avformat_open_input(formatContext, file.toAbsolutePath().toString(), null, null) != 0) {
                throw new RuntimeException("FFmpeg: 无法打开文件: " + file);
            }

            if (avformat.avformat_find_stream_info(formatContext, (PointerPointer) null) < 0) {
                throw new RuntimeException("FFmpeg: 无法获取流信息");
            }

            // 2. 查找音频流
            int audioIndex = -1;
            for (int i = 0; i < formatContext.nb_streams(); i++) {
                if (formatContext.streams(i).codecpar().codec_type() == avutil.AVMEDIA_TYPE_AUDIO) {
                    audioIndex = i;
                    break;
                }
            }
            if (audioIndex == -1) throw new RuntimeException("FFmpeg: 未找到音频流");

            // 3. 初始化解码器
            AVCodecParameters codecParams = formatContext.streams(audioIndex).codecpar();
            AVCodec audioCodec = avcodec.avcodec_find_decoder(codecParams.codec_id());
            if (audioCodec == null) throw new RuntimeException("FFmpeg: 未找到解码器");

            codecContext = avcodec.avcodec_alloc_context3(audioCodec);
            avcodec.avcodec_parameters_to_context(codecContext, codecParams);

            if (avcodec.avcodec_open2(codecContext, audioCodec, (PointerPointer) null) < 0) {
                throw new RuntimeException("FFmpeg: 无法打开解码器");
            }

            // 4. 初始化重采样 (SwrContext) -> 目标: 浮点数立体声
            AVChannelLayout outChLayout = new AVChannelLayout();
            avutil.av_channel_layout_default(outChLayout, 2); // 强制立体声

            swrContext = new SwrContext();
            swresample.swr_alloc_set_opts2(swrContext, outChLayout, avutil.AV_SAMPLE_FMT_FLT,   // 输出: Float (32-bit)
                    sampleRate,                 // 输出采样率
                    codecContext.ch_layout(),   // 输入布局
                    codecContext.sample_fmt(),  // 输入格式
                    codecContext.sample_rate(), // 输入采样率
                    0, null);

            if (swresample.swr_init(swrContext) < 0) {
                throw new RuntimeException("FFmpeg: 无法初始化重采样上下文");
            }

            avPacket = avcodec.av_packet_alloc();
            decodedFrame = avutil.av_frame_alloc();

            // 预分配指针数组用于存放输出 buffer 地址
            PointerPointer outData = new PointerPointer(1);

            // 5. 解码循环
            while (avformat.av_read_frame(formatContext, avPacket) >= 0) {
                if (avPacket.stream_index() == audioIndex) {
                    if (avcodec.avcodec_send_packet(codecContext, avPacket) == 0) {
                        while (avcodec.avcodec_receive_frame(codecContext, decodedFrame) == 0) {
                            // 计算输出样本数
                            long delay = swresample.swr_get_delay(swrContext, codecContext.sample_rate());
                            int dstNbSamples = (int) avutil.av_rescale_rnd(delay + decodedFrame.nb_samples(), sampleRate, codecContext.sample_rate(), avutil.AV_ROUND_UP);

                            // 分配输出内存 (Samples * Channels * BytesPerSample)
                            BytePointer outBuffer = new BytePointer(avutil.av_malloc((long) dstNbSamples * 2 * 4));

                            // === 关键修复: 将分配的内存放入指针数组 ===
                            outData.put(0, outBuffer);

                            // === 执行转换: 传入 decodedFrame.data() 以支持平面(Planar)格式 ===
                            int convertedSamples = swresample.swr_convert(swrContext, outData, dstNbSamples, decodedFrame.data(), // 修复点：传递整个数据指针数组，而非 data(0)
                                    decodedFrame.nb_samples());

                            if (convertedSamples > 0) {
                                byte[] data = new byte[convertedSamples * 2 * 4];
                                outBuffer.get(data);
                                rawOutStream.write(data);

                                if (debugFileStream != null) {
                                    debugFileStream.write(data);
                                }
                            }

                            avutil.av_free(outBuffer); // 释放原生内存
                        }
                    }
                }
                avcodec.av_packet_unref(avPacket);
            }

            outData.close();
            System.out.println("[FFmpeg] 解码完成，总字节数: " + rawOutStream.size());

            // 6. 转换为 float 数组
            byte[] allBytes = rawOutStream.toByteArray();
            float[] finalFloats = new float[allBytes.length / 4];
            ByteBuffer.wrap(allBytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(finalFloats);
            return finalFloats;

        } catch (Exception e) {
            throw new RuntimeException("FFmpeg 解码错误", e);
        } finally {
            if (debugFileStream != null) {
                try {
                    debugFileStream.close();
                } catch (IOException ignored) {
                }
            }
            if (avPacket != null) avcodec.av_packet_free(avPacket);
            if (decodedFrame != null) avutil.av_frame_free(decodedFrame);
            if (codecContext != null) avcodec.avcodec_free_context(codecContext);
            if (swrContext != null) swresample.swr_free(swrContext);
            if (formatContext != null) {
                avformat.avformat_close_input(formatContext);
                avformat.avformat_free_context(formatContext);
            }
        }
    }

    /**
     * 读取音频元数据
     */
    public static HashMap<String, String> readMetadata(Path file) {
        HashMap<String, String> metadataMap = new HashMap<>();
        AVFormatContext formatContext = null;

        try {
            formatContext = avformat.avformat_alloc_context();
            if (avformat.avformat_open_input(formatContext, file.toAbsolutePath().toString(), null, null) != 0) {
                return metadataMap;
            }

            AVDictionary dict = formatContext.metadata();
            AVDictionaryEntry tag = null;

            while ((tag = avutil.av_dict_get(dict, "", tag, avutil.AV_DICT_IGNORE_SUFFIX)) != null) {
                String key = tag.key().getString();
                String value = tag.value().getString();
                metadataMap.put(key, value);
                metadataMap.put(key.toLowerCase(), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (formatContext != null) {
                avformat.avformat_close_input(formatContext);
                avformat.avformat_free_context(formatContext);
            }
        }
        return metadataMap;
    }


    /**
     * 元数据 Key 枚举
     */
    public enum AudioMetadataKey {
        ARTIST("artist"), GENRE("genre"), TITLE("title"), ALBUM("album"), TEMPO("TBPM"), ENCODER("encoder");

        private final String key;

        AudioMetadataKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}