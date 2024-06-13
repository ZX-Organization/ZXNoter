
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;

import static org.bytedeco.ffmpeg.global.avformat.*;

public class FFmpegInfoTest {
    public static void main(String[] args) {

        String filename = "D:\\Projects\\ZXNoter\\docs\\reference\\mp3Time\\audio_au_ff.mp3";

        AVFormatContext formatContext = new AVFormatContext(null);

        // 打开输入文件
        if (avformat.avformat_open_input(formatContext, filename, null, null) != 0) {
            System.err.println("Could not open file " + filename);
            return;
        }

        // 获取流信息
        if (avformat.avformat_find_stream_info(formatContext, (AVDictionary) null) < 0) {
            System.err.println("Could not find stream information");
            return;
        }


        // 寻找音频流
        int audioStreamIndex = -1;
        for (int i = 0; i < formatContext.nb_streams(); i++) {
            if (formatContext.streams(i).codecpar().codec_type() == avutil.AVMEDIA_TYPE_AUDIO) {
                audioStreamIndex = i;
                break;
            }
        }

        if (audioStreamIndex == -1) {
            System.err.println("Could not find audio stream");
            return;
        }


        // 获取音频时长
        System.out.println(formatTime(formatContext.duration() / 1000));
        System.out.println(formatTime(formatContext.start_time() / 1000));
        System.out.println(formatTime((formatContext.start_time() + formatContext.duration()) / 1000) + " ms");
        System.out.println(formatContext.max_analyze_duration());
        avformat.avformat_close_input(formatContext);
    }

    /**
     * 将时间转为格式化时间字符串
     *
     * @param timeInMillis 时间ms
     * @return 格式化时间
     */
    public static String formatTime(long timeInMillis) {
        long hours = timeInMillis / (60 * 60 * 1000);
        long minutes = (timeInMillis % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (timeInMillis % (60 * 1000)) / 1000;
        long milliseconds = timeInMillis % 1000;

        StringBuilder formattedTime = new StringBuilder();
        if (hours > 0) {
            formattedTime.append(hours).append(":");
        }
        if (minutes > 0 || hours > 0) {
            formattedTime.append(String.format("%02d", minutes)).append(":");
        }
        formattedTime.append(String.format("%02d", seconds)).append(".");
        formattedTime.append(String.format("%03d", milliseconds));
        return formattedTime.toString();
    }

}
