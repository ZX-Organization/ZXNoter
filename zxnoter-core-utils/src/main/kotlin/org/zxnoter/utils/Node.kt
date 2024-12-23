package org.zxnoter.utils

import org.zxnoter.api.node.TreeNode
import org.zxnoter.core.TreeNodeImpl


fun <T> treeNode(): TreeNode<T> {
    return TreeNodeImpl()
}

fun <T> treeNode(value: T? = null): TreeNode<T> {
    return TreeNodeImpl(value)
}

fun <T> treeNode(name: String, value: T? = null): TreeNode<T> {
    return TreeNodeImpl(name, value)
}

