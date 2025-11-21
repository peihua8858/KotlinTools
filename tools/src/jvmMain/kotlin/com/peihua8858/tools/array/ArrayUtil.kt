@file:JvmName("ArrayUtil")
@file:JvmMultifileClass

package com.peihua8858.tools.array

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * 对象深度拷贝
 * [V] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <V : Serializable> Array<V>.deepClone(): Array<V>? {
    try {
        ByteArrayOutputStream().use { byteOut ->
            ObjectOutputStream(byteOut).use { out ->
                out.writeObject(this)
                out.flush()
                ObjectInputStream(ByteArrayInputStream(byteOut.toByteArray())).use { input ->
                    return this::class.java.cast(input.readObject())
                }
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        return null
    }
}