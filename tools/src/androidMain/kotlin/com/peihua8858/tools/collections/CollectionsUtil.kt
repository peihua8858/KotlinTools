package com.peihua8858.tools.collections

import android.os.Parcel
import android.os.Parcelable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


/**
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <T : Parcelable> Set<T>.deepCloneParcelableSet(): Set<T>? {
    return try {
        val tClass = this.iterator().next().javaClass
        val source = Parcel.obtain()
        val aa = arrayListOf<T>()
        for (v in this) {
            aa.add(v)
        }
        source.writeTypedArray(aa.toArray(arrayOfNulls(size)), 0)
        val bytes: ByteArray = source.marshall()
        source.recycle()
        val newSource = Parcel.obtain()
        newSource.unmarshall(bytes, 0, bytes.size)
        newSource.setDataPosition(0)
        val name = tClass.toString()
        val data = mutableSetOf<T>()
        val f = tClass.getField("CREATOR")
        val creator: Parcelable.Creator<T> = f[null] as Parcelable.Creator<T>?
            ?: throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
        val a: Array<T>? = newSource.createTypedArray(creator)
        if (!a.isNullOrEmpty()) {
            for (v in a) {
                data.add(v)
            }
        }
        newSource.recycle()
        data
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <T : Serializable> Collection<T>.deepClone(): Collection<T>? {
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
fun <T : Parcelable> List<T>.deepCloneParcelableList(): List<T>? {
    return try {
        val tClass = this[0].javaClass
        val source = Parcel.obtain()
        source.writeTypedList(this)
        val bytes: ByteArray = source.marshall()
        source.recycle()
        val newSource = Parcel.obtain()
        newSource.unmarshall(bytes, 0, bytes.size)
        newSource.setDataPosition(0)
        val name = tClass.toString()
        val data = arrayListOf<T>()
        val f = tClass.getField("CREATOR")
        val creator: Parcelable.Creator<T> = f[null] as Parcelable.Creator<T>?
            ?: throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
        newSource.readTypedList(data, creator)
        newSource.recycle()
        data
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}
