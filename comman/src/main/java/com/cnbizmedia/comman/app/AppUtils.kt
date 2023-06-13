@file: JvmName("AppUtils")

package com.cnbizmedia.comman.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.os.Process
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.WindowManager
import android.webkit.MimeTypeMap
import androidx.annotation.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.hardware.display.DisplayManagerCompat
import com.cnbizmedia.comman.BuildConfig
import com.cnbizmedia.comman.R
import com.cnbizmedia.comman.view.getPxDimensionSizeById
import com.cnbizmedia.comman.view.showToast
import java.io.File
import kotlin.system.exitProcess


/** 判断应用进程是否存在 **/
fun Context.isAppProcessesRunningTaskExist(processName: String): Boolean {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .runningAppProcesses.any { it.processName == processName }
}

/** 获取判断应用是否是暗黑模式 **/
val Context.isUIDarkMode: Boolean
    get() {
        val mode = AppCompatDelegate.getDefaultNightMode()
        return if (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            val applicationUiMode = resources.configuration.uiMode
            val systemMode = applicationUiMode and Configuration.UI_MODE_NIGHT_MASK
            systemMode == Configuration.UI_MODE_NIGHT_YES
        } else mode == AppCompatDelegate.MODE_NIGHT_YES
    }

/**
 * 获取资源Id
 * @param name 资源名称
 * @param defType 资源类型，例如：mipmap, drawable ...
 * @param defPackage 资源所在包名
 */
@SuppressLint("DiscouragedApi")
fun Context.getResourceId(name: String?, defType: String?, defPackage: String?) =
    resources.getIdentifier(name, defType, defPackage)

/**
 * 通过ID获取颜色值
 * @param id 颜色资源ID
 */
@ColorInt
fun Context.getColorById(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

/** 获取[android.R.attr.selectableItemBackground]对应的Drawable资源 **/
fun Context.getSelectableItemBackground(): Drawable? {
    val drawable: Drawable?
    val tv = TypedValue()
    theme.resolveAttribute(android.R.attr.selectableItemBackground, tv, true)
    val ta = theme.obtainStyledAttributes(
        tv.resourceId,
        intArrayOf(android.R.attr.selectableItemBackground)
    )
    drawable = ta.getDrawable(0)
    ta.recycle()
    return drawable
}

/** 获取[android.R.attr.selectableItemBackgroundBorderless]对应的Drawable资源 **/
fun Context.getSelectableItemBackgroundBorderless(): Drawable? {
    val drawable: Drawable?
    val tv = TypedValue()
    theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, tv, true)
    val ta = theme.obtainStyledAttributes(
        tv.resourceId,
        intArrayOf(android.R.attr.selectableItemBackgroundBorderless)
    )
    drawable = ta.getDrawable(0)
    ta.recycle()
    return drawable
}

/**
 * 通过ID获取ColorState对象
 * @param id 资源ID
 */
@Suppress("DEPRECATION")
@SuppressLint("NewApi", "UseCompatLoadingForColorStateLists")
fun Context.getColorStateListById(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)

/**
 * 获取图片资源
 *
 * @param drawableId 图片资源ID
 * @return
 */
@SuppressLint("UseCompatLoadingForDrawables")
@Suppress("DEPRECATION")
fun Context.getDrawableById(@DrawableRes drawableId: Int): Drawable {
    return ContextCompat.getDrawable(this, drawableId)?.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    } as Drawable
}


/** 获取屏幕尺寸大小 **/
@Suppress("DEPRECATION")
fun Context.getScreenSize(): DisplayMetrics {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        wm.defaultDisplay.getMetrics(outMetrics)
    else {
        val (width, height) =
            wm.currentWindowMetrics.bounds.run { (right - left) to (bottom - top) }
        outMetrics.widthPixels = width
        outMetrics.heightPixels = height
    }
    return outMetrics
}

/** 获取屏幕真实尺寸大小 **/
@Suppress("DEPRECATION")
fun Context.getScreenRealSize(): DisplayMetrics {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        wm.defaultDisplay.getRealMetrics(outMetrics)
    else DisplayManagerCompat.getInstance(this)
        .getDisplay(Display.DEFAULT_DISPLAY)
        ?.getRealMetrics(outMetrics)
    return outMetrics
}

/** 获取屏幕宽度 **/
val Context.screenWidth get() = getScreenSize().widthPixels

/** 获取屏幕高度 **/
val Context.screenHeight get() = getScreenSize().heightPixels

/** 获取真实屏幕宽度 **/
val Context.screenRealWidth get() = getScreenRealSize().widthPixels

/** 获取真实屏幕高度 **/
val Context.screenRealHeight get() = getScreenRealSize().heightPixels

/** 获取状态栏高度 **/
val Context.statusBarHeight: Int
    @SuppressLint("PrivateApi", "LogNotTimber")
    get() {
        val defaultHeightValue = 75
        try {
            return getPxDimensionSizeById(
                getResourceId(
                    "status_bar_height",
                    "dimen",
                    "android"
                )
            )
        } catch (e: Exception) {
            if (BuildConfig.DEBUG)
                Log.e("AppUtils", e.message ?: "")
        }
        return defaultHeightValue
    }

/** 获取ActionBar高度 **/
val Context.actionBarHeight: Int
    get() = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.abc_action_bar_default_height_material)

/** 获取底部导航栏高度 **/
val Context.bottomNavigationBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() = resources.getDimensionPixelSize(
        resources.getIdentifier("navigation_bar_height", "dimen", "android")
    )

/**
 * 获取文件的Uri地址
 *
 * @param file 源文件
 * @return
 */
fun Context.getFileUri(file: File): Uri = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
        FileProvider.getUriForFile(this, "$packageName.FileProvider", file)
    else -> Uri.fromFile(file)
}


/** 获取判断系统的通知信息是否可用 **/
val Context.areNotificationsEnabled: Boolean
    get() = NotificationManagerCompat.from(this).areNotificationsEnabled()

/** 打开应用的通知设置页面 **/
fun Context.openNotificationSettings() {
    startActivity(Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> { // android 8.0引导
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("android.provider.extra.APP_PACKAGE", packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> { // android 5.0-7.0
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", packageName)
                putExtra("app_uid", applicationInfo.uid)
            }
            else -> { //其它
                action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                data = Uri.fromParts("package", packageName, null)
            }
        }
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
    if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/** 打开应用的通知设置页面 **/
fun Activity.openNotificationSettings(requestCode: Int) {
    startActivityForResult(Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> { // android 8.0引导
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("android.provider.extra.APP_PACKAGE", packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> { // android 5.0-7.0
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", packageName)
                putExtra("app_uid", applicationInfo.uid)
            }
            else ->//其它
                data = Uri.fromParts("package", packageName, null)
        }
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }, requestCode)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 同步刷新设备资源
 *
 * @param fileName  文件名
 * @return
 */
fun Context.syncDeviceResources(fileName: String, onCompleted: () -> Unit, onError: () -> Unit) =
    syncDeviceResources(File(fileName), onCompleted, onError)

/**
 * 同步刷新设备资源
 * @param file    文件对象
 * @param onCompleted 完成下载
 * @param onError 发生错误
 */
@SuppressLint("LogNotTimber")
fun Context.syncDeviceResources(file: File, onCompleted: () -> Unit, onError: () -> Unit) {
    val fileMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Thread {
            try {
                val uri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
                        put(MediaStore.MediaColumns.MIME_TYPE, fileMimeType)
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH, when {
                            fileMimeType?.lowercase()?.startsWith("image") == true ->
                                Environment.DIRECTORY_DCIM
                            else -> Environment.DIRECTORY_DOWNLOADS
                        }
                        )
                    }) ?: throw Exception()
                val os = contentResolver.openOutputStream(uri) ?: throw Exception()
                val fis = file.inputStream()
                FileUtils.copy(fis, os)
                fis.close()
                os.close()
                onCompleted.invoke()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG)
                    Log.e("AppUtils", e.message ?: "")
                onError.invoke()
            }
        }.start()
        else -> MediaScannerConnection.scanFile(this, arrayOf(file.absolutePath), null, null)
    }
}

/**
 * 检查手机上是否安装了指定的软件
 *
 * @param packageName 应用包名
 * @return
 */
@SuppressLint("LogNotTimber")
@Suppress("DEPRECATION")
fun Context.isSoftwareInstalled(packageName: String): Boolean = try {
    packageManager.getPackageInfo(packageName, 0)
    true
} catch (e: Exception) {
    if (BuildConfig.DEBUG)
        Log.e("AppUtils", e.message ?: "")
    false
}


/**
 * 执行Apk应用程序安装
 *
 * @param filePath    文件路径
 * @param requestCode 请求码
 * @param fileProviderAuthor 文件提供者
 */
fun Activity.installApk(
    filePath: String,
    requestCode: Int,
    fileProviderAuthor: String = "$packageName.FileProvider"
) = with(Intent(Intent.ACTION_VIEW)) {
    val targetFile = File(filePath)
    val isSDKIntThanN = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    val uri: Uri
    if (isSDKIntThanN) {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        uri = FileProvider.getUriForFile(
            this@installApk,
            fileProviderAuthor,
            targetFile
        )
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    } else {
        uri = Uri.fromFile(targetFile)
    }
    putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
    setDataAndType(uri, "application/vnd.android.package-archive")
    startActivityForResult(this, requestCode)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 执行Apk应用程序卸载
 *
 * @param packageName 应用包名
 */
fun Context.uninstallApk(packageName: String) =
    startActivity(Intent(Intent.ACTION_DELETE, Uri.parse("package:$packageName")))

/**
 * 获取进程名称
 *
 * @param pid 进程的PID
 */
fun Context.getProcessName(pid: Int): String {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val processes = am.runningAppProcesses
    if (processes != null && processes.size > 0) {
        for (processInfo in processes) {
            if (processInfo.pid == pid)
                return processInfo.processName
        }
    }
    return ""
}

/** 启动拍照 **/
fun Activity.takePicture(outputFile: File, reqCode: Int) {
    with(Intent(MediaStore.ACTION_IMAGE_CAPTURE)) {
        putExtra(MediaStore.EXTRA_OUTPUT, getFileUri(outputFile))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            putExtra(MediaStore.Images.ImageColumns.ORIENTATION, 0)
        startActivityForResult(this, reqCode)
    }
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 获取文件
 * @param mediaType 文件的类型，例如图片：image|*
 * @param requestCode 请求码
 */
fun Activity.pickFile(mediaType: String, requestCode: Int) {
    startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply {
        type = mediaType
        addCategory(Intent.CATEGORY_OPENABLE)
    }, requestCode)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 图片裁剪
 * @param imgFileUri 图片文件URI
 * @param aspectX 裁剪比例X
 * @param aspectY 裁剪比例Y
 * @param outputW 输出大小W
 * @param outputH 输出大小H
 * @param requestCode 请求码
 */
fun Activity.cropPicture(
    imgFileUri: Uri,
    aspectX: Int = 1,
    aspectY: Int = 1,
    outputW: Int = 150,
    outputH: Int = 150,
    outputFormat: String = Bitmap.CompressFormat.JPEG.toString(),
    requestCode: Int,
) {
    startActivityForResult(Intent("com.android.camera.action.CROP").apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        setDataAndType(imgFileUri, "image/*")
        putExtra("crop", "true")
        putExtra("aspectX", aspectX)
        putExtra("aspectY", aspectY)
        putExtra("outputX", outputW)
        putExtra("outputY", outputH)
        putExtra("noFaceDetection", true)
        putExtra("outputFormat", outputFormat)
        putExtra("return-data", true)
    }, requestCode)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}


/**
 * 拨打电话
 *
 * @param phoneNumber 电话号码
 */
@SuppressLint("MissingPermission")
fun Context.callPhone(phoneNumber: String) {
    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")))
    if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 跳转到拨打电话的界面
 *
 * @param phoneNumber 电话号码
 */
fun Context.showCallDial(phoneNumber: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
    if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 打开手机浏览器
 *
 * @param url     网页地址
 */
fun Context.openSelfWebBrowser(url: String) {
    with(Intent()) {
        action = "android.intent.action.VIEW"
        data = Uri.parse(url)
        startActivity(this)
    }
    if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/** 打开网络设置页面 **/
fun Context.startNetworkSettingsPage() {
    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
    if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/** 打开应用详情页面 **/
fun Context.startAppDetailInfoPage() {
    startActivity(Intent("android.settings.APPLICATION_DETAILS_SETTINGS").apply {
        data = Uri.fromParts("package", packageName, null)
    })
    if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/** 启动应用安装设置页面 **/
@RequiresApi(Build.VERSION_CODES.O)
fun Activity.startAppInstallSettingsPage(requestCode: Int) {
    startActivityForResult(
        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            .setData(Uri.parse("package:$packageName")),
        requestCode
    )
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 启动视频录制页面
 *
 * @param videoQuality  视频质量，取值： 0 ， 1
 * @param durationLimit 视频录制时间限制
 * @param fileSizeLimit 文件大小最高限制
 * @param reqCode       请求码
 */
fun Activity.startVideoCapturePage(
    videoQuality: Int,
    durationLimit: Long,
    fileSizeLimit: Long,
    reqCode: Int,
) {
    with(Intent(MediaStore.ACTION_VIDEO_CAPTURE)) {
        putExtra(MediaStore.EXTRA_VIDEO_QUALITY, videoQuality)
        //限制时长
        putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit)
        //限制大小
        putExtra(MediaStore.EXTRA_SIZE_LIMIT, fileSizeLimit);
        startActivityForResult(this, reqCode)
    }
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**
 * 启动电子邮件发送页面
 * @param body 电子邮件内容
 * @param subject 电子邮件主题
 * @param mailTo 发送给谁，多个电邮用","分割
 */
fun Context.startEmailSendPage(mailTo: String?, body: String? = null, subject: String? = null) {
    try {
        startActivity(
            Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_TEXT, body ?: "")
                .putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
                .putExtra(
                    Intent.EXTRA_EMAIL,
                    mailTo?.run { if (contains(",")) split(",").toTypedArray() else arrayOf(this) })
        )
        if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    } catch (ex: ActivityNotFoundException) {
        showToast(R.string.common_email_app_not_install)
    }
}

/**
 * 打开软件应用市场
 * @param packageName 包名
 */
@SuppressLint("QueryPermissionsNeeded", "LogNotTimber")
fun Context.startApplicationMarket(packageName: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("market://details?id=$packageName")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            true
        } else throw Exception("CANNOT FIND APPLICATION MARKET")
    } catch (e: Exception) {
        if (BuildConfig.DEBUG)
            Log.e("AppUtils", e.message ?: "")
        showToast(R.string.common_market_app_not_install)
        false
    }
}

/** 返回到手机桌面 **/
fun Context.backToPhoneDesktop() {
    startActivity(Intent(Intent.ACTION_MAIN).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        addCategory(Intent.CATEGORY_HOME)
    })
    if (this is Activity) overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

/**  退出应用程序 **/
@SuppressLint("LogNotTimber")
fun exitApplication() {
    try {
        Process.killProcess(Process.myPid())
        Process.killProcess(Process.myTid())
        Process.killProcess(Process.myUid())
        exitProcess(0)//退出JVM
    } catch (e: Exception) {
        if (BuildConfig.DEBUG)
            Log.e("AppUtils", e.message ?: "")
    }
}