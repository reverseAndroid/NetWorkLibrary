package com.fgms.mobile.network


object NetWorkConstant {

    const val SETTINGS_PACKAGE: String = "com.android.settings"

    const val SETTINGS_CLASS: String = "com.android.settings.WirelessSettings"

    const val ACTION_VIEW: String = "android.intent.action.VIEW"

    const val PRAGMA: String = "Pragma"

    const val RESPONSE: String = "response"

    const val Cache_Control: String = "Cache-Control"

    const val REQUEST_HEADER_CONTENT_TYPE_KEY: String = "Content-Type"

    const val REQUEST_HEADER_CONTENT_TYPE_VALUE: String = "application/json"

    const val REQUEST_HEADER_ACCEPT_ENCODING_KEY: String = "Accept-Encoding"

    const val REQUEST_HEADER_ACCEPT_ENCODING_VALUE: String = "gzip, deflate"

    const val REQUEST_HEADER_CONNECTION_KEY: String = "Connection"

    const val REQUEST_HEADER_CONNECTION_VALUE: String = "keep-alive"

    const val REQUEST_HEADER_ACCEPT_KEY: String = "Accept"

    const val REQUEST_HEADER_ACCEPT_VALUE: String = "*/*"

    const val REQUEST_HEADER_COOKIE_KEY: String = "Cookie"

    const val REQUEST_HEADER_COOKIE_VALUE: String = "add cookies here"

    const val REQUEST_HEADER_TOKEN_KEY: String = "Authorization"

    const val REQUEST_HEADER_X_REQUESTID_KEY: String = "x-requestid"

    private const val PACKAGE_NAME: String = "com.fgms.mobile.authentication.AuthenticationContentProvider"

    //表名
    private const val TABLE_NAME: String = "Authentication"

    const val TOKEN: String = "token"

    //对外暴露出可以访问contentProvider的Uri
    const val ACCESS_CONTENT_PROVIDER_URI: String = "content://${PACKAGE_NAME}/${TABLE_NAME}"

    //访问的域名
    const val HOSTNAME: String = "https://fgms-idsrv.fgms.dev.com"

    //连接超时时间
    var CONNECT_TIMEOUT = 30L

    //读取超时时间
    var READ_TIMEOUT = 30L

    //写入超时时间
    var WRITE_TIMEOUT = 30L

    //证书名字
    var CA_NAME = "ca.cer"
}