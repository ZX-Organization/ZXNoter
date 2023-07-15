package team.zxorg.zxnoter.resource.pack;

import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;

import java.nio.file.Path;

public class StyleResourcePack extends BaseResourcePack {
    public StyleResourcePack(ResourcePack pack, ResourceType type, Path jsonPath) {
        super(pack, type, jsonPath);
    }

    @Override
    public void reload() {

    }
}
