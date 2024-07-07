package team.zxorg.zxnoter_old.sound.audiomixer;

import team.zxorg.zxnoter_old.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;

public class FFmpeg {
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
