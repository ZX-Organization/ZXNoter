package org.zxnoter.utils.node


/**
 * 树节点
 */
interface TreeNode<T> {
    /**
     * 获取子节点数量
     */
    fun size(): Int

    /**
     * 获取父节点
     * @return 如果当前节点是根节点则返回 null
     */
    fun parent(): TreeNode<T>?

    /**
     * 获取树的根节点
     */
    fun root(): TreeNode<T>

    /**
     * 获取所有子节点
     */
    fun children(): List<TreeNode<T>>

    /**
     * 增加子节点
     * @return 新增的节点
     */
    fun add(name: String, value: T): TreeNode<T>

    /**
     * 获取子节点
     */
    fun get(name: String): TreeNode<T>?

    /**
     * 移除子节点
     * @return 被移除的节点
     */
    fun remove(name: String): TreeNode<T>?

    /***
     * 获取节点值
     */
    val value: T?

    /**
     * 查找路径对应的节点
     */
    fun find(regex: Regex): List<TreeNode<T>>

    /**
     * 当前节点的名称
     */
    val name: String?

    /**
     * 获取当前节点的路径
     */
    fun path(): NodePath

    /**
     * 检查当前节点是否为叶子节点
     */
    fun isLeaf(): Boolean

    /**
     * 获取当前节点的深度
     */
    fun depth(): Int

    /**
     * 添加一个监听器
     */
//    fun addListener(listener: TreeNodeListener<T>)

    /**
     * 移除一个监听器
     */
//    fun removeListener(listener: TreeNodeListener<T>)
}


