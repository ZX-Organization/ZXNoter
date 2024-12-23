package org.zxnoter.api.extension

interface Extension {
    fun execute()
    val name: String
    val namespace: String
    val meta: ExtensionMeta
        get() {
            return ExtensionMeta("az", "a", listOf("d"), listOf())
        }
}

data class ExtensionMeta(
    val name: String,
    val namespace: String,
    val entry: List<String>,
    val dependencies: List<Dependence>
)

data class Dependence(
    val name: String,
    val version: String,
)
