package org.zxnoter.core

import org.zxnoter.api.node.Factory
import org.zxnoter.api.node.TreeNode


class Factory : Factory {
    override fun <T> createTreeNode(): TreeNode<T> {
        return org.zxnoter.core.node.TreeNodeImpl()
    }
}