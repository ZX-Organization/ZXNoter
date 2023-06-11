package team.zxorg.zxnoter.resource;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class Utils {
    public static String readSvg(Path path) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String clip = Files.readString(path);
        String temp;

        if (clip == null) {
            throw new RuntimeException("内容为空");
        }
        if (!clip.contains("<svg")) {
            throw new RuntimeException("文件类型有误！");
        }

        temp = clip.substring(clip.indexOf("viewBox=") + "viewBox=".length() + 1);
        temp = temp.substring(0, temp.indexOf("\""));
        String[] size = temp.split(" ");
        if (size.length != 4) {
            throw new RuntimeException("没有指定大小！");
        }

        int t = 0;
        while (true) {
            t = clip.indexOf("<path", t + 1);
            if (t == -1)
                break;

            temp = clip.substring(t, clip.indexOf(">", t));

            if (temp.replaceAll(" ", "").contains("fill=\"none\""))
                continue;
            if (temp.replaceAll(" ", "").contains("M" + size[0] + "," + size[1] + "h" + size[2] + "v" + size[3] + "H" + size[0] + "V" + size[1] + "z"))
                continue;

            temp = temp.substring(temp.indexOf("d=\"") + "d=\"".length());
            temp = temp.substring(0, temp.indexOf("\""));
            //System.out.println(temp);
            stringBuilder.append(temp);
            stringBuilder.append(" ");
        }
        if (stringBuilder.length() == 1) {
            throw new RuntimeException("不OK！");
        }

        stringBuilder.append("M").append(size[0]).append(",").append(size[1]).append("z ");
        stringBuilder.append("M").append(size[2]).append(",").append(size[3]).append("z");

        return stringBuilder.toString();
    }

}
