@file:JvmName("ArrayUtil")
@file:JvmMultifileClass

package com.peihua8858.tools.array

import android.os.Parcel
import android.os.Parcelable
import java.io.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

/**
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <T : Parcelable> Array<T>.deepCloneParcelableArray(): Array<T>? {
    return try {
        val tClass = this[0].javaClass
        val source = Parcel.obtain()
        source.writeTypedArray(this, 0)
        val bytes: ByteArray = source.marshall()
        source.recycle()
        val newSource = Parcel.obtain()
        newSource.unmarshall(bytes, 0, bytes.size)
        newSource.setDataPosition(0)
        val name = tClass.toString()
        val f = tClass.getField("CREATOR")
        val creator: Parcelable.Creator<T> = f[null] as Parcelable.Creator<T>?
            ?: throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
        val data = newSource.createTypedArray(creator)
        newSource.recycle()
        data
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}
