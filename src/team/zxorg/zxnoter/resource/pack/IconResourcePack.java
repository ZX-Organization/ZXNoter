package team.zxorg.zxnoter.resource.pack;

import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.resource.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class IconResourcePack extends BaseResourcePack {
    private final HashMap<String, SVGPath> icons = new HashMap<>();
    private Image icon;

    public IconResourcePack(ResourcePack pack, ResourceType type, Path jsonPath) {
        super(pack, type, jsonPath);
    }

    public SVGPath getIcon(String key) {
        return icons.get(key);
    }

    public HashMap<String, SVGPath> getIcons() {
        return icons;
    }

    public Image getResourceIcon() {
        return icon;
    }

    @Override
    public void reload() {
        Path rootPath = getPath();
        try (Stream<Path> files = Files.walk(rootPath)) {
            Iterator<Path> iterator = files.iterator();
            Path file;
            while (iterator.hasNext()) {
                file = iterator.next();
                String fileName = file.getFileName().toString();
                if (fileName.endsWith(".svg")) {
                    String key = file.subpath(rootPath.getNameCount(), file.getNameCount()).toString();
                    key = key.substring(0, key.lastIndexOf("."));
                    key = key.replaceAll("[/\\\\]", ".");
                    SVGPath svgPath = new SVGPath();
                    svgPath.setContent(ResourceUtils.readSvg(file));
                    icons.put(key, svgPath);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        icon = new Image(rootPath.resolve(getInfo().getString("icon")).toUri().toString());
    }
}
