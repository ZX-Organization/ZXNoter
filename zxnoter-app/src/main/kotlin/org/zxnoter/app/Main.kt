package org.zxnoter.app


import org.zxnoter.api.extension.Extension
import org.zxnoter.utils.treeNode
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import java.net.URLClassLoader


class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Hello, ZXNoter!")
            val extensionLoader = JarClassLoader(File("runtime/extensions/example.jar"))
            loadPluginClass(extensionLoader, "org.zxnoter.example.ExampleExtension").execute()
            println(treeNode<String>().create().treeToString())
        }
    }
}

fun printAsciiArt(text: String, size: Int) {
    val width = size * text.length
    val height = size
    val bi = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g = bi.graphics
    g.font = Font("", Font.ITALIC or Font.PLAIN, (size * 1.4).toInt())
    g.drawString(text, 0, size)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = bi.getRGB(x, y)
            val brightness = (color shr 16 and 0xFF) + (color shr 8 and 0xFF) + (color and 0xFF)
            print(if (brightness < 100) " " else "#") // 简单的二值化
        }
        println()
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
