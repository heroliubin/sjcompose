package com.cnbizmedia.comman.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

/** 分享功能辅助类 **/
object ShareUtils {

    /**
     * 分享文本
     * @param context 上下文对象
     * @param title 标题
     * @param text 文本
     */
    @JvmStatic
    fun shareText(
        context: Context,
        title: String,
        text: String
    ) = context.startActivity(
        Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, text)
            .run { Intent.createChooser(this, title) }
    )

    /**
     * 分享图片
     * @param context 上下文对象
     * @param title 标题
     * @param imageUri 图片的URI
     * @param extraText 附带的说明文字
     */
    @JvmStatic
    @JvmOverloads
    fun shareImage(
        context: Context,
        title: String,
        imageUri: Uri,
        extraText: String? = null,
    ) = context.startActivity(
        Intent(Intent.ACTION_SEND)
            .setType("image/*")
            .putExtra(Intent.EXTRA_STREAM, imageUri)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                if (!extraText.isNullOrEmpty())
                    putExtra(Intent.EXTRA_TEXT, extraText)
            }
            .run { Intent.createChooser(this, title) }
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
    )

    /**
     * 分享多张图片
     * @param context 上下文对象
     * @param title 标题
     * @param imageUris 图片的URI集合
     */
    @JvmStatic
    fun shareImages(
        context: Context,
        title: String,
        imageUris: ArrayList<Uri>
    ) = context.startActivity(
        Intent(Intent.ACTION_SEND_MULTIPLE)
            .setType("image/*")
            .putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            .run { Intent.createChooser(this, title) }
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
    )

}