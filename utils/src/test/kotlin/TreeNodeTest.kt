import org.zxnoter.utils.node.TreeNodeImpl

fun main() {
    val treeNode = TreeNodeImpl<String>();
    val n1 = treeNode.add("aaa", "bb")
    val n2 = n1.add("gg", "ggg")
    treeNode.add("cc", "ddd").add("gg", "ggg")
    println(treeNode.depth())
    println(n1.depth())
    println(n2.depth())
    println(treeNode)
}