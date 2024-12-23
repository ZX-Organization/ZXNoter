package org.zxnoter.api.node


/**
 * 树节点
 */
interface TreeNode<T> {

    val children: List<TreeNode<T>>

    /**
     * 父节点
     */
    var parent: TreeNode<T>?

    /**
     * 根节点
     */
    val root: TreeNode<T>

    /***
     * 节点值
     */
    var value: T?

    /**
     * 节点名称
     */
    var name: String

    /**
     * 获取当前节点的深度
     */
    val depth: Int


    fun create(): TreeNode<T>
    fun create(name: String): TreeNode<T> {
        val node = create()
        node.name = name
        return node
    }
    fun create(name: String, value: T?): TreeNode<T> {
        val node = create(name)
        node.value = value
        return node
    }

    fun put(name: String): TreeNode<T> {
        val node = create(name)
        return put(node)
    }

    fun put(name: String, value: T): TreeNode<T> {
        val node = create(name, value)
        return put(node)
    }

    fun put(node: TreeNode<T>): TreeNode<T>

    fun get(name: String): TreeNode<T>? {
        return children.find { it.name == name }
    }

    fun get(index: Int): TreeNode<T>? {
        return children[index]
    }

    fun remove(name: String): TreeNode<T>

    fun remove(node: TreeNode<T>): TreeNode<T> {
        return remove(node.name)
    }

    fun indexOf(name: String): Int {
        return children.indexOfFirst { it.name == name }
    }

    fun indexOf(node: TreeNode<T>): Int {
        return indexOf(node.name)
    }

    fun find(node: TreeNode<T>?): Boolean {
        if (node == null) return false
        if (node == this) return true

        children.forEach {
            if (it.find(node)) return true
        }
        return false
    }

    fun findParent(node: TreeNode<T>?): Boolean {
        if (node == null) return false
        if (node == this) return true
        if (parent != null) if (parent!!.findParent(node)) return true
        return false
    }

    operator fun plus(other: TreeNode<T>): TreeNode<T> {
        return put(other)
    }

    operator fun minus(other: TreeNode<T>): TreeNode<T> {
        return remove(other)
    }

    fun treeToString(sb: StringBuilder = StringBuilder(), isLast: Boolean = true, prefix: String = ""): StringBuilder {
        val connector = if (isLast) "┗━" else "┣━"
        val nodePrefix = if (prefix.isEmpty()) "" else connector
        sb.append("$prefix$nodePrefix[${name}]")
        if (value != null) {
            sb.append(": $value")
        }
        sb.append("\n")
        val newPrefix = prefix + if (isLast) "\t" else "┃\t"
        for ((index, child) in children.withIndex()) {
            child.treeToString(sb, isLast = (index == children.size - 1), prefix = newPrefix)
        }
        return sb
    }

    fun getOrCreate(path: List<String>): TreeNode<T> {
        var currentNode = this
        for (i in path.indices) {
            val child = currentNode.children.find { it.name == path[i] }
            if (child != null) {
                currentNode = child
            } else {
                val newNode = currentNode.create(path[i])
                currentNode.put(newNode)
                currentNode = newNode
            }
        }
        return currentNode
    }


}


