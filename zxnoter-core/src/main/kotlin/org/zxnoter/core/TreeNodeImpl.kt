package org.zxnoter.core

import org.zxnoter.api.node.NodePath
import org.zxnoter.api.node.TreeNode


class TreeNodeImpl<T> : TreeNode<T> {
    constructor()
    constructor(value: T?) {
        this.value = value
    }

    constructor(name: String, value: T?) {
        this.name = name
        this.value = value
    }


    override var value: T? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override var name: String = "root"
        set(newName) {
            if (newName.isEmpty())
                throw IllegalArgumentException("Name cannot be empty")
            field = newName
        }

    override var depth: Int = 0

    override fun create(): TreeNode<T> {
        return TreeNodeImpl()
    }

    override fun remove(name: String): TreeNode<T> {
        val node = children[indexOf(name)]
        children.remove(node)
        return node
    }

    override fun put(node: TreeNode<T>): TreeNode<T> {
        if (this.findParent(node)) {
            throw IllegalArgumentException("Cannot add node: circular reference detected")
        }
        val index = indexOf(node)
        if (index != -1) {
            children[index].remove(node)
        }
        children.add(node)
        node.parent = this
        return node
    }

    override fun toString(): String {
        return "$parent > TreeNode(name: $name ,value: $value ,children: ${children.size})"
    }


    override val children = mutableListOf<TreeNode<T>>()

    @Transient
    override var parent = null as TreeNode<T>?
        set(parent) {
            if (parent == field)
                return
            if (this.findParent(parent)) {
                throw IllegalArgumentException("Cannot add node: circular reference detected")
            }
            field?.remove(name)
            field = parent
            if (field?.indexOf(this) == -1)
                field?.put(this)
            root = field?.root ?: this
            depth = 0
            var p = this.parent
            while (p != null) {
                p = p.parent
                depth++
            }
        }

    override var root = this as TreeNode<T>

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

