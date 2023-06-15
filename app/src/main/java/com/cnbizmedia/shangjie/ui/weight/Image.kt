package com.cnbizmedia.shangjie.ui.weight

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
fun SjImage(modifier: Modifier, model: Any) {
//    GlideImage(
//        modifier = modifier,
//        model = model,
//        contentDescription = null, contentScale = ContentScale.Crop
//    )
    AsyncImage(modifier = modifier, model = model, contentDescription = null)
}