package com.cnbizmedia.shangjie.api.resp

data class ADData(
    var app: ADAPP?,
    var alist: List<ADlist?>?
)

data class ADAPP(
    var url: String? ,
    var content: String? ,
    var version: String? ,
    var news: String? ,
    var theme: Int = 0
)


data class ADlist(
    var name: String?=null ,
    var type: String?=null,
    var typeval: String?=null,
    var endDate: String?=null,
    var addDate: String?=null,
    var value: String?=null,
    var description: String?=null,
    var platform: String?=null,
    var id: String?=null,
    var device: String?=null,
    var adlink: String?=null,
    var timeflag: String?=null,
    var jumptime: String?=null,
    var position: String?=null,
    var is_share: String?=null,
    var catid: String?=null,
    var url: String?=null,
    var jump_id: String?=null
)



