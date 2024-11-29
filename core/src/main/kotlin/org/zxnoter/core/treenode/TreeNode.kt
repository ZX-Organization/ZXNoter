package org.zxnoter.core.treenode

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.zxnoter.api.treenode.TreeNode
import java.nio.file.Path

@Serializable
class TreeNode<T> : TreeNode<T> {
    private val children = HashMap<String, TreeNode<T>>()

    //    private val listeners = mutableListOf<TreeNodeListener<T>>()
    @Transient
    private var parent = null as TreeNode<T>?

    @Transient
    private var root = this

    private var value = null as T?
    private var key = String()

    override fun childrenList(): List<TreeNode<T>> = children.values.toList()
    override fun removeNode(key: String): TreeNode<T> {
        TODO("Not yet implemented")
    }

    override fun addNode(key: String, value: T) = TreeNode<T>().also {
        it.parent = this
        it.root = this.root
        it.key = key
        it.value = value
        children[key] = it
    }

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

   /* override fun toString(): String {
        val serializer = serializer<T>()
        return Json.encodeToString(serializer, this)
    }

    object PersonSerializer : KSerializer<TreeNode> {
        override val descriptor: SerialDescriptor = SerialDescriptor("Person")

        override fun serialize(encoder: Encoder, value: Person) {
            val jsonObject = buildJsonObject {
                put("name", value.name)
                put("age", value.age)
            }
            encoder.encodeJsonElement(jsonObject)
        }

        override fun deserialize(decoder: Decoder): Person {
            val jsonElement = decoder.decodeJsonElement()
            val name = jsonElement.jsonObject["name"]!!.jsonPrimitive.content
            val age = jsonElement.jsonObject["age"]!!.jsonPrimitive.int
            return Person(name, age)
        }
    }*/
}