package com.cnbizmedia.shangjie.viewmodel

import com.cnbizmedia.network.resp.ApiCallback
import com.cnbizmedia.shangjie.api.repo.ADApiRepo
import com.cnbizmedia.shangjie.api.resp.ADData

class SplashViewModel:BaseViewModel(){

    fun getAD(callback:(ApiCallback<ADData>)->Unit={}){
        ADApiRepo.getAD(this){
            callback.invoke(it)
        }
    }
}