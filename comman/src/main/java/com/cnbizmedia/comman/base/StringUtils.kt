package com.cnbizmedia.comman.base

/** 判断是否是电子邮箱地址 **/
val String.isEmailAddress: Boolean
    get() = matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$".toRegex())

/** 判断是否是中国的电话号码 **/
val String.isPhoneNumber: Boolean
    get() = matches("^(\\+?86)?1\\d{10}$".toRegex())

/** 隐藏电话号码中间几个字符 **/
fun String.toSecurePhoneNumber(): String =
    replace("(.*\\d{3})(\\d{4})(\\d{4})$".toRegex(), "$1****$3")

/** 判断字符串是否是Base64字符串 **/
val String.isBase64Text: Boolean
    get() = matches(Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"))