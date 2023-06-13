package com.cnbizmedia.shangjie.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.cnbizmedia.comman.app.ConfigUtils
import com.cnbizmedia.network.resp.ApiCallback
import com.cnbizmedia.shangjie.R
import com.cnbizmedia.shangjie.api.resp.ADlist
import com.cnbizmedia.shangjie.app.SjConstants
import com.cnbizmedia.shangjie.viewmodel.SplashViewModel
import com.google.gson.Gson
import java.lang.Exception

class SplashActivity : BaseActivity() {
    private val viewmodel by viewModels<SplashViewModel>()


    @Composable
    override fun setMainContent() {
        var imageurl by remember { mutableStateOf("") }
        var gotext by remember {
            mutableStateOf("跳过")
        }
        var adinfo by remember {
            mutableStateOf(ADlist())
        }
        viewmodel.getAD {
            if (it is ApiCallback.Success)
                it.dataResult?.let { it1 ->
                    try {
                        ConfigUtils.default().write(SjConstants.Ads, Gson().toJson(it1))
                    }catch (_:Exception){

                    }
                    it1.alist?.forEach { ad ->
                        if (ad?.type == "1") {
                            ad.typeval?.let { img ->
                                imageurl = img
                            }
                            adinfo=ad
                        }
                    }
                }
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (adimg, topbutton, bottombuttton, bottomimg) = createRefs()

            Image(modifier = Modifier.constrainAs(bottomimg) {
                bottom.linkTo(parent.bottom)
            }, painter = painterResource(id = R.mipmap.welcome_logo), contentDescription = null)

            SjImage(modifier = Modifier
                .wrapContentHeight(Alignment.Top, false)
                .constrainAs(adimg) {
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomimg.top)
                }, model = imageurl)
            Button(modifier = Modifier.constrainAs(topbutton) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)

            }, onClick = {
                GuideActivity.launch(this@SplashActivity)
            }) {
                Text(text =gotext)
            }
            if (adinfo.adlink?.isNotBlank() == true){
            Button(modifier = Modifier.constrainAs(bottombuttton) {
                bottom.linkTo(bottomimg.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onClick = {
                WebActivity.launch(this@SplashActivity,adinfo.adlink,adinfo.name)
            }) {
                Text(text = "查看详情")
            }
        }

    }

}

@Composable
fun SjImage(modifier: Modifier, model: Any) {
//    GlideImage(
//        modifier = modifier,
//        model = model,
//        contentDescription = null, contentScale = ContentScale.Crop
//    )
    AsyncImage(modifier = modifier, model = model, contentDescription = null)
}

@Preview
@Composable
fun GreetingPreview() {

}
}