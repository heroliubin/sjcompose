package com.cnbizmedia.network.resp

/** API接口返回类型 **/
sealed class ApiCallback<out T : Any> private constructor() {

    /** 接口返回数据请求码 **/
    @Transient
    open var code: String? = null

    /** 接口返回消息内容 **/
    @Transient
    open var message: String? = null

    /**
     * 成功事件
     * @param code 结果码
     * @param message 消息内容
     */
    data class Success<out T : Any> @JvmOverloads constructor(
        override var code: String? = null,
        override var message: String? = null,
        val dataResult: T? = null
    ) : ApiCallback<T>()

    /**
     * 错误事件
     * @param code 结果码
     * @param message 消息内容
     */
    data class Error @JvmOverloads constructor(
        override var code: String? = null,
        override var message: String? = null
    ) : ApiCallback<Nothing>()


    companion object {

        @JvmStatic
        fun <T : Any, R : Any> from(
            other: ApiCallback<T>,
            transform: T?.() -> R?
        ): ApiCallback<R> = when (other) {
            is Success -> Success(other.code, other.message, transform(other.dataResult))
            else -> Error(other.code, other.message)
        }

        @JvmStatic
        fun <T : Any> from(value: IBasicRespInfo<T>): ApiCallback<T> = when {
            value.isSuccessful ->
                Success(value.code, value.message, value.data)
            else -> Error(value.code, value.message)
        }

        @JvmStatic
        fun <T : Any, R : Any> from(
            value: IBasicRespInfo<T>,
            transform: T?.() -> R?
        ): ApiCallback<R> = when {
            value.isSuccessful ->
                Success(value.code, value.message, transform(value.data))
            else -> Error(value.code, value.message)
        }

    }
}