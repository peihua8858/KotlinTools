@file:JvmName("Utils")
@file:JvmMultifileClass

package com.peihua8858.tools.utils

import android.content.Context
import android.os.Looper
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import java.util.Locale

fun Any?.isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

fun Any?.checkMainThread(msg: String?): Boolean {
    if (isMainThread()) {
        return true
    }
    throw IllegalStateException(msg)
}
/**
 * 布局方向是从右到左
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Any?.isAppRtl(local: Locale?): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(local) == ViewCompat.LAYOUT_DIRECTION_RTL
}

/**
 * 布局方向是从右到左
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Locale?.isAppRtl(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(this) == ViewCompat.LAYOUT_DIRECTION_RTL
}

/**
 * 布局方向是从左到右
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Locale?.isAppLtr(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(this) == ViewCompat.LAYOUT_DIRECTION_LTR
}

/**
 * 布局方向是从左到右
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Any?.isAppLtr(local: Locale?): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(local) == ViewCompat.LAYOUT_DIRECTION_LTR
}
fun Context.isRtl():Boolean{
    if (isAtLeastN) {
        return resources.configuration.locales.get(0).isAppRtl()
    }
    return resources.configuration.locale.isAppRtl()
}

fun Any.toMap(): MutableMap<String, Any> {
    val fields = javaClass.declaredFields
    val result = mutableMapOf<String, Any>()
    try {
        for (field in fields) {
            val key = field.name
            if (key.isNotEmpty()) {
                field.isAccessible = true
                val value = field.get(this)
                if (value != null) {
                    result[key] = value
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}