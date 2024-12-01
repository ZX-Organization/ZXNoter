package org.zxnoter.utils.node


class TreeNodeImpl<T>(value: T? = null) : TreeNode<T> {
    override var value: T? = value
        get() {
            return field
        }
        set(name) {
            field = name
        }

    override var name: String? = null
        get() {
            return field
        }
        set(name) {
            field = name
        }

    override fun path(): NodePath {
        TODO("Not yet implemented")
    }

    operator fun plus(other: TreeNodeImpl<T>): TreeNodeImpl<T> {
        return TreeNodeImpl(this.value ?: this.value)
    }


    private val children = mutableMapOf<String, TreeNodeImpl<T>>()

    @Transient
    private var parent = null as TreeNode<T>?

    @Transient
    private var root = this

    //    private val listeners = mutableListOf<TreeNodeListener<T>>()

    override fun get(name: String): TreeNode<T>? {
        TODO("Not yet implemented")
    }

    override fun remove(name: String): TreeNode<T> {
        TODO("Not yet implemented")
    }

    override fun find(regex: Regex): List<TreeNode<T>> {
        TODO("Not yet implemented")
    }

    override fun add(name: String, value: T) = TreeNodeImpl<T>().also {
        it.parent = this
        it.root = this.root
        it.name = name
        it.value = value
        children[name] = it
    }

    override fun size(): Int = children.size

    override fun parent(): TreeNode<T>? = parent


    override fun root(): TreeNode<T> = root
    override fun children(): List<TreeNode<T>> = children.values.toList()


    override fun isLeaf(): Boolean {
        return children.isEmpty()
    }

    override fun depth(): Int {
        var node = this.parent
        var depth = 0
        while (true) {
            if (node == null) break
            node = node.parent()
            depth++
        }
        return depth
    }
}


class NodePathImpl(vararg path: String) : NodePath {
    private val nodePath = mutableListOf<String>()

    override fun nodes(): List<String> = nodePath

    override fun parent(): NodePath {
        return NodePathImpl(nodePath[nodePath.size - 2])
    }

    override fun lastNode(): String {
        TODO("Not yet implemented")
    }

    override fun resolve(child: String): NodePath {
        TODO("Not yet implemented")
    }

    override fun isRoot(): Boolean {
        TODO("Not yet implemented")
    }

    override fun depth(): Int {
        TODO("Not yet implemented")
    }

}

