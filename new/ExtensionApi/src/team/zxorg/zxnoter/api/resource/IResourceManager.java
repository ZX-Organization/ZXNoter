package team.zxorg.zxnoter.api.resource;

import team.zxorg.zxnoter.api.resource.item.IResourceItem;

import java.util.List;

public interface IResourceManager {
    List<IResourcePack> getResourcePacks();

    IResourcePack getResourcePack(String name);

    /**
     * 获取资源项
     *
     * @param key 资源Key
     * @param <T> 资源类型
     * @return 资源项
     */
    <T extends IResourceItem> T getItem(Class<T> tClass, String key);
}
