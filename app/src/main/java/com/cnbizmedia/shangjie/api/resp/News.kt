package com.cnbizmedia.shangjie.api.resp

import com.google.gson.JsonArray


class SjNews(var content: List<SjNewItem>?, var top: List<SjNewItem>?)
data class SjNewItem(
    var id: String? = null,

    var catid: String? = null,

    var title: String? = null,
    var description: String? = null,
    var author: String? = null,
    var thumb: String? = null,

    var inputtime: String? = null,

    /**
     * news=普通文章,picture =图集
     */
    var type: String? = null,

    var comment: Int = 0,

    var url: String? = null,

    var favid: String? = null,
    var slide: List<NewsItem>? = null,
    var is_origin: String? = null,
    var mag_id: String? = null,
    var mag_name: String? = null,
    var mag_installment_no: String? = null,
    var view_count: String? = null,
    var picture: JsonArray? = null,
    var sharepic: String? = null,
    var is_share: String? = null,
    var share_content: String? = null,
    var label: String? = null,
    var label_id: String? = null,
    var shareurl: String? = null,
    var is_apply: String? = null,
    var activitystatus: String? = null,
    var views: String? = null,
    var favorite_id: String? = null,

    var subtitle: String? = null,
    var keywords: String? = null,
    var jump_id: String? = null
)

data class NewsItem(
    var catname: String? = null,
    var catdir: String? = null,


    var type: String? = null,

    var listorder: String? = null,

    var magid: String? = null,

    var jump_name: String? = null,

    var is_pay: String? = null,


    var id: String? = null,
    var image_text: String? = null,
    var title: String? = null,

    var thumb: String? = null,
    var url: String? = null,
    var inputtime: String? = null,
    var catid: String? = null,
    var description: String? = null,
    var area: String? = null,
    var number: String? = null,
    var address: String? = null,
    var content: String? = null,
    var activitystatus: String? = null,
    var count: String? = null,
    var sharepic: String? = null,
    var shareurl: String? = null,
    var share_content: String? = null,
    var show_button: String? = null,
    var is_apply: String? = null,
    var readpoint: String? = null,
    var invite_url: String? = null,
    var video: String? = null
)