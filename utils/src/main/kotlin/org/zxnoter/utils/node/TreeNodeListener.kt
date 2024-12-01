package org.zxnoter.utils.node

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
