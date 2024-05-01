package team.zxorg.audiosystem.test;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.swresample.SwrContext;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;

import static org.bytedeco.ffmpeg.global.avcodec.*;
import static org.bytedeco.ffmpeg.global.avformat.*;
import static org.bytedeco.ffmpeg.global.avutil.*;
import static org.bytedeco.ffmpeg.global.swresample.*;
import static org.bytedeco.ffmpeg.presets.avutil.AVERROR_EAGAIN;

public class Main {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false));
        sourceDataLine.open();
        sourceDataLine.start();


        String filename = "D:\\Projects\\ZXNoter\\docs\\reference\\Corruption\\Corruption.mp3";

        AVFormatContext formatContext = new AVFormatContext(null);

        // 打开输入文件
        if (avformat_open_input(formatContext, filename, null, null) != 0) {
            System.err.println("Could not open file " + filename);
            return;
        }


        // 查找流信息
        int audio_index = avformat_find_stream_info(formatContext, (PointerPointer) null);

        if (audio_index < 0) {
            System.err.println("没有找到可用的音频流");
            return;
        }

        //输出音频格式信息
        av_dump_format(formatContext, 0, filename, 0);

        //得到音频解码器
        AVCodec audio_codec = avcodec_find_decoder(formatContext.streams(audio_index).codecpar().codec_id());
        if (audio_codec == null) {
            System.err.println("未找到解码器");
            return;
        }


        AVCodecContext codec_ctx = avcodec_alloc_context3(audio_codec);
        avcodec_parameters_to_context(codec_ctx, formatContext.streams(audio_index).codecpar());


        // 配置转换器参数
        SwrContext swrContext = swr_alloc_set_opts(null,
                av_get_default_channel_layout(sourceDataLine.getFormat().getChannels()),   // 目标音频通道布局
                AV_SAMPLE_FMT_S16,                  // 目标音频样本格式（16位有符号short）
                (int) sourceDataLine.getFormat().getSampleRate(),                              // 目标音频采样率
                av_get_default_channel_layout(codec_ctx.channels()), // 输入音频通道布局
                codec_ctx.sample_fmt(),             // 输入音频样本格式
                codec_ctx.sample_rate(),
                0, null);





        if (swrContext == null) {
            System.err.println("无法创建SwrContext");
            return;
        }


        // 初始化转换器
        if (swr_init(swrContext) < 0) {
            System.err.println("无法初始化SwrContext");
            swr_free(swrContext);
            return;
        }

        long aa = 0;
        // 打开解码器
        int ret = avcodec_open2(codec_ctx, audio_codec, (PointerPointer) null);
        if (ret < 0) {
            System.err.println("解码器打开失败");
            return;
        }

        // 初始化包和帧数据结构
        AVPacket avPacket = av_packet_alloc();
        av_init_packet(avPacket);

        AVFrame decodedFrame = av_frame_alloc();
        //读取帧
        while (true) {
            ret = av_read_frame(formatContext, avPacket);
            if (ret < 0) {
                System.out.println("音频读取完毕");
                break;
            } else if (audio_index == avPacket.stream_index()) { // 过滤音频
                ret = avcodec_send_packet(codec_ctx, avPacket);

                if (ret == AVERROR_EAGAIN()) {
                    System.out.println("解码EAGAIN：");
                } else if (ret < 0) {
                    byte error[] = new byte[1024];
                    av_strerror(ret, error, 1024);
                    System.out.println("解码失败：" + new String(error));
                    return;
                }
                //解码帧
                while (true) {
                    ret = avcodec_receive_frame(codec_ctx, decodedFrame);
                    if (ret == AVERROR_EAGAIN() || ret == AVERROR_EOF) {
                        break;
                    } else if (ret < 0) {
                        System.err.println("音频解码失败");
                        return;
                    }
                    aa += decodedFrame.nb_samples();
                    // 计算转换后的音频数据总大小
                    int convertedDataSize = swr_get_out_samples(swrContext, decodedFrame.nb_samples());
                    // 分配缓冲区来存储转换后的音频数据
                    int size = convertedDataSize * av_get_bytes_per_sample(AV_SAMPLE_FMT_S16) * codec_ctx.channels();
                    BytePointer convertedData = new BytePointer(av_malloc(size));
                    // 进行音频数据的转换
                    int convertedSamples = swr_convert(swrContext, convertedData, convertedDataSize, decodedFrame.data(0), decodedFrame.nb_samples());
                    //System.out.println(convertedSamples);
                    // 将转换后的音频数据写入 SourceDataLine 以播放
                    byte[] buf = new byte[size];
                    convertedData.get(buf);
                    //sourceDataLine.write(buf, 0, size);
                    // 释放转换后的音频数据的缓冲区
                    av_free(convertedData);
                }
            } else {
                av_packet_unref(avPacket); // 减少引用计数
            }
        }
        long t1 = (aa * 1000) / formatContext.streams(0).codecpar().sample_rate();
        long t2 = formatContext.duration() / 1000;
        System.out.println("实际：" + FFmpegInfoTest.formatTime(t1));
        System.out.println("标签：" + FFmpegInfoTest.formatTime(t2));
        System.out.println("标签补偿：" + FFmpegInfoTest.formatTime(t2 + formatContext.start_time() / 1000));
        System.out.println("误差：" + FFmpegInfoTest.formatTime(Math.abs(t1 - t2)));
        System.out.println("均值：" + FFmpegInfoTest.formatTime((t1 + t2) / 2));
        System.out.println("------");
        if (formatContext.start_time() == 0) {
            t1 = (t1 + t2) / 2;
        } else {
            t1 = t2 + formatContext.start_time() / 1000;
        }
        System.out.println("可接受：" + FFmpegInfoTest.formatTime(t1));
        av_frame_free(decodedFrame);
        av_packet_free(avPacket);
        avcodec_free_context(codec_ctx);
        avformat_close_input(formatContext);
    }
}
