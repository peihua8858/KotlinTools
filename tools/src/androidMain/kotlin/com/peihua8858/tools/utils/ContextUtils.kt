@file:JvmName("ContextUtils")
@file:JvmMultifileClass

package com.peihua8858.tools.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.peihua8858.tools.ContextInitializer
import com.peihua8858.tools.file.deleteFileOrDir
import com.peihua8858.tools.file.formatSize
import com.peihua8858.tools.file.getFileSize
import com.peihua8858.tools.file.mimeTypeFromFilePath
import java.io.File
import java.util.Locale


/**
 * 网络链接管理器
 * @author dingpeihua
 * @date 2021/9/28 16:24
 * @version 1.0
 */
val Context?.connectivityManager: ConnectivityManager?
    get() = this?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

/**
 * 剪贴板管理器
 * @author dingpeihua
 * @date 2021/9/28 16:40
 * @version 1.0
 */
val Context?.clipboardManager: ClipboardManager?
    get() = this?.applicationContext?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

/**
 * 下载管理器
 * @author dingpeihua
 * @date 2021/9/28 16:41
 * @version 1.0
 */
val Context?.downloadManager: DownloadManager?
    get() = this?.applicationContext?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager

/**
 * 电话管理器
 * @author dingpeihua
 * @date 2021/9/28 16:41
 * @version 1.0
 */
val Context?.telephonyManager: TelephonyManager?
    get() = this?.applicationContext?.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

/**
 * 输入管理器
 * @author dingpeihua
 * @date 2021/9/28 16:41
 * @version 1.0
 */
val Context?.inputMethodManager: InputMethodManager?
    get() = this?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

/**
 *位置管理器
 * @author dingpeihua
 * @date 2021/9/28 16:54
 * @version 1.0
 */
val Context?.locationManager: LocationManager?
    get() = this?.applicationContext?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

/**
 *显示管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.displayManager: DisplayManager?
    get() = this?.applicationContext?.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager

/**
 *相机管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.cameraManager: CameraManager?
    get() = this?.applicationContext?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

/**
 *窗口管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.windowManager: WindowManager?
    get() = this?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager

/**
 * 通知管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.notificationManager: NotificationManager?
    get() = this?.applicationContext?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

/**
 * 活动管理器
 * @author dingpeihua
 * @date 2021/9/28 16:56
 * @version 1.0
 */
val Context?.activityManager: ActivityManager?
    get() = this?.applicationContext?.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

/**
 * 警报管理器
 * @author dingpeihua
 * @date 2021/9/28 16:56
 * @version 1.0
 */
val Context?.alarmManager: AlarmManager?
    get() = this?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

fun Context.getDisplayMetrics(): DisplayMetrics {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getRealMetrics(outMetrics)
    return outMetrics
}

/**
 * 判断通知是否启用
 *
 * @param context
 * @author dingpeihua
 * @date 2019/12/17 16:30
 * @version 1.0
 */
fun Any?.isEnabledNotification(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

/**
 * 判断通知是否启用
 *
 * @author dingpeihua
 * @date 2019/12/17 16:30
 * @version 1.0
 */
fun Context?.isEnabledNotification(): Boolean {
    return this != null && isEnabledNotification(this)
}

/**
 * 跳转到通知设置页面
 * 需要有回调
 */
fun Activity.startNotificationSettingsForResult(requestCode: Int) {
    startActivityForResult(buildIntent(this), requestCode)
}

/**
 * 跳转到通知设置页面
 * 需要有回调
 */
fun Any?.startNotificationSettingsForResult(context: Activity, requestCode: Int) {
    context.startActivityForResult(buildIntent(context), requestCode)
}

/**
 * 跳转到通知设置页面
 *
 * @author dingpeihua
 * @date 2019/12/17 16:27
 * @version 1.0
 */
fun Context.startNotificationSettings() {
    startActivity(buildIntent(this))
}

/**
 * 跳转到通知设置页面
 *
 * @param context
 * @author dingpeihua
 * @date 2019/12/17 16:27
 * @version 1.0
 */
fun Any?.startNotificationSettings(context: Context) {
    context.startActivity(buildIntent(context))
}

private fun buildIntent(context: Context): Intent {
    val packageName = context.packageName
    val intent = Intent()
    val uid = context.applicationInfo.uid
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid)
    } else {
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", uid)
    }
    // < 4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
    intent.data = Uri.fromParts("package", context.packageName, null)
    return intent
}

/**
 * 判断当前用户系统使用的语言为阿拉伯语，true表示为阿拉伯语，false表示其它语言
 *
 * @return
 */
fun Locale?.isUsingArLanguage(): Boolean {
    try {
        val language = this?.language
        return language.isNonEmpty() && language.equals("ar", ignoreCase = true)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 判断当前用户系统使用的语言为阿拉伯语，true表示为阿拉伯语，false表示其它语言
 *
 * @return
 */
fun Any?.isUsingArLanguage(): Boolean {
    try {
        val context = ContextInitializer.context
        return context.isUsingArLanguage()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 判断当前用户系统使用的语言为阿拉伯语，true表示为阿拉伯语，false表示其它语言
 *
 * @return
 */
fun Context.isUsingArLanguage(): Boolean {
    try {
        val locale: Locale? = if (isAtLeastN) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }
        return locale.isUsingArLanguage()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun Context?.getColorCompat(@ColorRes resId: Int): Int {
    val ctx = this ?: ContextInitializer.context
    return ctx.let {
        ContextCompat.getColor(it, resId)
    } ?: -1
}

fun Context?.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    val ctx = this ?: ContextInitializer.mContext
    return ctx.let {
        ContextCompat.getDrawable(it, resId)
    }
}

fun Context?.getDimensionPixelOffset(@DimenRes resId: Int): Int {
    val ctx = this ?: ContextInitializer.mContext
    return ctx.resources?.getDimensionPixelOffset(resId) ?: 0
}

fun Context.clearCacheFile() {
    getDiskCacheDir().deleteFileOrDir()
}

fun Context.getCacheSize(): Long {
    return getDiskCacheDir().getFileSize()
}

fun Context.getCacheFormatSize(): String {
    return getDiskCacheDir().formatSize()
}

fun Context.getDiskCacheDir(): File? {
    //如果SD卡存在通过getExternalCacheDir()获取路径，
    //放在路径 /sdcard/Android/data/<application package>/cache/
    val file = externalCacheDir
    //如果SD卡不存在通过getCacheDir()获取路径，
    //放在路径 /data/data/<application package>/cache/
    if (file != null && file.exists()) {
        return file
    }
    return cacheDir
}

val Context.isLandScape: Boolean
    get() {
        val isLandScape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Log.d("isLandScape", isLandScape.toString())
        Log.d("isLandScape", resources.configuration.orientation.toString())
        return isLandScape
    }


fun Context.getDimen(id: Int) = resources.getDimension(id)
fun Context.getDimenPixel(id: Int) = resources.getDimensionPixelSize(id)
fun Context.getDimenPixelOffset(id: Int) = resources.getDimensionPixelOffset(id)
fun Context.getDimenPixelSize(id: Int) = resources.getDimensionPixelSize(id)
fun Context.getStringArray(id: Int) = resources.getStringArray(id)

fun Context.dimenOffset(dip: Int): Int {
    return resources.getDimensionPixelOffset(dip)
}

fun View.dimenOffset(dip: Int): Int {
    return resources.getDimensionPixelSize(dip)
}

fun Context.registerReceiverCompat(
    receiver: BroadcastReceiver,
    filter: IntentFilter,
): Intent? {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, filter)
    } else {
        registerReceiver(
            receiver,
            filter,
            Context.RECEIVER_NOT_EXPORTED
        )

    }
}

fun Context.finish() {
    (this as? Activity)?.finish()
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels
val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels


object ContextExt {

    @JvmStatic
    fun Context.isLandscape(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}

val Context.isLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context?.isFinishing: Boolean
    get() = this?.let {
        when (it) {
            is Activity -> it.isFinishing
            is ContextWrapper -> it.baseContext.isFinishing
            else -> false
        }
    } == true

val Context?.isResumed: Boolean
    get() = this?.let {
        when (it) {
            is Activity -> it.isResumed
            is ContextWrapper -> it.baseContext.isResumed
            else -> false
        }
    } == true

fun Context.installApk(apkPath: String) {
    installApk(File(apkPath))
}

fun Context.installApk(apkFile: File) {
    val mediaType = apkFile.mimeTypeFromFilePath
    installApk(apkFile.fileProvider, mediaType)
}

fun Context.installApk(uri: Uri?) {
    if (uri == null) {
        dLog { "installApk>>>>>>uri is null" }
        return
    }
    val mediaType = uri.mimeTypeFromFilePath
    installApk(uri, mediaType)
}

fun Context.installApk(uri: Uri, mediaType: String?) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (Build.VERSION.SDK_INT < 29) {
            intent.setDataAndType(uri, mediaType)
        } else {
            intent.setDataAndType(uri, mediaType)
        }
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.installLocalApk(uri: Uri?) {
    val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    if (Build.VERSION.SDK_INT < 30) {
        intent.setData(uri)
        startActivity(intent)
    } else {
        val file = getFileFromUri(uri) ?: return
        intent.setData(file.fileProvider)
        startActivity(intent)
    }
}


fun Context.startStorageSettingsActivity() {
    if (isAtLeastR) {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.setData(("package:$packageName").toUri())
        try {
            startActivity(intent)
        } catch (e: Exception) {
            dLog { "fail e:${e.stackTraceToString()}" }
            try {
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(intent)
            } catch (e: Exception) {
                dLog { "fail e:${e.stackTraceToString()}" }
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                startActivity(intent)
            }
        }
    } else if (isAtLeastN) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(("package:$packageName").toUri())
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS))
    }
}

fun Context.shareCertainFiles(filePath: String, title: String = "") {
    shareCertainFiles(File(filePath), title)
}

fun Context.shareCertainFiles(file: File, title: String = "") {
    shareCertainFiles(file.fileProvider, title)
}

fun Context.shareCertainFiles(uri: Uri, title: String = "") {
    shareCertainFiles(arrayListOf(uri), title)
}

fun Context.shareCertainFiles(uris: MutableList<Uri>, title: String) {
    if (uris.isEmpty()) return
    val intent = Intent()
    //intent.setType("application/vnd.android.package-archive");
    intent.setType("application/x-zip-compressed")
    if (uris.size > 1) {
        intent.setAction(Intent.ACTION_SEND_MULTIPLE)
        intent.putExtra(Intent.EXTRA_STREAM, ArrayList<Uri>(uris))
    } else {
        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uris[0])
    }
    val tempTitle = title.ifEmpty { "Share" }
    intent.putExtra(Intent.EXTRA_SUBJECT, tempTitle)
    intent.putExtra(Intent.EXTRA_TEXT, tempTitle)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        val chooser = Intent.createChooser(intent, "Share File")
        val resInfoList =
            this.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            uris.forEach {
                this.grantUriPermission(
                    packageName,
                    it,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
        chooser.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(chooser)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun Context.startAccessibilitySettings() {
    // 引导用户到系统辅助功能设置
    try {
        toAccessibilitySettingActivity("android.settings.ACCESSIBILITY_DETAILS_SETTINGS")
    } catch (e: Throwable) {
        dLog { "MainScreen>>>>>>>error:${e.stackTraceToString()}" }
        try {
            toAccessibilitySettingActivity(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        } catch (e: Exception) {
            dLog { e.stackTraceToString() }
        }
    }
}

fun Context.toAccessibilitySettingActivity(action: String) {
    try {
        val intent = Intent(action)
        intent.setData("package:${packageName}".toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Throwable) {
        dLog { "MainScreen>>>>>>>error:${e.stackTraceToString()}" }
        val intent = Intent(action)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}


val Context.isAccessibilityServiceEnabled: Boolean
    get() {
        return Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED,
            0
        ) == 1
    }

fun Context.openWithFile(filePath: String) {
    val uriForFile = filePath.fileProvider
    val intent = Intent()
    intent.setAction("android.intent.action.VIEW")
    val mimeType = filePath.mimeTypeFromFilePath
    intent.setDataAndType(uriForFile, mimeType ?: "*/*")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(Intent.createChooser(intent, "Open with"))
    dLog { "openWithFile mimeType:$mimeType, file:${filePath}" }
}



