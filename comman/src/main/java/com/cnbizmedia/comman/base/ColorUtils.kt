package com.cnbizmedia.comman.base

import android.graphics.Color

/** Int颜色 => ARGB(0x??????)的颜色值 **/
fun Int.toHexColor(): String {
    val toHexString: ((Int) -> Int) -> String = { funConverter ->
        val result = Integer.toHexString(funConverter(this))
        if (result.length == 1) "0$result" else result
    }
    val alpha = toHexString(Color::alpha)
    val red = toHexString(Color::red)
    val green = toHexString(Color::green)
    val blue = toHexString(Color::blue)
    return "0x%s%s%s%s".format(alpha, red, green, blue)
}

/** Int颜色 => #ARGB(#FF??????)的颜色值 **/
fun Int.toColorString(): String = toHexColor().replace("0x", "#")

/** Int颜色 => Web(#??????FF)的颜色值 **/
fun Int.toWebColorString(): String =
    toHexColor().replace("0x([A-Fa-f\\d]{2})([A-Fa-f\\d]{6})".toRegex(), "#$2$1")