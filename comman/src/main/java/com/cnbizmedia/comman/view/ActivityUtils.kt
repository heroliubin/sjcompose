package com.cnbizmedia.comman.view

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.cnbizmedia.comman.app.getColorById
import com.cnbizmedia.comman.app.isUIDarkMode


/** 页面切换动画信息 **/
typealias PageTransitionAnimationInfo = Pair<Int, Int>

/** 获取或设置页面进入时的动画 **/
val PageTransitionAnimationInfo.enterAnimationId get() = first

/** 获取或设置页面结束时的动画 **/
val PageTransitionAnimationInfo.exitAnimationId get() = second

/**
 * 系统SystemBar配置
 * @param statusBarColor 状态栏颜色, 如果该值为完全透明系颜色，那么标识DecorView是全屏显示的
 * @param isStatusBarLightMode 是否是黑色文字状态栏，默认：TRUE
 * @param navigationBarColor 底部导航栏颜色，默认：黑色 [Color.BLACK]
 * @param isNavigationBarLightMode 是否是黑色按钮导航栏，默认：FALSE
 */
data class SystemBarStyleConfig(
    @ColorInt var statusBarColor: Int,
    var isStatusBarLightMode: Boolean = true,
    @ColorInt var navigationBarColor: Int = Color.BLACK,
    var isNavigationBarLightMode: Boolean = false
)

/**
 * 设置状态栏和导航栏的颜色和状态栏文本模式
 * @param statusBarColor 状态栏背景色
 * @param applyConfig 应用状态栏配置, [SystemBarStyleConfig.isStatusBarLightMode]默认为TRUE, [SystemBarStyleConfig.navigationBarColor]默认为BLACK, [SystemBarStyleConfig.isNavigationBarLightMode]默认为FALSE
 */
fun Activity.setSystemBarStyle(
    statusBarColor: Int,
    applyConfig: SystemBarStyleConfig.() -> Unit
) {
    val config = SystemBarStyleConfig(statusBarColor).also(applyConfig)
    setSystemBarStyle(
        config.statusBarColor,
        config.isStatusBarLightMode,
        config.navigationBarColor,
        config.isNavigationBarLightMode
    )
}

/**
 * 设置状态栏和导航栏的颜色和状态栏文本模式
 * @param statusBarColor 状态栏颜色, 如果该值为完全透明系颜色，那么标识DecorView是全屏显示的
 * @param isStatusBarLightMode 是否是黑色文字状态栏，默认：TRUE
 * @param navigationBarColor 底部导航栏颜色，默认：黑色 [Color.BLACK]
 * @param isNavigationBarLightMode 是否是黑色按钮导航栏，默认：FALSE
 */
// @Suppress("DEPRECATION")
fun Activity.setSystemBarStyle(
    @ColorInt statusBarColor: Int,
    isStatusBarLightMode: Boolean = true,
    @ColorInt navigationBarColor: Int = Color.BLACK,
    isNavigationBarLightMode: Boolean = false
) {
    val decorView = window.decorView
    val navBarColor = when {
        navigationBarColor != Color.TRANSPARENT && !isUIDarkMode -> Color.WHITE
        else -> navigationBarColor
    }
    decorView.setOnApplyWindowInsetsListener { view, insets ->
        val navigationBars = WindowInsetsCompat.toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.navigationBars())
        view.findViewById<View>(android.R.id.content)
            .updatePadding(bottom = navigationBars.bottom)
        val insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets)
        ViewCompat.onApplyWindowInsets(view, insetsCompat).toWindowInsets()!!
    }
    WindowInsetsControllerCompat(window, window.decorView).apply {
        isAppearanceLightNavigationBars = when {
            !(navigationBarColor != Color.TRANSPARENT && !isUIDarkMode) -> true
            else -> isNavigationBarLightMode
        }
        isAppearanceLightStatusBars = isStatusBarLightMode
        show(WindowInsetsCompat.Type.systemBars())
        show(WindowInsetsCompat.Type.displayCutout())
    }
    window.statusBarColor = statusBarColor
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        window.navigationBarDividerColor = navBarColor
    window.navigationBarColor = navBarColor
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

/**
 * 设置状态栏和导航栏的颜色和状态栏文本模式
 * @param statusBarColorId 状态栏颜色ID, 如果该值为完全透明系颜色ID [android.R.color.transparent]，那么标识DecorView是全屏显示的
 * @param isStatusBarLightMode 是否是黑色文字状态栏，默认：TRUE
 * @param navigationBarColorId 底部导航栏颜色ID，默认：黑色 [android.R.color.black]
 * @param isNavigationBarLightMode 是否是黑色按钮导航栏，默认：FALSE
 */
fun Activity.setSystemBarStyleWithResources(
    @ColorRes statusBarColorId: Int,
    isStatusBarLightMode: Boolean = true,
    @ColorRes navigationBarColorId: Int = android.R.color.black,
    isNavigationBarLightMode: Boolean = false
) = setSystemBarStyle(
    getColorById(statusBarColorId),
    isStatusBarLightMode,
    getColorById(navigationBarColorId),
    isNavigationBarLightMode
)

/** 修改状态栏上文字图标的颜色 **/
fun Activity.changeStatusBarMode(isLightMode: Boolean) =
    WindowInsetsControllerCompat(window, window.decorView)
        .apply { isAppearanceLightStatusBars = isLightMode }