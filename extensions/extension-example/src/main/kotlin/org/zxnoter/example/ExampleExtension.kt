package org.zxnoter.example

import org.zxnoter.api.extension.Extension
import org.zxnoter.utils.Node

class ExampleExtension : Extension {
    override fun execute() {
        val node1 = Node.createTreeNode("rn1", 1)
        val n = node1.put("111").put("2222")
        node1.put("222").put("3333").put("44444")
        node1.put("333")
        n.put("cs")
        n.put("c2")
        n.put("c3")

        println(node1.treeToString(StringBuilder()))
    }

    override fun name(): String {
        return "ExampleExtension"
    }

    override fun namespace(): String {
        return "org.zxnoter.example"
    }
}