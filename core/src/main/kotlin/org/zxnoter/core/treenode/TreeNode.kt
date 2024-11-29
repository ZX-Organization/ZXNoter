package org.zxnoter.core.treenode

import org.zxnoter.api.treenode.TreeNode
import java.nio.file.Path


class TreeNode<T> : TreeNode<T> {
    private val children = HashMap<String, TreeNode<T>>()

    //    private val listeners = mutableListOf<TreeNodeListener<T>>()
    private val parent = null as TreeNode<T>?
    private val root = this
    private val value = null as T?
    private val key = String()

    override fun childrenList(): List<TreeNode<T>> = children.values.toList()
    override fun removeNode(key: String): TreeNode<T> {
        TODO("Not yet implemented")
    }

    override fun addNode(key: String, value: T) = TreeNode<T>().also { children[key] = it }

    override fun removeNode(child: TreeNode<T>): TreeNode<T> {
        TODO("Not yet implemented")
    }

    override fun size(): Int = children.size

    override fun parent(): TreeNode<T>? = parent


    override fun root(): TreeNode<T> = root

    override fun children(): Sequence<TreeNode<T>> = children.values.asSequence()

    override fun getValue(): T? = value

    override fun findNodeByPath(path: Path): TreeNode<T>? {
        TODO("Not yet implemented")
    }

    override fun findNodeByRegex(regex: Regex): TreeNode<T>? {
        TODO("Not yet implemented")
    }

    override fun key(): String = key

    override fun path(): Path {
        TODO("Not yet implemented")
    }

    override fun isLeaf(): Boolean {
        TODO("Not yet implemented")
    }

    override fun depth(): Int {
        TODO("Not yet implemented")
    }


}