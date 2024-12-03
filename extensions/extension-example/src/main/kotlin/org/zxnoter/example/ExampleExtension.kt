package org.zxnoter.example

import org.zxnoter.api.extension.Extension
import org.zxnoter.utils.Node
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

class ExampleExtension : Extension {
    override fun execute() {

    }

    override fun name(): String {
        return "ExampleExtension"
    }

    override fun namespace(): String {
        return "org.zxnoter.example"
    }
}