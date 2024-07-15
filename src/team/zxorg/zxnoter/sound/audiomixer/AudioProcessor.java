package team.zxorg.zxnoter.sound.audiomixer;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.ffmpeg.global.swresample;
import org.bytedeco.javacpp.*;
import org.bytedeco.ffmpeg.avcodec.*;
import org.bytedeco.ffmpeg.avformat.*;
import org.bytedeco.ffmpeg.avutil.*;
import org.bytedeco.ffmpeg.swresample.*;
import org.bytedeco.javacpp.annotation.Cast;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
public class AudioProcessor {
    private static final int AVIO_CONTEXT_BUFFER_SIZE = 4096;
    private static final int TARGET_SAMPLE_RATE = 192000;
    private static final int TARGET_SAMPLE_FORMAT = avutil.AV_SAMPLE_FMT_S32;
    private static final int TARGET_CHANNEL_LAYOUT = 2; // Stereo

    public static AudioInputStream read(byte[] inputData) {
        if (inputData == null || inputData.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty");
        }

        AVFormatContext formatContext = null;
        AVIOContext avioContext = null;
        AVCodecContext codecContext = null;
        SwrContext swrContext = null;
        AVPacket avPacket = null;
        AVFrame decodedFrame = null;

        try {
            BytePointer avioContextBuffer = new BytePointer(avutil.av_malloc(AVIO_CONTEXT_BUFFER_SIZE)).capacity(AVIO_CONTEXT_BUFFER_SIZE);
            ByteBuffer data = ByteBuffer.wrap(inputData).order(ByteOrder.nativeOrder());

            formatContext = createFormatContext(data, avioContextBuffer);
            int audioIndex = findAudioStream(formatContext);
            codecContext = createCodecContext(formatContext, audioIndex);
            swrContext = createSwrContext(codecContext);
            avPacket = avcodec.av_packet_alloc();
            decodedFrame = avutil.av_frame_alloc();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            processAudioFrames(formatContext, codecContext, swrContext, avPacket, decodedFrame, audioIndex, byteArrayOutputStream);

            byte[] audioBytes = byteArrayOutputStream.toByteArray();
            return createAudioInputStream(audioBytes);
        } finally {
            cleanup(formatContext, avioContext, codecContext, swrContext, avPacket, decodedFrame);
        }
    }
    static class BufferData {
        ByteBuffer data;

        BufferData(byte[] inputData) {
            this.data = ByteBuffer.wrap(inputData).order(ByteOrder.nativeOrder());
        }
        BufferData(ByteBuffer data) {
            this.data =data;
        }

        int read(byte[] buf, int bufSize) {
            int remaining = data.remaining();
            bufSize = Math.min(bufSize, remaining);
            data.get(buf, 0, bufSize);
            return bufSize;
        }
    }

    private static AVFormatContext createFormatContext(ByteBuffer data, BytePointer avioContextBuffer) {
        AVFormatContext formatContext = avformat.avformat_alloc_context();
        if (formatContext == null) {
            throw new RuntimeException("Could not allocate format context");
        }

        AVIOContext.Read_packet_Pointer_BytePointer_int readPacketCallback = new AVIOContext.Read_packet_Pointer_BytePointer_int() {
            @Override
            public int call(Pointer opaque, BytePointer buf, int buf_size) {
                ByteBuffer dataBuffer = ((ByteBuffer) opaque.asByteBuffer()).order(ByteOrder.nativeOrder());
                if (!dataBuffer.hasRemaining()) {
                    return avutil.AVERROR_EOF;
                }
                int bytesToRead = Math.min(buf_size, dataBuffer.remaining());
                buf.put(dataBuffer.array(), dataBuffer.position(), bytesToRead);
                dataBuffer.position(dataBuffer.position() + bytesToRead);
                return bytesToRead;
            }
        };
        AVIOContext avioContext = avformat.avio_alloc_context(
                new byte[AVIO_CONTEXT_BUFFER_SIZE],
                AVIO_CONTEXT_BUFFER_SIZE,
                0,
                new Pointer(data),
                new Read_packet_Pointer_byte___int() {
                    @Override
                    public int call(Pointer opaque, @Cast("unsigned char*") byte[] buf, int bufSize) {
                        int remaining = data.remaining();
                        bufSize = Math.min(bufSize, remaining);
                        data.get(buf, 0, bufSize);
                        return bufSize;
                    }
                },
                null,
                null
        );

        if (avioContext == null) {
            avformat.avformat_free_context(formatContext);
            throw new RuntimeException("Could not allocate AVIO context");
        }

        formatContext.pb(avioContext);

        if (avformat.avformat_open_input(formatContext, (String) null, null, null) != 0) {
            avformat.avformat_free_context(formatContext);
            throw new RuntimeException("Cannot open audio input");
        }

        if (avformat.avformat_find_stream_info(formatContext, (PointerPointer) null) < 0) {
            avformat.avformat_close_input(formatContext);
            throw new RuntimeException("Cannot find stream information");
        }

        return formatContext;
    }

    private static int findAudioStream(AVFormatContext formatContext) {
        for (int i = 0; i < formatContext.nb_streams(); i++) {
            if (formatContext.streams(i).codecpar().codec_type() == avutil.AVMEDIA_TYPE_AUDIO) {
                return i;
            }
        }
        throw new RuntimeException("No audio stream found");
    }

    private static AVCodecContext createCodecContext(AVFormatContext formatContext, int audioIndex) {
        AVCodec audioCodec = avcodec.avcodec_find_decoder(formatContext.streams(audioIndex).codecpar().codec_id());
        if (audioCodec == null) {
            throw new RuntimeException("Decoder not found");
        }

        AVCodecContext codecContext = avcodec.avcodec_alloc_context3(audioCodec);
        avcodec.avcodec_parameters_to_context(codecContext, formatContext.streams(audioIndex).codecpar());

        if (avcodec.avcodec_open2(codecContext, audioCodec, (PointerPointer) null) < 0) {
            throw new RuntimeException("Could not open codec");
        }

        return codecContext;
    }

    private static SwrContext createSwrContext(AVCodecContext codecContext) {
        SwrContext swrContext = swresample.swr_alloc_set_opts(null,
                TARGET_CHANNEL_LAYOUT,
                TARGET_SAMPLE_FORMAT,
                TARGET_SAMPLE_RATE,
                codecContext.ch_layout().nb_channels(),
                codecContext.sample_fmt(),
                codecContext.sample_rate(),
                0, null);

        if (swrContext == null) {
            throw new RuntimeException("Could not allocate SwrContext");
        }

        if (swresample.swr_init(swrContext) < 0) {
            throw new RuntimeException("Could not initialize SwrContext");
        }

        return swrContext;
    }

    private static void processAudioFrames(AVFormatContext formatContext, AVCodecContext codecContext,
                                           SwrContext swrContext, AVPacket avPacket, AVFrame decodedFrame,
                                           int audioIndex, ByteArrayOutputStream byteArrayOutputStream) {
        while (avformat.av_read_frame(formatContext, avPacket) >= 0) {
            if (audioIndex == avPacket.stream_index()) {
                if (avcodec.avcodec_send_packet(codecContext, avPacket) == 0) {
                    while (avcodec.avcodec_receive_frame(codecContext, decodedFrame) == 0) {
                        convertAndWriteFrame(swrContext, decodedFrame, byteArrayOutputStream);
                    }
                }
            }
            avcodec.av_packet_unref(avPacket);
        }
    }

    private static void convertAndWriteFrame(SwrContext swrContext, AVFrame decodedFrame,
                                             ByteArrayOutputStream byteArrayOutputStream) {
        int convertedDataSize = swresample.swr_get_out_samples(swrContext, decodedFrame.nb_samples());
        BytePointer convertedData = new BytePointer(avutil.av_malloc((long) convertedDataSize * avutil.av_get_bytes_per_sample(TARGET_SAMPLE_FORMAT) * TARGET_CHANNEL_LAYOUT));

        try {
            int outSamples = swresample.swr_convert(swrContext, convertedData, convertedDataSize, decodedFrame.data(0), decodedFrame.nb_samples());

            if (outSamples > 0) {
                int bufferSize = outSamples * TARGET_CHANNEL_LAYOUT * avutil.av_get_bytes_per_sample(TARGET_SAMPLE_FORMAT);
                byte[] buf = new byte[bufferSize];
                convertedData.get(buf);
                byteArrayOutputStream.writeBytes(buf);
            }
        } finally {
            avutil.av_free(convertedData);
        }
    }

    private static AudioInputStream createAudioInputStream(byte[] audioBytes) {
        return new AudioInputStream(
                new ByteArrayInputStream(audioBytes),
                new AudioFormat(TARGET_SAMPLE_RATE, 16, TARGET_CHANNEL_LAYOUT, true, false),
                audioBytes.length / 4
        );
    }

    private static void cleanup(AVFormatContext formatContext, AVIOContext avioContext,
                                AVCodecContext codecContext, SwrContext swrContext,
                                AVPacket avPacket, AVFrame decodedFrame) {
        if (codecContext != null) avcodec.avcodec_free_context(codecContext);
        if (decodedFrame != null) avutil.av_frame_free(decodedFrame);
        if (avPacket != null) avcodec.av_packet_free(avPacket);
        if (swrContext != null) swresample.swr_free(swrContext);
        if (formatContext != null) {
            avformat.avformat_close_input(formatContext);
            if (avioContext != null) {
                avutil.av_free(avioContext.buffer());
                avformat.avio_context_free(avioContext);
            }
            avformat.avformat_free_context(formatContext);
        }
    }
}
