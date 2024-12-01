package org.zxnoter.utils.node

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