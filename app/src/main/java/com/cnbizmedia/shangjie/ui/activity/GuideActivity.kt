package com.cnbizmedia.shangjie.ui.activity

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.cnbizmedia.shangjie.R

class GuideActivity : BaseActivity() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(
                Intent(context, GuideActivity::class.java)
            )
        }

    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun setMainContent() {
        val imgs by remember {
            mutableStateOf(
                arrayListOf(
                    R.mipmap.guide1,
                    R.mipmap.guide2,
                    R.mipmap.guide3,
                    R.mipmap.guide4,
                    R.mipmap.guide5
                )
            )
        }
        val pagerState = rememberPagerState (0,0F){
            imgs.size
        }
        HorizontalPager( state = pagerState) {
            Image(
                painter = painterResource(id = imgs[it]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = it == imgs.size - 1) {
                        HomeActivity.launch(this@GuideActivity)
                    })
        }
    }


}