package team.zxorg.audiosystem;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.ffmpeg.global.swresample;
import org.bytedeco.ffmpeg.swresample.SwrContext;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.file.Path;

public class AudioBuffer {
    private final FloatBuffer mainBuffer;
    private final FloatBuffer[] channelBuffers;

    private final int channels;
    private final int sampleRate;

    public AudioBuffer(FloatBuffer mainBuffer, int channels, int sampleRate) {
        this.mainBuffer = mainBuffer;
        this.channels = channels;
        this.sampleRate = sampleRate;
        channelBuffers = new FloatBuffer[channels];
        // 计算每个声道的样本数
        int channelSampleCount = mainBuffer.capacity() / channels;

        //初始化每个通道的缓冲区
        for (int i = 0; i < channels; i++) {
            channelBuffers[i] = ByteBuffer.allocateDirect(channelSampleCount).asFloatBuffer();
        }


    }

    public AudioBuffer(Path file, int targetChannels, int targetSampleRate) {
        // 打开输入文件
        AVFormatContext formatContext = avformat.avformat_alloc_context();

        if (avformat.avformat_open_input(formatContext, file.toString(), null, null) != 0) {
            throw new RuntimeException("无法打开音频: " + file);
        }
        // 查找流信息
        if (avformat.avformat_find_stream_info(formatContext, (PointerPointer) null) < 0) {
            throw new RuntimeException("没有找到可用的音频流: " + file);
        }

        // 获取音频流索引
        int audioIndex = -1;
        for (int i = 0; i < formatContext.nb_streams(); i++) {
            if (formatContext.streams(i).codecpar().codec_type() == avutil.AVMEDIA_TYPE_AUDIO) {
                audioIndex = i;
                break;
            }
        }
        if (audioIndex == -1) {
            throw new RuntimeException("没有找到音频流: " + file);
        }

        // 获取音频解码器
        AVCodec audioCodec = avcodec.avcodec_find_decoder(formatContext.streams(audioIndex).codecpar().codec_id());
        if (audioCodec == null) {
            throw new RuntimeException("未找到解码器");
        }

        AVCodecContext codecContext = avcodec.avcodec_alloc_context3(audioCodec);
        avcodec.avcodec_parameters_to_context(codecContext, formatContext.streams(audioIndex).codecpar());

        // 配置转换器参数
        SwrContext swrContext = swresample.swr_alloc_set_opts(null,
                targetChannels,   // 目标音频通道布局
                avutil.AV_SAMPLE_FMT_FLT,                  // 目标音频样本格式（float）
                targetSampleRate,                              // 目标音频采样率
                codecContext.ch_layout().nb_channels(), // 输入音频通道布局
                codecContext.sample_fmt(),             // 输入音频样本格式
                codecContext.sample_rate(),             // 输入音频采样率
                0, null);

        if (swrContext == null) {
            throw new RuntimeException("无法创建SwrContext");
        }
        // 初始化转换器
        if (swresample.swr_init(swrContext) < 0) {
            swresample.swr_free(swrContext);
            throw new RuntimeException("无法初始化SwrContext");
        }

        // 打开解码器
        if (avcodec.avcodec_open2(codecContext, audioCodec, (PointerPointer) null) < 0) {
            throw new RuntimeException("解码器打开失败");
        }

        // 初始化包和帧数据结构
        AVPacket avPacket = avcodec.av_packet_alloc();
        AVFrame decodedFrame = avutil.av_frame_alloc();


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //读取帧
        while (avformat.av_read_frame(formatContext, avPacket) >= 0) {
            if (audioIndex == avPacket.stream_index()) { // 过滤音频
                if (avcodec.avcodec_send_packet(codecContext, avPacket) == 0) {
                    while (avcodec.avcodec_receive_frame(codecContext, decodedFrame) == 0) {
                        // 计算转换后的音频数据总大小
                        int convertedDataSize = swresample.swr_get_out_samples(swrContext, decodedFrame.nb_samples());
                        // 分配缓冲区来存储转换后的音频数据
                        BytePointer convertedData = new BytePointer(avutil.av_malloc((long) convertedDataSize * avutil.av_get_bytes_per_sample(avutil.AV_SAMPLE_FMT_FLT) * targetChannels));
                        // 进行音频数据的转换
                        int outSamples = swresample.swr_convert(swrContext, convertedData, convertedDataSize, decodedFrame.data(0), decodedFrame.nb_samples());

                        if (outSamples > 0) {
                            int bufferSize = outSamples * targetChannels * avutil.av_get_bytes_per_sample(avutil.AV_SAMPLE_FMT_FLT);
                            byte[] buf = new byte[bufferSize];
                            convertedData.get(buf);
                            byteArrayOutputStream.writeBytes(buf);
                        }
                        // 释放转换后的音频数据的缓冲区
                        avutil.av_free(convertedData);
                    }
                }
            }
            avcodec.av_packet_unref(avPacket); // 减少引用计数
        }

        byte[] audioBytes = byteArrayOutputStream.toByteArray();
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(audioBytes.length).order(ByteOrder.nativeOrder());
        directBuffer.put(audioBytes).flip();


        this(directBuffer.asFloatBuffer(), targetChannels, targetSampleRate);

        // 清理资源
        avcodec.avcodec_free_context(codecContext);
        avutil.av_frame_free(decodedFrame);
        avcodec.av_packet_free(avPacket);
        swresample.swr_free(swrContext);
        avformat.avformat_close_input(formatContext);
    }

    // 创建声道 FloatBuffer 视图的方法
    private static FloatBuffer createChannelBufferView(ByteBuffer mainBuffer, int channel) {
        // 设置步长为 2，并创建一个新的 FloatBuffer 视图
        ByteBuffer byteBuffer = mainBuffer.duplicate().order(ByteOrder.nativeOrder()).position(channel * Float.BYTES).limit(mainBuffer.limit() * Float.BYTES).slice();
        FloatBuffer channelBuffer = byteBuffer.asFloatBuffer();
        channelBuffer.limit(mainBuffer.capacity() / 2);  // 限制缓冲区容量为主缓冲区的一半
        return channelBuffer;
    }

    public FloatBuffer getMainBuffer() {
        return mainBuffer;
    }

    public int getChannels() {
        return channels;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void rewind() {
        mainBuffer.rewind();
    }
}
