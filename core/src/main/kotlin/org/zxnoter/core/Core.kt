package org.zxnoter.core

import org.zxnoter.api.treenode.Factory
import org.zxnoter.api.treenode.TreeNode


class Factory : Factory {
    override fun <T> createTreeNode(): TreeNode<T> {
        return org.zxnoter.core.treenode.TreeNode<T>()
    }
}