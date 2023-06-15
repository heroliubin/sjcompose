package com.cnbizmedia.shangjie.ui.activity

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cnbizmedia.comman.view.showToast
import com.cnbizmedia.network.resp.ApiCallback
import com.cnbizmedia.shangjie.R
import com.cnbizmedia.shangjie.api.resp.Category
import com.cnbizmedia.shangjie.api.resp.SjNews
import com.cnbizmedia.shangjie.ui.weight.SjImage
import com.cnbizmedia.shangjie.viewmodel.HomeViewModel


class HomeActivity : BaseActivity() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    private val viewmodel by viewModels<HomeViewModel>()

    @Composable
    override fun setMainContent() {
        var position by remember {
            mutableIntStateOf(0)
        }

        val text = arrayListOf("资讯", "商城", "会员", "我的")

        val imgId = arrayListOf(
            R.mipmap.icon_news,
            R.mipmap.icon_money,
            R.mipmap.icon_club,
            R.mipmap.icon_my
        )


        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (bottomnavigation, contentView) = createRefs()
            Column(modifier = Modifier.constrainAs(contentView) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(bottomnavigation.top)
            }, verticalArrangement = Arrangement.Top) {
                show(position)
            }
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(bottomnavigation) {
                        bottom.linkTo(parent.bottom)
                    }, containerColor = Color.White
            ) {
                for (i in 0 until 4) {
                    bottomItem(modifier = Modifier
                        .clickable { position = i }
                        .weight(1F)
                        .padding(top = Dp(10F))
                        .fillMaxHeight(), imgId[i], text[i])
                }
            }

        }


    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun show(position: Int) {
        when (position) {
            0 -> {
                var select by remember {
                    mutableIntStateOf(0)
                }
                var categorys by remember {
                    mutableStateOf(arrayOf(Category()))
                }
                viewmodel.getCategory { its ->
                    if (its is ApiCallback.Success) {
                        its.dataResult?.let {
                            categorys = it.toTypedArray()
                        }
                    } else {
                        showToast(its.message)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.logo_info),
                        contentDescription = null
                    )
                    ScrollableTabRow(selectedTabIndex = select, Modifier.weight(1F)) {
                        categorys.forEachIndexed { index, category ->
                            Tab(selected = index == select, onClick = { select = index }
                            ) {
                                category.catname?.let { Text(text = it) }
                            }
                        }
                    }
                    Image(
                        painter = painterResource(id = R.mipmap.ic_search_black),
                        contentDescription = null
                    )
                }
                val pagerState = rememberPagerState(select, 0F) {
                    categorys.size
                }
                var news by remember {
                    mutableStateOf(SjNews(null, null))
                }
                viewmodel.getRecommendNews {
                    if (it is ApiCallback.Success) {
                        it.dataResult?.let { its -> news = its }
                    } else {
                        showToast(it.message)
                    }
                }
                HorizontalPager(state = pagerState) {
                    categorys.forEachIndexed { index, category ->
                        LazyColumn(content = {
                            news.content?.forEach {
                                item {
                                    Row {
                                        SjImage(
                                            modifier = Modifier
                                                .width(Dp(40F))
                                                .height(Dp(40F)), model = it.thumb ?: ""
                                        )
                                        Text(text = it.title?:"title")
                                    }
                                }
                            }
                        })
                    }

                }
            }


            1 -> {
                Text(text = "付费")
            }

            2 -> {
                Text(text = "会员")
            }

            3 -> {
                Text(text = "我的")
            }
        }
    }

    @Composable
    fun bottomItem(
        modifier: Modifier,
        imgId: Int,
        text: String
    ) {

        Column(
            modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imgId),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp(10F)),
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }

}