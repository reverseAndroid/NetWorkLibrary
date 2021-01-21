package com.fgms.mobile.network

import android.content.Context


object FileUtils {

    fun getJson(context: Context, fileName: String): String? {
        // 读取本地asset文件，然后并转换成json
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}