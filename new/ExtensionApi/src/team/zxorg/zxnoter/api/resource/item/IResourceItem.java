package team.zxorg.zxnoter.api.resource.item;

import team.zxorg.zxnoter.api.node.ITreeNode;
import team.zxorg.zxnoter.api.resource.IResourcePack;

public interface IResourceItem<T> extends ITreeNode {

    /**
     * 获取来源包
     *
     * @return 资源包
     */
    IResourcePack getResourcePack();


}
