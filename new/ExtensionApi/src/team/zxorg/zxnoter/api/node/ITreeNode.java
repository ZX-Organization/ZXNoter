package team.zxorg.zxnoter.api.node;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface ITreeNode {
    /**
     * 路径解析
     * @param root 根节点
     * @param path 节点路径
     * @return 目标节点
     */
    static ITreeNode resolve(@NotNull ITreeNode root, @NotNull String path) {
        String[] parts = path.split("\\.");
        ITreeNode subKey = root;
        for (String part : parts) {
            if (subKey == null)
                return null;
            subKey = subKey.getNode(part);
        }
        return subKey;
    }

    /**
     * 获取节点值
     *
     * @return 值对象
     */
    <T> T getValue(@NotNull Class<T> tclass);

    /**
     * @param value 设置值
     */
    <T> void setValue(@NotNull T value);

    /**
     * 获取父节点
     *
     * @return 父节点，如果没有父节点则返回null
     */
    ITreeNode getParentNode();

    /**
     * 获取节点名
     *
     * @return 节点名
     */
    String getName();

    /**
     * 增加子节点
     *
     * @param name 子节点名
     * @return 子节点对象
     */
    ITreeNode addNode(@NotNull String name);

    /**
     * 获取子节点
     *
     * @param name 子节点名
     * @return 子节点对象
     */
    ITreeNode getNode(@NotNull String name);


    /**
     * 获取节点的完整路径
     *
     * @return 完整节点路径，以"."分隔符连接的路径
     */
    String getPath();

    /**
     * 获取子节点集合
     *
     * @return 子节点集合
     */

    Collection<ITreeNode> getChildren();

    /**
     * 按名称查找子节点
     *
     * @param name 节点名
     * @return 节点
     */
    ITreeNode findByName(String name);

    /**
     * 按条件查找节点
     *
     * @param condition 条件
     * @return 符合条件的节点
     */
    List<ITreeNode> findByCondition(Predicate<String> condition);
}
