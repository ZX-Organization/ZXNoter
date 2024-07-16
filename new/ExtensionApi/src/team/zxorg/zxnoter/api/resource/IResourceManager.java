package team.zxorg.zxnoter.api.resource;

import java.util.List;

public interface IResourceManager {
    List<IResourcePack> getResourcePacks();

    IResourcePack getResourcePack(String name);
}
