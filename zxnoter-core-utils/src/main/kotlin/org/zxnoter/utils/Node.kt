package org.zxnoter.utils

import org.zxnoter.api.node.TreeNode
import org.zxnoter.core.TreeNodeImpl

object Node {
    fun <T> createTreeNode(): TreeNode<T> {
        return TreeNodeImpl()
    }

    fun <T> createTreeNode(value: T? = null): TreeNode<T> {
        return TreeNodeImpl(value)
    }

    fun <T> createTreeNode(name: String, value: T? = null): TreeNode<T> {
        return TreeNodeImpl(name, value)
    }
}
