package com.cnbizmedia.shangjie.viewmodel

import com.cnbizmedia.network.resp.ApiCallback
import com.cnbizmedia.shangjie.api.repo.NewsApiRepo
import com.cnbizmedia.shangjie.api.resp.Category
import com.cnbizmedia.shangjie.api.resp.HotNews
import com.cnbizmedia.shangjie.api.resp.SjNews

class HomeViewModel : BaseViewModel() {
    fun getCategory(callback: (ApiCallback<List<Category>>) -> Unit = {}) {
        NewsApiRepo.getCategory(this){
            callback.invoke(it)
        }
    }
    fun getRecommendNews(callback: (ApiCallback<SjNews>) -> Unit = {}) {
        NewsApiRepo.getRecommend(slug = "tuijian", viewModel = this){
            callback.invoke(it)
        }
    }
    fun getHotNews(callback: (ApiCallback<List<HotNews>>) -> Unit = {}) {
        NewsApiRepo.getHotNew(this){
            callback.invoke(it)
        }
    }

}