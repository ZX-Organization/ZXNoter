package team.zxorg.zxnoter.resource.type;

import team.zxorg.zxnoter.resource.ResourcePack;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

public class LayoutResource extends Resource {
    private final ArrayList<Path> cssFiles = new ArrayList<>();

    public LayoutResource(ResourcePack pack, ResourceType type, Path jsonPath) {
        super(pack, type, jsonPath);
    }

    public Iterator<Path> iterator() {
        return cssFiles.iterator();
    }

    @Override
    public void reload() {
        cssFiles.clear();
        Path path;
        for (String file : getInfo().getObject("style", String[].class)) {
            path = getPath().resolve(file);
            if (Files.exists(path)) {
                cssFiles.add(path);
            } else {
                System.err.println("LayoutResource:引用的样式文件不存在 " + path);
            }
        }
    }
}
