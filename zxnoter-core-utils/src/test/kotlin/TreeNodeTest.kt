import org.zxnoter.utils.Node
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

fun main() {
    val node = Node.createTreeNode<Path>()
    Files.walkFileTree(Paths.get("./"), object : SimpleFileVisitor<Path>() {
        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
            var p = file.toString().substring(2).split("\\")
            node.getOrCreate(p).value = file
            return FileVisitResult.CONTINUE
        }
    })

    println(node.treeToString(StringBuilder()))
}