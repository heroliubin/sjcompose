package com.cnbizmedia.comman.uri

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.cnbizmedia.comman.BuildConfig
import java.io.*

/** URI转路径辅助类 **/
object UriToPathUtils {

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @JvmStatic
    @SuppressLint("ObsoleteSdkInt")
    fun getRealPathFromUri(context: Context, uri: Uri): String? = when {
        Build.VERSION.SDK_INT >= 19 ->
            getRealPathFromUriAboveApi19(context, uri)
        else -> getRealPathFromUriBelowAPI19(context, uri)
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? =
        getDataColumn(context, uri, null, null)

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        when {
            DocumentsContract.isDocumentUri(context, uri) -> {
                // 如果是document类型的 uri, 则通过document id来进行处理
                val documentId: String = DocumentsContract.getDocumentId(uri)
                if (isMediaDocument(uri)) { // MediaProvider
                    val id = documentId.split(":".toRegex()).toTypedArray()[1] // 使用':'分割
                    filePath = getDataColumn(
                        context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "${MediaStore.Images.Media._ID}=?", arrayOf(id)
                    )
                } else if (isDownloadsDocument(uri)) {
                    filePath = getDataColumn(
                        context, ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        documentId.toLong()
                    ), null, null
                    )
                }
            }
            "content".equals(uri.scheme, ignoreCase = true) -> // 如果是 content 类型的 Uri
                filePath = getDataColumn(context, uri, null, null)
            "file" == uri.scheme -> filePath = uri.path // 如果是 file 类型的 Uri,直接获取图片对应的路径
        }
        return filePath
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor =
                context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                path = cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            println("SOMETHING ERROR: $e")
        } finally {
            cursor?.close()
        }
        return path
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private fun isMediaDocument(uri: Uri): Boolean =
        "com.android.providers.media.documents" == uri.authority

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private fun isDownloadsDocument(uri: Uri): Boolean =
        "com.android.providers.downloads.documents" == uri.authority

    /**
     * 获取绝对路径
     * @param context 上下文对象
     * @param contentUri URI对象
     */
    fun getAbsolutePath(context: Context, contentUri: Uri): String? = when {
        Build.VERSION.SDK_INT >= 24 ->
            getFilePathFromURI(context, contentUri)
        else -> getRealPathFromUri(context, contentUri)
    }

    private fun getFilePathFromURI(context: Context, contentUri: Uri): String? {
        val rootDataDir: File = context.filesDir
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile =
                File("${rootDataDir.absolutePath}${File.separator}${fileName}")
            copyFile(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    private fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path: String? = uri.path
        val cut = path?.lastIndexOf('/')
        if (path != null && cut != null && cut != -1)
            fileName = path.substring(cut + 1)
        return System.currentTimeMillis().toString() + fileName
    }

    @SuppressLint("LogNotTimber")
    private fun copyFile(context: Context, srcUri: Uri, dstFile: File) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
            val outputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            if (BuildConfig.DEBUG && !e.message.isNullOrEmpty()) {
                Log.e("UriToPathUtils", e.message!!)
            }
        }
    }

    @Throws(Exception::class, IOException::class)
    private fun copyStream(input: InputStream, output: OutputStream): Int {
        val bufferSize = 1024 * 2
        val buffer = ByteArray(bufferSize)
        val `in` = BufferedInputStream(input, bufferSize)
        val out = BufferedOutputStream(output, bufferSize)
        var count = 0
        var n: Int
        try {
            while (`in`.read(buffer, 0, bufferSize).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (_: IOException) {
            }
            try {
                `in`.close()
            } catch (_: IOException) {
            }
        }
        return count
    }
}