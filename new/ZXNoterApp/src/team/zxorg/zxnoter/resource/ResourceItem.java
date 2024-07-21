package team.zxorg.zxnoter.resource;

import team.zxorg.zxnoter.api.resource.IResourcePack;
import team.zxorg.zxnoter.api.resource.item.IResourceItem;
import team.zxorg.zxnoter.core.TreeNode;

public class ResourceItem<T> extends TreeNode implements IResourceItem<T> {
    private IResourcePack pack;

    public ResourceItem(String name) {
        super(name);
    }

    @Override
    public IResourcePack getResourcePack() {
        return pack;
    }
}
