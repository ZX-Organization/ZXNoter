package team.zxorg.zxnoter.resource;

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
            Files.walk(resourcePackagePath).forEach(path -> {
                try {
                    if (!Files.isDirectory(path)) {
                        String type = path.getFileName().toString();
                        type = type.substring(type.lastIndexOf(".") + 1).toLowerCase();
                        String key = path.subpath(resourcePackagePath.getNameCount(), path.getNameCount()).toString();
                        key = key.substring(0, key.lastIndexOf(".")).replaceAll("[\\\\/]", ".").toLowerCase();
                        if (type.equals("png")) {

                        } else if (type.equals("lang")) {

                        } else if (type.equals("svg")) {
                            SVGPath svg = new SVGPath();
                            svg.setContent(Utils.readSvg(path));
                            allThings.put(key, svg);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            iconPane = getSvgPane("svg.icons.system.question-line");
        return iconPane;
    }
}
