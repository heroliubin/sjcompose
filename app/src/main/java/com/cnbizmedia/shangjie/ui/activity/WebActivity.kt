package com.cnbizmedia.shangjie.ui.activity

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.cnbizmedia.shangjie.R
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewState

class WebActivity : BaseActivity() {
    companion object {
        private const val ARG_URL = "url"
        private const val ARG_TITLE = "title"
        fun launch(context: Context, url: String?, title: String?) {
            context.startActivity(
                Intent(context, WebActivity::class.java).putExtra(ARG_URL, url).putExtra(
                    ARG_TITLE, title
                )
            )
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun setMainContent() {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { finish() }, contentPadding = PaddingValues(Dp(15F), Dp(5F)),
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.black_back),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.weight(1F),
                    text = intent.getStringExtra(ARG_TITLE) ?: "广告", fontSize = TextUnit(
                        14F,
                        TextUnitType.Sp
                    ), maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center
                )
            }
            val state = rememberWebViewState(url = intent.getStringExtra(ARG_URL) ?: "")
            WebView(state = state)

        }
    }
}