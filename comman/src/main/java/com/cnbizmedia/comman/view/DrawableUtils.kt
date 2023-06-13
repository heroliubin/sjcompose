package com.cnbizmedia.comman.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.cnbizmedia.comman.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream



/** Drawable转换成InputStream **/
fun Drawable.drawable2InputStream() = drawable2Bitmap().bitmap2InputStream()

/** 将Bitmap转换成InputStream **/
fun Bitmap.bitmap2InputStream(): InputStream {
    val baos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, baos)
    return ByteArrayInputStream(baos.toByteArray())
}

/** Drawable转换成Bitmap **/
fun Drawable.drawable2Bitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        if (opacity != PixelFormat.OPAQUE)
            Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return bitmap
}