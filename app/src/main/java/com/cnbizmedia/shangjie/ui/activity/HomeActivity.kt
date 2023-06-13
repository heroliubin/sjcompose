package com.cnbizmedia.shangjie.ui.activity

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cnbizmedia.shangjie.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeActivity : BaseActivity() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    @Composable
    override fun setMainContent() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {

            }
            composable("buy") {

            }
            composable("club") {

            }
            composable("mine") {

            }
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (bottomnavigation) = createRefs()
            NavigationBar(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomnavigation) {
                    bottom.linkTo(parent.bottom)
                }, containerColor = Color.White
            ) {
                Column(
                    Modifier
                        .clickable { navController.navigate("home") }
                        .weight(1F).padding(top = Dp(10F)).fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.icon_news),
                        contentDescription = null
                    )
                    Text(modifier = Modifier.fillMaxWidth().padding(top = Dp(10F)),
                        text = "资讯",
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    Modifier
                        .clickable { navController.navigate("buy") }
                        .weight(1F).padding(top = Dp(10F)).fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.icon_money),
                        contentDescription = null
                    )
                    Text(modifier = Modifier.fillMaxWidth().padding(top = Dp(10F)),
                        text = "付费",
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    Modifier
                        .clickable { navController.navigate("club") }
                        .weight(1F).padding(top = Dp(10F)).fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.icon_club),
                        contentDescription = null
                    )
                    Text(modifier = Modifier.fillMaxWidth().padding(top = Dp(10F)),
                        text = "会员",
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    Modifier
                        .clickable { navController.navigate("mine") }
                        .weight(1F).padding(top = Dp(10F)).fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.icon_my),
                        contentDescription = null
                    )
                    Text(modifier = Modifier.fillMaxWidth().padding(top = Dp(10F)),
                        text = "我的",
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

    }
    @Composable
    fun show(route:String){

    }

}