package com.cnbizmedia.shangjie.api.repo

import androidx.lifecycle.ViewModel
import com.cnbizmedia.network.core.doHttpApiRequest
import com.cnbizmedia.network.resp.ApiCallback
import com.cnbizmedia.shangjie.api.core.ApiManager
import com.cnbizmedia.shangjie.api.resp.Category
import com.cnbizmedia.shangjie.api.resp.HotNews
import com.cnbizmedia.shangjie.api.resp.SjNews

object NewsApiRepo {
    fun getCategory(
        viewModel: ViewModel,
        callback: (ApiCallback<List<Category>>) -> Unit = {}
    ) = viewModel.doHttpApiRequest({
        ApiManager.appService.getCategory()
    }, callback)

    fun getRecommend(
        p:Int=1,
        ps:Int=10,
        top:Int=1,
        slug:String,
        viewModel: ViewModel,
        callback: (ApiCallback<SjNews>) -> Unit = {}
    ) = viewModel.doHttpApiRequest({
        ApiManager.appService.getRecommendNews(p,ps,top,slug)
    }, callback)

    fun getHotNew(
        viewModel: ViewModel,
        callback: (ApiCallback<List<HotNews>>) -> Unit = {}
    ) = viewModel.doHttpApiRequest({
        ApiManager.appService.getHotNews()
    }, callback)
}