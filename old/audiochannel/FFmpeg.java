package team.zxorg.zxnoter.audiochannel;

import java.io.IOException;
import java.nio.file.Path;

public class FFmpeg {
    public static boolean audioToWav(Path audio, Path wav) {
        String command = "ffmpeg -y -i \"" + audio.toAbsolutePath() + "\" \"" + wav.toAbsolutePath() + "\"";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

            // 获取系统环境变量
            processBuilder.environment().putAll(System.getenv());

            // 启动进程
            Process process = processBuilder.start();

            // 等待进程执行完成
            int exitCode = process.waitFor();

            return (exitCode == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
