package com.fgms.mobile.network

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.M)
object NetUtils {

    /**
     * 判断当前网络是否可以上网并吐司提醒
     */
    fun isConnectedAndToast(context: Context): Boolean {
        val flag = isNetworkAvailable(context)
        if (!flag) {
            Toast.makeText(context, R.string.check_network_status, Toast.LENGTH_SHORT).show()
        }
        return flag
    }

    /**
     * 判断网络是否可用
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.getActiveNetwork() ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * 判断是否是wifi连接
     */
    fun isWifi(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.getActiveNetwork() ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    /**
     *
     * 判断是否是移动网络
     */
    fun isMobile(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.getActiveNetwork() ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: Activity) {
        val intent = Intent("/")
        val cm = ComponentName(NetWorkConstant.SETTINGS_PACKAGE, NetWorkConstant.SETTINGS_CLASS)
        intent.component = cm
        intent.action = NetWorkConstant.ACTION_VIEW
        activity.startActivityForResult(intent, 0)
    }
}