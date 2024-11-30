package org.zxnoter.core.node


import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.internal.readOnly
import org.zxnoter.api.node.NodePath
import org.zxnoter.api.node.TreeNode
import java.nio.file.Path

@Serializable
class TreeNodeImpl<T>(
    private var value: T? = null
) : TreeNode<T> {
    private var name = null as String?

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
    override fun value(): T? = value


    override fun name(): String? = name

    override fun path(): Path {
        TODO("Not yet implemented")
        /* val path = Path.of("")
         var node = this.parent
         var depth = 0
         while (true) {
             if (node == null) break
             node = node.parent()
             depth++
         }
         return depth*/
    }

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

    /*  fun serializable() = SerializableRequest(value, bodyType)
        @Serializable(SerializableRequest.Serializer::class)
        class SerializableRequest(val body: Any?, val bodyType: KType) {
            class Serializer : KSerializer<SerializableRequest> {
                // ... serialize by using bodyType to create a serializer
            }
        }*/

    override fun toString(): String {
        return Json.encodeToString(this)
    }
}

class NodePathImpl(vararg path: String) : NodePath {
    private val nodePath = mutableListOf<String>()

    override fun nodes(): List<String> = nodePath.readOnly()

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

