package com.cnbizmedia.network.provider

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

internal class AppContextProvider : ContentProvider() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null

        var context: Context?
            private set(value) {
                mContext = value
            }
            get() = mContext

    }

    override fun onCreate(): Boolean {
        mContext = this.context
        return false
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null


}