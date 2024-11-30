package org.zxnoter.api.node

import java.nio.file.Path


interface Factory {
    fun <T> createTreeNode(): TreeNode<T>
}

object ZXNoter {
    fun getFactory(): Factory = Class.forName("org.zxnoter.core.Factory").getConstructor().newInstance() as Factory
}

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
    fun value(): T?

    /**
     * 查找路径对应的节点
     */
    fun find(regex: Regex): List<TreeNode<T>>

    /**
     * 当前节点的名称
     * @return 如果当前节点是根节点则返回 null
     */
    fun name(): String?

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

interface NodePath {
    /**
     * 获取路径中的所有节点。
     * @return 返回一个包含路径上所有节点的列表，按层级顺序排列。
     */
    fun nodes(): List<String>

    /**
     * 获取当前路径的父路径。
     * 如果当前路径已经是根路径（即没有父节点），则返回 null。
     * @return 父路径的 NodePath 实现
     */
    fun parent(): NodePath

    /**
     * 获取路径中的最后一个节点（即路径的最右端节点）。
     * @return 返回路径中最后一个节点的名称。
     */
    fun lastNode(): String

    /**
     * 解析当前路径并返回一个新的路径，该路径为当前路径拼接一个新的子节点。
     * @param child 新的子节点名称。
     * @return 返回拼接后的新路径。
     */
    fun resolve(child: String): NodePath

    /**
     * 判断当前路径是否为根路径。
     * 根路径是指路径中没有任何节点，或者是顶级路径（不再有父路径）。
     * @return 如果是根路径返回 true，否则返回 false。
     */
    fun isRoot(): Boolean

    /**
     * 获取当前路径的深度（即路径中节点的数量）。
     * @return 返回路径的深度，深度为路径中节点的数量。
     */
    fun depth(): Int
}