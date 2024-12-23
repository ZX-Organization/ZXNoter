import org.zxnoter.utils.treeNode
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

fun main() {
    val node = treeNode<Path>()
    Files.walkFileTree(Paths.get("./"), object : SimpleFileVisitor<Path>() {
        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
            var p = file.toString().substring(2).split("\\")
            node.getOrCreate(p).value = file
            return FileVisitResult.CONTINUE
        }
    })
    node.name = "我是歌姬"
    println(node.get(0)?.get(1)?.name)
//    println(node.treeToString(StringBuilder()))
}