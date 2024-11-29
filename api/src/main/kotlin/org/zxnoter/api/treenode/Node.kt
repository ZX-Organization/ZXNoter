package org.zxnoter.api.treenode

import java.nio.file.Path


interface Factory {
    fun <T> createTreeNode(): TreeNode<T>
}

object ZXNoter {
    fun getFactory(): Factory = Class.forName("org.zxnoter.core.Factory").getConstructor().newInstance() as Factory
}

interface TreeNode<T> {


    /**
     * 获取当前节点的大小
     */
    fun size(): Int

    /**
     * 获取父节点，如果当前节点是根节点则返回 null
     */
    fun parent(): TreeNode<T>?

    /**
     * 获取树的根节点，根节点通常是非空的
     */
    fun root(): TreeNode<T>

    /**
     * 获取当前节点的所有子节点
     */
    fun children(): Sequence<TreeNode<T>>

    /**
     * 获取当前节点的所有子节点
     */
    fun childrenList(): List<TreeNode<T>>

    /**
     * 增加子节点
     */
    fun addNode(key: String, value: T): TreeNode<T>

    /**
     * 移除子节点
     */
    fun removeNode(key: String): TreeNode<T>

    /**
     * 移除子节点
     */
    fun removeNode(child: TreeNode<T>): TreeNode<T>

    /***
     * 获取当前节点的值
     */
    fun getValue(): T?

    /**
     * 查找路径对应的节点
     */
    fun findNodeByPath(path: Path): TreeNode<T>?

    /**
     * 查找路径对应的节点
     */
    fun findNodeByRegex(regex: Regex): TreeNode<T>?

    /**
     * 当前节点的唯一标识符
     */
    fun key(): String?

    /**
     * 获取当前节点的路径
     */
    fun path(): Path

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

interface TreeNodeListener<T> {
    /**
     * 当节点被添加时触发
     */
    fun onNodeAdded(node: TreeNode<T>)

    /**
     *  当节点被移除时触发
     */
    fun onNodeRemoved(node: TreeNode<T>)

    /**
     * 当节点的值发生变化时触发
     */
    fun onNodeUpdated(node: TreeNode<T>)

    /**
     * 当子节点发生变化
     */
    fun onChildUpdated(child: TreeNode<T>)
}
