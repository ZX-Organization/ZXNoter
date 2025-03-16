package team.zxorg.zxnoter.sound.audiomixer;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVChannelLayout;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.ffmpeg.global.swresample;
import org.bytedeco.ffmpeg.swresample.SwrContext;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;
import team.zxorg.zxnoter.Main;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;

public class FFmpeg {


    public static float[] read(Path file, int sampleRate) {
        AVFormatContext formatContext = null;
        AVCodecContext codecContext = null;
        SwrContext swrContext = null;
        AVPacket avPacket = null;
        AVFrame decodedFrame = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            formatContext = avformat.avformat_alloc_context();
            if (avformat.avformat_open_input(formatContext, file.toString(), null, null) != 0) {
                throw new RuntimeException("无法打开音频: " + file);
            }

            if (avformat.avformat_find_stream_info(formatContext, (PointerPointer) null) < 0) {
                throw new RuntimeException("没有找到可用的音频流: " + file);
            }

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

            AVCodec audioCodec = avcodec.avcodec_find_decoder(formatContext.streams(audioIndex).codecpar().codec_id());
            if (audioCodec == null) {
                throw new RuntimeException("未找到解码器");
            }

            codecContext = avcodec.avcodec_alloc_context3(audioCodec);
            avcodec.avcodec_parameters_to_context(codecContext, formatContext.streams(audioIndex).codecpar());

            AVChannelLayout outChLayout = new AVChannelLayout();
            avutil.av_channel_layout_default(outChLayout, 2);
            swrContext = new SwrContext();
            swresample.swr_alloc_set_opts2(swrContext,
                    outChLayout,
                    avutil.AV_SAMPLE_FMT_S16,
                    sampleRate,
                    codecContext.ch_layout(),
                    codecContext.sample_fmt(),
                    codecContext.sample_rate(),
                    0,
                    null);

            if (swresample.swr_init(swrContext) < 0) {
                throw new RuntimeException("无法初始化SwrContext");
            }

            if (avcodec.avcodec_open2(codecContext, audioCodec, (PointerPointer) null) < 0) {
                throw new RuntimeException("解码器打开失败");
            }

            avPacket = avcodec.av_packet_alloc();
            decodedFrame = avutil.av_frame_alloc();
            byteArrayOutputStream = new ByteArrayOutputStream();

            while (avformat.av_read_frame(formatContext, avPacket) >= 0) {
                if (audioIndex == avPacket.stream_index()) {
                    if (avcodec.avcodec_send_packet(codecContext, avPacket) == 0) {
                        while (avcodec.avcodec_receive_frame(codecContext, decodedFrame) == 0) {
                            int convertedDataSize = swresample.swr_get_out_samples(swrContext, decodedFrame.nb_samples());
                            BytePointer convertedData = new BytePointer(avutil.av_malloc((long) convertedDataSize * avutil.av_get_bytes_per_sample(avutil.AV_SAMPLE_FMT_FLT) * 2));
                            int outSamples = swresample.swr_convert(swrContext, convertedData, convertedDataSize, decodedFrame.data(0), decodedFrame.nb_samples());

                            if (outSamples > 0) {
                                int bufferSize = outSamples * 2 * avutil.av_get_bytes_per_sample(avutil.AV_SAMPLE_FMT_FLT);
                                byte[] buf = new byte[bufferSize];
                                convertedData.get(buf);
                                byteArrayOutputStream.write(buf);
                            }

                            avutil.av_free(convertedData);
                        }
                    }
                }
                avcodec.av_packet_unref(avPacket);
            }
            ByteBuffer data = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

            //byte[] audioBytes = byteArrayOutputStream.toByteArray();
//            return new AudioInputStream(new ByteArrayInputStream(audioBytes), new AudioFormat(sampleRate, 16, 2, true, false), audioBytes.length / 4);
            return data.asFloatBuffer().array();
        } catch (Exception e) {
            throw new RuntimeException("音频处理过程中出现错误: " + e.getMessage(), e);
        } finally {
            if (codecContext != null) {
                avcodec.avcodec_free_context(codecContext);
            }
            if (decodedFrame != null) {
                avutil.av_frame_free(decodedFrame);
            }
            if (avPacket != null) {
                avcodec.av_packet_free(avPacket);
            }
            if (swrContext != null) {
                swresample.swr_free(swrContext);
            }
            if (formatContext != null) {
                avformat.avformat_close_input(formatContext);
            }
        }
    }

    public static boolean audioToWav(Path audio, Path wav) {
        String[] command = {"ffmpeg", "-y", "-i", "\"" + audio.toAbsolutePath() + "\"", "\"" + wav.toAbsolutePath() + "\""};

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.environment().putAll(System.getenv());
            processBuilder.redirectErrorStream(true); // 将错误流合并到标准流中

            Process process = processBuilder.start();

            // 读取进程输出，防止阻塞
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // 等待进程执行完成
            int exitCode = process.waitFor();
            return (exitCode == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkFFmpegExistence() {
        String[] command = {"ffmpeg", "-version"};

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // 获取系统环境变量
            processBuilder.environment().putAll(System.getenv());

            // 重定向标准输出和错误输出
            processBuilder.redirectErrorStream(true);

            // 启动进程
            Process process = processBuilder.start();

            // 读取进程输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待进程执行完成
            int exitCode = process.waitFor();

            // 检查输出中是否包含 FFmpeg 版本信息
            boolean ffmpegExists = output.toString().toLowerCase().contains("ffmpeg version");

            return (exitCode == 0 && ffmpegExists);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HashMap<String, String> audioToMetadata(Path audio) {
        HashMap<String, String> metadata = new HashMap<>();
        String[] command = {"ffmpeg", "-i", Main.quotationMark + audio.toAbsolutePath() + Main.quotationMark, "-f", "ffmetadata"};
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            // 获取系统环境变量
            processBuilder.environment().putAll(System.getenv());
            // 启动进程
            Process process = processBuilder.start();
            // 获取进程的输出流
            InputStream inputStream = process.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // 读取命令输出的每一行数据
            String line;
            while ((line = reader.readLine()) != null) {
                // 在每行中查找包含元数据的部分
                if (line.startsWith("  ")) {
                    String[] parts = line.trim().split(": ");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        metadata.put(key, value);
                    }
                }
            }
            // 等待进程执行完成
            int exitCode = process.waitFor();
            return metadata;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return metadata;
    }

    public enum AudioMetadataKey {
        ARTIST("artist"),
        GENRE("genre"),
        TITLE("title"),
        TEMPO("TBPM"),
        ENCODER("encoder");

        private final String key;

        AudioMetadataKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

}
