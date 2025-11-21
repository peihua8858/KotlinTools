@file:JvmName("Utils")
@file:JvmMultifileClass

package com.peihua8858.tools.utils

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