package org.zxnoter.api.util

interface Version {
    val code: Long
    val codes: List<Int>
    val value: String

    /**
     * 主版本号
     */
    val major: Int

    /**
     * 次版本号
     */
    val minor: Int

    /**
     * 补丁版本号
     */
    val patch: Int

    /**
     *预发布
     */
    val prerelease: String

    val buildMetadata: String
}