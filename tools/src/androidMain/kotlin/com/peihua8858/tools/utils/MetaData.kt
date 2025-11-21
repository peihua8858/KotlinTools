package com.peihua8858.tools.utils

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ApplicationInfoFlags
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import android.content.ComponentName

/**
 * 根据key获取Manifest里面配置的值
 *
 * @author dingpeihua
 * @date 2018/8/27 16:12
 * @version 1.0
 */
fun Context?.getMetaData(key: String, default: String): String {
    if (this == null) {
        return default
    }
    try {
        val appInfo = this.appInfo
        val notifyChannel =
            appInfo.metaData.getString(key)
        if (notifyChannel.isNonEmpty()) {
            return notifyChannel
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return default
}

val Context.appInfo: ApplicationInfo
    get() {
        return if (isTiramisu)
            packageManager.getApplicationInfo(
                packageName,
                ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }
val Context.activityInfo:ActivityInfo
    get() {
        return if (isTiramisu)
            packageManager.getActivityInfo(ComponentName<Activity>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getActivityInfo(ComponentName<Activity>(), PackageManager.GET_META_DATA)
    }
val Context.serviceInfo: ServiceInfo
    get() {
        return if (isTiramisu)
            packageManager.getServiceInfo(ComponentName<Service>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getServiceInfo(ComponentName<Service>(), PackageManager.GET_META_DATA)
    }
val Context.providerInfo: ProviderInfo
    get() {
        return if (isTiramisu)
            packageManager.getProviderInfo(ComponentName<Service>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getProviderInfo(ComponentName<Service>(), PackageManager.GET_META_DATA)
    }
val Context.receiverInfo: ActivityInfo
    get() {
        return if (isTiramisu)
            packageManager.getReceiverInfo(ComponentName<Service>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getReceiverInfo(ComponentName<Service>(), PackageManager.GET_META_DATA)
    }
fun Context.applicationMetaDataOf(name: String): String? =
   appInfo.metaData.getString(name)

fun Context.activityMetaDataOf(name: String): String? =
    activityInfo.metaData.getString(name)

 fun Context.serviceMetaDataOf(name: String): String? =
    serviceInfo.metaData.getString(name)

inline fun <reified T : BroadcastReceiver> Context.providerMetaDataOf(name: String): String? =
    providerInfo.metaData.getString(name)

inline fun <reified T : BroadcastReceiver> Context.receiverMetaDataOf(name: String): String? =
    receiverInfo.metaData.getString(name)

inline fun <reified T> Context.ComponentName() = ComponentName(this, T::class.java)