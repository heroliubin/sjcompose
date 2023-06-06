@file: JvmName("FileDownloader")

package com.cnbizmedia.network.core.manager

import android.annotation.SuppressLint
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.cnbizmedia.network.BuildConfig
import com.cnbizmedia.network.R
import com.cnbizmedia.network.core.exception.handleHttpErrorMessage
import com.cnbizmedia.network.provider.ContextProvider
import com.cnbizmedia.network.service.HttpApiService
import kotlinx.coroutines.*
import okhttp3.*
import okio.*
import retrofit2.Retrofit
import java.io.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/** 文件下载器 **/
class FileDownloader private constructor() {

    companion object {

        /** 创建一个新的实例 **/
        fun newInstance(): FileDownloader = FileDownloader()

    }

    /** 文件下载监听器 **/
    interface OnDownloadListener {

        /** 下载开始 **/
        fun onDownloadStart() {}

        /** 下载进行中, 非主线程运行 **/
        fun onDownloadProgress(total: Long, progress: Long) {}

        /** 下载完成 **/
        fun onDownloadFinish(file: File)

        /** 下载失败 **/
        fun onDownloadFail(errorInfo: String)

    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://www.zaobao.com/")
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(DownloadProgressInterceptor { total, progress ->
                        mDownloadListener?.onDownloadProgress(total, progress)
                    })
                    .retryOnConnectionFailure(false)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build()
            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private var mDownloadJob: Job? = null

    private var mDownloadListener: OnDownloadListener? = null

    fun download(
        url: String,
        destPath: String,
        downloadListener: OnDownloadListener? = null
    ) = download(url, File(destPath), downloadListener)


    fun download(url: String, destFile: File, downloadListener: OnDownloadListener? = null): Job {
        cancelDownloadTask() //取消下载任务
        mDownloadListener = downloadListener //重新赋值监听器
        return CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            try {
                mDownloadListener?.onDownloadStart()
                val isOk = withContext(Dispatchers.IO) {
                    val stream = retrofit.create(HttpApiService::class.java)
                        .downloadFile(url)
                        .byteStream()
                    writeFile(stream, destFile.absolutePath)
                }
                if (isOk)
                    mDownloadListener?.onDownloadFinish(destFile)
                else throw Exception("文件下载时发生错误,请重试~")
                if (BuildConfig.DEBUG) Log.i("Downloader", "下载完成或失败：$isOk, ${destFile.absolutePath}")
            } catch (e: Exception) {
                val err = e.handleHttpErrorMessage()
                var errMessage = err.message
                if (errMessage?.contains("Socket Closed") == true)
                    errMessage = ContextProvider.provideContext()?.getString(R.string.file_download_canceled)
                if (BuildConfig.DEBUG) e.printStackTrace()
                mDownloadListener?.onDownloadFail(errMessage ?: "")
            }
        }.apply { mDownloadJob = this }
    }

    /**
     * 开始下载
     *
     * @param url
     * @param destFile
     */
    @SuppressLint("CheckResult")
    fun download(
        lifecycleScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
        url: String,
        destFile: File,
        downloadListener: OnDownloadListener? = null
    ) = download(lifecycleScope, url, destFile.absolutePath, downloadListener)


    /**
     * 开始下载
     *
     * @param url
     * @param filePath
     */
    @SuppressLint("CheckResult")
    fun download(
        lifecycleScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
        url: String,
        filePath: String,
        downloadListener: OnDownloadListener? = null
    ) {
        cancelDownloadTask() //关闭之前的下载任务
        mDownloadListener = downloadListener //重新赋值监听器
        mDownloadJob = lifecycleScope.launch(Dispatchers.Main) {
            try {
                mDownloadListener?.onDownloadStart()
                val isOk = withContext(Dispatchers.IO) {
                    val stream = retrofit.create(HttpApiService::class.java)
                        .downloadFile(url)
                        .byteStream()
                    writeFile(stream, filePath)
                }
                if (isOk)
                    mDownloadListener?.onDownloadFinish(File(filePath))
                else throw Exception(
                    ContextProvider.provideContext()?.getString(R.string.file_download_error_save)
                )
                if (BuildConfig.DEBUG) Log.i("Downloader", "下载完成")
            } catch (e: Exception) {
                val err = e.handleHttpErrorMessage()
                var errMessage = err.message
                if (errMessage?.contains("Socket Closed") == true)
                    errMessage = ContextProvider.provideContext()?.getString(R.string.file_download_canceled)
                if (BuildConfig.DEBUG) e.printStackTrace()
                mDownloadListener?.onDownloadFail(errMessage ?: "")
            }
        }
    }

    /**
     * 将输入流写入文件
     * @param stream 数据流
     * @param filePath 存储位置
     */
    private fun writeFile(
        stream: InputStream,
        filePath: String
    ): Boolean = try {
        val file = File(filePath)
        file.parentFile?.mkdirs() //创建父目录，防止父目录不存在
        if (file.exists()) file.delete()
        val fos = FileOutputStream(file)
        val b = ByteArray(2048)
        var len: Int = stream.read(b)
        while (len != -1) {
            fos.write(b, 0, len)
            len = stream.read(b)
        }
        fos.flush()
        fos.close()
        stream.close()
        true
    } catch (e: Exception) {
        if (BuildConfig.DEBUG)
            e.printStackTrace()
        false
    }

    /** 取消下载任务 **/
    fun cancelDownloadTask() = apply {
        if (mDownloadJob != null) {
            if (mDownloadJob?.isCancelled != true)
                mDownloadJob?.cancel()
            mDownloadJob = null
        }
        mDownloadListener = null
    }

    private inner class DownloadProgressInterceptor(
        val onProgress: (total: Long, progress: Long) -> Unit
    ) : Interceptor {

        @Throws(Exception::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            return response.newBuilder()
                .body(DownloadResponseBody(response.body, onProgress))
                .build()
        }

    }

    private inner class DownloadResponseBody(
        private val responseBody: ResponseBody?,
        private val onProgress: (total: Long, progress: Long) -> Unit
    ) : ResponseBody() {

        private var bufferedSource: BufferedSource? = null

        override fun contentType(): MediaType? = responseBody?.contentType()

        override fun contentLength(): Long = responseBody?.contentLength() ?: 0

        override fun source(): BufferedSource {
            if (bufferedSource == null && responseBody != null)
                bufferedSource = source(responseBody.source()).buffer()
            return bufferedSource!!
        }

        private fun source(source: Source): Source {
            val totalLength = contentLength()
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L

                @Throws(IOException::class)
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    if (bytesRead == -1L) {
                        totalBytesRead = totalLength
                    } else {
                        totalBytesRead += bytesRead
                        if (BuildConfig.DEBUG)
                            println("下载进度值：$totalBytesRead = $totalLength")
                        onProgress(totalLength, totalBytesRead)
                    }
                    return bytesRead
                }
            }
        }
    }

}