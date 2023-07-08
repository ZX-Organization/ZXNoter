package team.zxorg.zxnoter.resource.type;

import team.zxorg.zxnoter.resource.ResourcePack;

import java.nio.file.Path;

public class AudioResource extends Resource{
    public AudioResource(ResourcePack pack, ResourceType type, Path jsonPath) {
        super(pack, type, jsonPath);
    }

    @Override
    public void reload() {

    }
}
