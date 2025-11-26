package com.peihua8858.tools.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T> Intent?.getParcelableExtraCompat(name: String?): T? {
    return getParcelableExtraCompat(name, T::class.java)
}

fun <T> Intent?.getParcelableExtraCompat(name: String?, clazz: Class<T>): T? {
    if (this == null) return null
    return if (isTiramisu) {
        getParcelableExtra(name, clazz)
    } else {
        getParcelableExtra(name)
    }
}

inline fun <reified T : Parcelable> Intent?.getParcelableArrayListExtraCompat(name: String?): ArrayList<T> {
    return getParcelableArrayListExtraCompat(name, T::class.java)
}

fun <T : Parcelable> Intent?.getParcelableArrayListExtraCompat(name: String?, clazz: Class<T>): ArrayList<T> {
    if (this == null) return arrayListOf()
    return if (isTiramisu) {
        getParcelableArrayListExtra(name, clazz) ?: arrayListOf()
    } else {
        getParcelableArrayListExtra(name) ?: arrayListOf()
    }
}

inline fun <reified T> Bundle?.getParcelableCompat(name: String?): T? {
    return getParcelableCompat(name, T::class.java)
}

fun <T> Bundle?.getParcelableCompat(name: String?, clazz: Class<T>): T? {
    if (this == null) return null
    return if (isTiramisu) {
        getParcelable(name, clazz)
    } else {
        getParcelable(name)
    }
}

inline fun <reified T : Parcelable> Bundle?.getParcelableArrayListCompat(name: String?): ArrayList<T> {
    return getParcelableArrayListCompat(name, T::class.java)
}

fun <T : Parcelable> Bundle?.getParcelableArrayListCompat(name: String?, clazz: Class<T>): ArrayList<T> {
    if (this == null) return arrayListOf()
    return if (isTiramisu) {
        getParcelableArrayList(name, clazz) ?: arrayListOf()
    } else {
        getParcelableArrayList(name) ?: arrayListOf()
    }
}


fun Context.registerReceiverExported(receiver: BroadcastReceiver, intent: IntentFilter) {
    if (isTiramisu) {
        registerReceiver(receiver, intent, Context.RECEIVER_EXPORTED)
    } else registerReceiver(receiver, intent)
}

fun Context.registerReceiverNotExported(receiver: BroadcastReceiver, intent: IntentFilter) {
    if (isTiramisu) {
        registerReceiver(receiver, intent, Context.RECEIVER_NOT_EXPORTED)
    } else registerReceiver(receiver, intent)
}