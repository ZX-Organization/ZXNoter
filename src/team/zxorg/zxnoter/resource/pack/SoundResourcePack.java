package team.zxorg.zxnoter.resource.pack;

import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;

import java.nio.file.Path;

public class SoundResourcePack extends BaseResourcePack {
    public SoundResourcePack(ResourcePack pack, ResourceType type, Path jsonPath) {
        super(pack, type, jsonPath);
    }

    @Override
    public void reload() {

    }
}
