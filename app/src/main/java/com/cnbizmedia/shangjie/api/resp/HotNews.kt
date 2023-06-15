package com.cnbizmedia.shangjie.api.resp

data class HotNews(
    var id: String? = null,
    var title: String? = null,
    var thumb: String? = null,
    var url: String? = null,
    var label_id: String? = null,
    var inputtime: String? = null,
    var catid: String? = null,
    var label: String? = null,
    var shareurl: String? = null,
    var share_content: String? = null,
    var sharepic: String? = null,
    var is_favorite: String? = null
)