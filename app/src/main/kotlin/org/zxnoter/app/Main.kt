package org.zxnoter.app

import org.zxnoter.api.extension.Extension
import org.zxnoter.api.node.ZXNoter
import java.io.File
import java.net.URLClassLoader

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Hello, ZXNoter!")
            val extensionLoader = JarClassLoader(File("runtime/extensions/example.jar"))
            loadPluginClass(extensionLoader, "org.zxnoter.example.ExampleExtension").execute()


            val treeNode = ZXNoter.getFactory().createTreeNode<String>();
            val n1 = treeNode.add("aaa", "bb")
            val n2 = n1.add("gg", "ggg")
            treeNode.add("cc", "ddd").add("gg", "ggg")
            println(treeNode.depth())
            println(n1.depth())
            println(n2.depth())
            println(treeNode)
        }
    }
}

class JarClassLoader(jarFile: File, parentLoader: ClassLoader = Thread.currentThread().contextClassLoader) :
    URLClassLoader(
        arrayOf(jarFile.toURI().toURL()),
        parentLoader
    ) {
    fun loadClassFromJar(className: String): Class<*> {
        return super.loadClass(className, true)
    }
}

fun loadPluginClass(loader: JarClassLoader, className: String): Extension {
    val clazz = loader.loadClassFromJar(className)
    return clazz.getDeclaredConstructor().newInstance() as Extension
}
