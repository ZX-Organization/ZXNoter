package team.zxorg.zxnoter.resource;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class Utils {
    public static String readSvg(Path path) throws IOException {
        //System.out.println(path);

        StringBuilder stringBuilder = new StringBuilder();
        String clip = Files.readString(path);

        String temp;

        //String clip="<svg t=\"1657259513758\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"6050\" width=\"32\" height=\"32\"><path d=\"M703.782 179.178c31.35 18.111 60.275 40.774 85.897 67.587C858.241 318.519 896 412.714 896 512c0 51.868-10.144 102.15-30.15 149.451-19.337 45.719-47.034 86.792-82.321 122.078-35.286 35.287-76.359 62.983-122.078 82.321C614.15 885.856 563.868 896 512 896c-99.286 0-193.481-37.759-265.234-106.321-26.813-25.621-49.476-54.547-67.587-85.897C228.224 724.74 281.523 736 336 736c220.561 0 400-179.439 400-400 0-54.477-11.26-107.776-32.218-156.822M533.997 64.536C617.645 125.651 672 224.47 672 336c0 185.568-150.432 336-336 336-111.53 0-210.349-54.354-271.464-138.003C76.005 771.202 271.952 960 512 960c247.424 0 448-200.576 448-448 0-240.048-188.798-435.995-426.003-447.464zM288 224c17.6 0 32-14.4 32-32s-14.4-32-32-32h-64V96c0-17.6-14.4-32-32-32s-32 14.4-32 32v64H96c-17.6 0-32 14.4-32 32s14.4 32 32 32h64v64c0 17.6 14.4 32 32 32s32-14.4 32-32v-64h64z\" p-id=\"6051\"></path><path d=\"M416 416h-32v-32c0-17.6-14.4-32-32-32s-32 14.4-32 32v32h-32c-17.6 0-32 14.4-32 32s14.4 32 32 32h32v32c0 17.6 14.4 32 32 32s32-14.4 32-32v-32h32c17.6 0 32-14.4 32-32s-14.4-32-32-32z\" p-id=\"6052\"></path></svg>";

        if (clip == null) {
            throw new RuntimeException("剪切板为空");
        }
        if (clip.indexOf("<svg") == -1) {
            throw new RuntimeException("文件类型有误！");
        }

        temp = clip.substring(clip.indexOf("viewBox=") + "viewBox=".length() + 1);
        temp = temp.substring(0, temp.indexOf("\""));
        String[] size = temp.split(" ");
        if (size.length != 4) {
            throw new RuntimeException("没有指定大小！");
        }
        //System.out.println("temp:" + Arrays.toString(size));


        int t = 0;
        //stringBuilder.append("\"");
        while (true) {
            t = clip.indexOf("<path", t + 1);
            if (t == -1)
                break;

            temp = clip.substring(t, clip.indexOf(">", t));
            //System.out.println(temp);

            if (temp.replaceAll(" ", "").indexOf("fill=\"none\"") != -1)
                continue;
            if (temp.replaceAll(" ", "").indexOf("M" + size[0] + "," + size[1] + "h" + size[2] + "v" + size[3] + "H" + size[0] + "V" + size[1] + "z") != -1)
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

        stringBuilder.append("M" + size[0] + "," + size[1] + "z ");
        stringBuilder.append("M" + size[2] + "," + size[3] + "z");
        //stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        //stringBuilder.append("\"");

        return stringBuilder.toString();
    }

    public static void depthTraversal(Path path, DepthTraversalCallback callback) throws IOException {
        int nameCount = path.getNameCount();
        Files.walkFileTree(path, new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                callback.findFile(file,file.subpath(path.getNameCount(), file.getNameCount()));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public interface DepthTraversalCallback {
        void findFile(Path path ,Path subPath);
    }
}
