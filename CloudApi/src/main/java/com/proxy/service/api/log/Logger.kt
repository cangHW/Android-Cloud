package com.proxy.service.api.log

import com.proxy.service.api.log.base.IL
import com.proxy.service.api.log.tree.TreeGroup

/**
 * 日志工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:26
 * @desc:
 */
object Logger : IL {

    private const val TAG: String = "CLOUD_SERVICES"

    private val tree = TreeGroup()

    override fun v(throwable: Throwable) {
        tree.setTag(TAG)
        tree.v(throwable)
    }

    override fun v(message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.v(message, *args)
    }

    override fun v(throwable: Throwable, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.v(throwable, message, *args)
    }

    override fun d(throwable: Throwable) {
        tree.setTag(TAG)
        tree.d(throwable)
    }

    override fun d(message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.d(message, *args)
    }

    override fun d(throwable: Throwable, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.d(throwable, message, *args)
    }

    override fun i(throwable: Throwable) {
        tree.setTag(TAG)
        tree.i(throwable)
    }

    override fun i(message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.i(message, *args)
    }

    override fun i(throwable: Throwable, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.i(throwable, message, *args)
    }

    override fun w(throwable: Throwable) {
        tree.setTag(TAG)
        tree.w(throwable)
    }

    override fun w(message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.w(message, *args)
    }

    override fun w(throwable: Throwable, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.w(throwable, message, *args)
    }

    override fun e(throwable: Throwable) {
        tree.setTag(TAG)
        tree.e(throwable)
    }

    override fun e(message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.e(message, *args)
    }

    override fun e(throwable: Throwable, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.e(throwable, message, *args)
    }

    override fun wtf(message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.wtf(message, *args)
    }

    override fun wtf(throwable: Throwable, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.wtf(throwable, message, *args)
    }

    override fun wtf(throwable: Throwable) {
        tree.setTag(TAG)
        tree.wtf(throwable)
    }

    override fun log(priority: Int, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.log(priority, message, *args)
    }

    override fun log(priority: Int, throwable: Throwable, message: String, vararg args: Any) {
        tree.setTag(TAG)
        tree.log(priority, throwable, message, *args)
    }

    override fun log(priority: Int, throwable: Throwable) {
        tree.setTag(TAG)
        tree.log(priority, throwable)
    }
}