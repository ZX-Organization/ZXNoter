package team.zxorg.skin.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UISParser {

    public static void main(String[] args) {
        UISParser parser = new UISParser();
        try {
            parser.parseUIS(Path.of("D:\\malody\\skin\\ttb5测试\\script-key-4K.mui"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUIS(Path filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(filePath))) {
            String line;
            String currentElement = null;
            boolean isProperty;

            while ((line = reader.readLine()) != null) {
                //判断是否是属性
                isProperty = line.startsWith("\t") || line.startsWith("  ");

                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    // 跳过空行和注释
                    continue;
                }
                if (isProperty) {
                    //是属性 为当前元素解析
                    System.out.println("property: " + line);
                } else {
                    //是元素 更新元素
                    System.out.println("Element: " + line);
                }


            }
        }
    }
}

