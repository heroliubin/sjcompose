package com.cnbizmedia.shangjie.app

import android.app.Application
import com.cnbizmedia.comman.app.ConfigUtils
import com.cnbizmedia.comman.app.LogUtils

class SjApplication:Application() {
    companion object{
        lateinit var instance: SjApplication
    }


    override fun onCreate() {
        super.onCreate()
        instance=this
        ConfigUtils.initialize(this)
    }
}