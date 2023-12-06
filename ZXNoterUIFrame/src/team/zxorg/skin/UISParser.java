package team.zxorg.skin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UISParser {

    public static void main(String[] args) {
        UISParser parser = new UISParser();
        try {
            parser.parseUIS("D:\\malody\\skin\\ttb5测试\\script-key-4K.mui");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUIS(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentElement = null;
            boolean isProperty;

            while ((line = reader.readLine()) != null) {
                isProperty = line.startsWith("\t") || line.startsWith("    ");

                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    // 跳过空行和注释
                    continue;
                }
                if (isProperty) {
                    //是属性 为当前元素解析
                    System.out.println("property: " + line);
                } else {
                    System.out.println("Element: " + line);

                }


            }
        }
    }
}

