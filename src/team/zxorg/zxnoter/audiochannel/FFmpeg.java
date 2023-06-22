package team.zxorg.zxnoter.audiochannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;

public class FFmpeg {
    // 创建命令行执行器
    static ProcessBuilder processBuilder = new ProcessBuilder();
    static ArrayList<String> commands = new ArrayList<>();
    // 获取环境变量
    static String path = System.getenv("PATH");

    static {
        // 设置命令行执行器的环境变量
        processBuilder.environment().put("PATH", path);
        processBuilder.command(commands);
    }

    /**
     * 音频格式转换
     *
     * @param audioPath 音频路径
     * @return 转换后的音频路径
     */
    public static int audioFormatConversion(Path audioPath, Path outputPath) throws IOException, InterruptedException {
        return exec("-i", audioPath.toString(), outputPath.toString());
    }

    public static int exec(String... command) throws IOException, InterruptedException {
        commands.add("ffmpeg");
        // 创建命令行执行器
        for (int i = 0; i < command.length; i++) {
            commands.add(command[i]);
        }

        // 启动命令行执行器
        Process process = processBuilder.start();
        // 读取命令行输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        // 等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("FFmpeg处理结果 Exit Code: " + exitCode);
        return exitCode;


    }
}
