package com.cnbizmedia.shangjie.api.repo

import androidx.lifecycle.ViewModel
import com.cnbizmedia.network.core.doHttpApiRequest
import com.cnbizmedia.network.resp.ApiCallback
import com.cnbizmedia.shangjie.api.core.ApiManager
import com.cnbizmedia.shangjie.api.resp.ADData
import javax.security.auth.callback.Callback

object ADApiRepo {
    fun getAD(viewModel: ViewModel,callback: (ApiCallback<ADData>)->Unit={})=viewModel.doHttpApiRequest({
        ApiManager.appService.getAD()
    },callback)
}