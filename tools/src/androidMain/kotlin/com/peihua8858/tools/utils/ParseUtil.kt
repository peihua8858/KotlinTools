@file:JvmName("ParseUtil")
@file:JvmMultifileClass

package com.peihua8858.tools.utils

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes


/**
 * 将Object对象转成String类型
 *
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(): String = toString("")

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(defaultValue: String = ""): String {
    return when {
        this is String -> {
            this
        }
        this is EditText -> {
            text.toString()
        }
        this is TextView -> {
            text.toString()
        }
        this != null -> {
            this.toString()
        }
        else -> defaultValue
    }
}

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(context: Context?, value: Any?, @StringRes resId: Int): String {
    return value.toString(context?.getString(resId) ?: "")
}

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(context: Context?, @StringRes resId: Int): String {
    return toString(context?.getString(resId) ?: "")
}

