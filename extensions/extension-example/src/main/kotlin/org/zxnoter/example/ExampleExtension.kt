package org.zxnoter.example

import org.zxnoter.api.extension.Extension

class ExampleExtension : Extension {
    override fun execute() {

    }

    override val name: String
        get() = "ExampleExtension"
    override val namespace: String
        get() = "org"

}