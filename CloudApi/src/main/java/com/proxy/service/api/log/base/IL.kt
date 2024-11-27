package com.proxy.service.api.log.base

/**
 * @author: cangHX
 * @data: 2024/4/28 20:20
 * @desc:
 */
interface IL {

    fun v(message: String, vararg args: Any)

    fun v(throwable: Throwable, message: String, vararg args: Any)

    fun v(throwable: Throwable)

    fun d(message: String, vararg args: Any)

    fun d(throwable: Throwable, message: String, vararg args: Any)

    fun d(throwable: Throwable)

    fun i(message: String, vararg args: Any)

    fun i(throwable: Throwable, message: String, vararg args: Any)

    fun i(throwable: Throwable)

    fun w(message: String, vararg args: Any)

    fun w(throwable: Throwable, message: String, vararg args: Any)

    fun w(throwable: Throwable)

    fun e(message: String, vararg args: Any)

    fun e(throwable: Throwable, message: String, vararg args: Any)

    fun e(throwable: Throwable)

    fun wtf(message: String, vararg args: Any)

    fun wtf(throwable: Throwable, message: String, vararg args: Any)

    fun wtf(throwable: Throwable)

    fun log(priority: Int, message: String, vararg args: Any)

    fun log(priority: Int, throwable: Throwable, message: String, vararg args: Any)

    fun log(priority: Int, throwable: Throwable)
}