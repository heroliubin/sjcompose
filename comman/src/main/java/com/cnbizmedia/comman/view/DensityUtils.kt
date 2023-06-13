@file: JvmName("DensityUtils")

package com.cnbizmedia.comman.view

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import com.cnbizmedia.comman.context.ContextProvider

/**
 * dp转px
 * @param dpVal   dip值
 */
fun Context.convertDip2px(dpVal: Number): Number = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dpVal.toFloat(), resources.displayMetrics
)

/** dp转px **/
val Number.dip2px: Number
    get() = ContextProvider.get().context?.convertDip2px(toFloat()) ?: 0f

/**
 * sp转px
 * @param spVal   sp值
 */
fun Context.convertSp2px(spVal: Number): Number = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    spVal.toFloat(), resources.displayMetrics
)

/** sp转px **/
val Number.sp2px: Number
    get() = ContextProvider.get().context?.convertSp2px(toFloat()) ?: 0f

/**
 * px转dp
 *
 * @param pxVal   px值
 */
fun Context.convertPx2dip(pxVal: Number): Number =
    pxVal.toFloat() / resources.displayMetrics.density

/** px转dp **/
val Number.px2dip: Number
    get() = (ContextProvider.get().context?.convertPx2dip(toFloat()) ?: 0f)

/**
 * px转sp
 *
 * @param pxVal   px值
 */
fun Context.convertPx2sp(pxVal: Number): Number =
    pxVal.toFloat() / resources.displayMetrics.scaledDensity

/** px转sp **/
val Number.px2sp: Number
    get() = (ContextProvider.get().context?.convertPx2sp(toFloat()) ?: 0f).toFloat()


@Dimension
fun Context.getPxDimensionById(@DimenRes id: Int) =
    resources.getDimension(id)

@Dimension
fun Int.toPxDimension(): Float =
    ContextProvider.get().context?.getPxDimensionById(this) ?: 0f

@Dimension
fun Context.getPxDimensionOffsetById(@DimenRes id: Int) =
    resources.getDimensionPixelOffset(id)

@Dimension
fun Int.toPxDimensionOffset(): Int =
    ContextProvider.get().context?.getPxDimensionOffsetById(this) ?: 0

@Dimension
fun Context.getPxDimensionSizeById(@DimenRes id: Int) =
    resources.getDimensionPixelSize(id)

@Dimension
fun Int.toPxDimensionSize(): Int =
    ContextProvider.get().context?.getPxDimensionSizeById(this) ?: 0