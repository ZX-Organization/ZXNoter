package org.zxnoter.api.extension

interface Extension {
    fun execute()
    fun name(): String
    fun namespace(): String
}