package team.zxorg.zxnoter.resource;

import java.util.HashMap;

public class ZXResources {

import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Consumer;

public class ZXResources {
    public static void loadResourcePackage(Path resourcePackagePath) {
        try {
            Files.walk(resourcePackagePath, 1).forEach(path -> {
                try {
                    if (!path.equals(resourcePackagePath)) {
                        if (Files.isDirectory(path)) {
                            String type = path.getFileName().toString();
                            if (type.equals("img")) {

                            } else if (type.equals("lang")) {

                            } else if (type.equals("svg")) {
                                loadSvg(path);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Utils.depthTraversal(resourcePackagePath, (filePath, subPath) -> {
                /*System.out.println(filePath);
                System.out.println(subPath);*/
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadSvg(Path svgPath) throws IOException {
        Files.walk(svgPath).forEach(filePath -> {
            filePath.subpath(svgPath.getNameCount(), filePath.getNameCount());
            System.out.println(filePath);
        });
    }

    public static HashMap<String, Object> allThings = new HashMap<>();

    /**
     * 获取矢量图图标
     *
     * @param key
     * @return
     */
    public static Pane getSvgPane(String key) {
        Pane iconPane = new Pane();
        if (allThings.get(key) instanceof Shape shape)
            iconPane.setShape(shape);
        else
            iconPane = getSvgPane("system.question-line");
        return iconPane;
    }
}
