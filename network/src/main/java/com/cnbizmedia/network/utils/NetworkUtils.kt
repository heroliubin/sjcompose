@file:Suppress("DEPRECATION")

package com.cnbizmedia.network.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.cnbizmedia.network.BuildConfig

/** 网络是否可用 **/
@SuppressLint("MissingPermission")
fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // if (BuildConfig.DEBUG) return true //默认网络可用
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        @Suppress("DEPRECATION")
        cm.activeNetworkInfo?.isAvailable ?: false
    else {
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        val hasCapability =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ?: false //这个标识才能真的表示能上网
        hasCapability && (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
                || networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true)
    }
}


/**
 * 获取网络是否可用
 * @return isAvailable to NetworkType
 */
@SuppressLint("MissingPermission")
fun Context.getNetworkAvailableInfo(): Pair<Boolean, Int?> {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (BuildConfig.DEBUG) return true to null//默认网络可用
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        @Suppress("DEPRECATION")
        val activeNetworkInfo = cm.activeNetworkInfo
        (activeNetworkInfo?.isAvailable ?: false) to activeNetworkInfo?.type
    } else {
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        val hasCapability =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ?: false //这个标识才能真的表示能上网
        if (hasCapability) {
            if (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true)
                true to NetworkCapabilities.TRANSPORT_CELLULAR
            else if (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true)
                true to NetworkCapabilities.TRANSPORT_WIFI
            else false to null
        } else false to null
    }
}


/**
 * 获取网络类型
 * @return wifi or cellular
 */

@SuppressLint("MissingPermission")
fun Context.getNetworkConnectedType(): String {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = cm.allNetworkInfo
    info.forEach {
        if (it.state == NetworkInfo.State.CONNECTED) {
            if (ConnectivityManager.TYPE_WIFI == it.type) {
                return "wifi"
            }
        }
    }
    return "cellular"
}