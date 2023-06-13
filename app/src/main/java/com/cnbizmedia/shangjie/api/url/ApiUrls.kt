package com.cnbizmedia.shangjie.api.url

import android.net.Uri
import com.cnbizmedia.shangjie.BuildConfig


/** API请求地址类 **/
object ApiUrls {

    /** 接口的域名地址 **/
    const val BaseUrl: String = "https://api.kanshangjie.com/v_5.0.3/"

    /** 个人中心 - 广告合作链接 **/
    const val AdvertCooperation =
        "https://interactive.zaobao.com/mobile-app-image/zaobao-app-settings/advertise/"

    /** 关于我们 - 早报100年链接 **/
    const val Zaobao100Years = "https://www.zaobao.com/news/singapore/story20221212-1342573"

    /** FAQ - 如何调整字体大小 **/
    const val FaqQuestionFontSizeAdjust = "file:///android_asset/html/faq/font.html"

    /** FAQ - 如何开启深色模式 **/
    const val FaqQuestionDisplaySettings = "file:///android_asset/html/faq/darkmode.html"

}

