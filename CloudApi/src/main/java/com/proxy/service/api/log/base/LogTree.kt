package com.proxy.service.api.log.base

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author: cangHX
 * @data: 2024/11/15 09:58
 * @desc:
 */
abstract class LogTree : IL {

    private val explicitTag: ThreadLocal<String> = ThreadLocal()

    open fun setTag(tag: String) {
        explicitTag.set(tag)
    }

    private fun getTag(): String? {
        val tag = explicitTag.get()
        if (tag != null) {
            explicitTag.remove()
        }
        return tag
    }

    override fun v(message: String, vararg args: Any) {
        prepareLog(Log.VERBOSE, null, message, *args)
    }

    override fun v(throwable: Throwable, message: String, vararg args: Any) {
        prepareLog(Log.VERBOSE, throwable, message, *args)
    }

    override fun v(throwable: Throwable) {
        prepareLog(Log.VERBOSE, throwable, null)
    }

    override fun d(message: String, vararg args: Any) {
        prepareLog(Log.DEBUG, null, message, *args)
    }

    override fun d(throwable: Throwable, message: String, vararg args: Any) {
        prepareLog(Log.DEBUG, throwable, message, *args)
    }

    override fun d(throwable: Throwable) {
        prepareLog(Log.DEBUG, throwable, null)
    }

    override fun i(message: String, vararg args: Any) {
        prepareLog(Log.INFO, null, message, *args)
    }

    override fun i(throwable: Throwable, message: String, vararg args: Any) {
        prepareLog(Log.INFO, throwable, message, *args)
    }

    override fun i(throwable: Throwable) {
        prepareLog(Log.INFO, throwable, null)
    }

    override fun w(message: String, vararg args: Any) {
        prepareLog(Log.WARN, null, message, *args)
    }

    override fun w(throwable: Throwable, message: String, vararg args: Any) {
        prepareLog(Log.WARN, throwable, message, *args)
    }

    override fun w(throwable: Throwable) {
        prepareLog(Log.WARN, throwable, null)
    }

    override fun e(message: String, vararg args: Any) {
        prepareLog(Log.ERROR, null, message, *args)
    }

    override fun e(throwable: Throwable, message: String, vararg args: Any) {
        prepareLog(Log.ERROR, throwable, message, *args)
    }

    override fun e(throwable: Throwable) {
        prepareLog(Log.ERROR, throwable, null)
    }

    override fun wtf(message: String, vararg args: Any) {
        prepareLog(Log.ASSERT, null, message, *args)
    }

    override fun wtf(throwable: Throwable, message: String, vararg args: Any) {
        prepareLog(Log.ASSERT, throwable, message, *args)
    }

    override fun wtf(throwable: Throwable) {
        prepareLog(Log.ASSERT, throwable, null)
    }

    override fun log(priority: Int, message: String, vararg args: Any) {
        prepareLog(priority, null, message, *args)
    }

    override fun log(priority: Int, throwable: Throwable, message: String, vararg args: Any) {
        prepareLog(priority, throwable, message, *args)
    }

    override fun log(priority: Int, throwable: Throwable) {
        prepareLog(priority, throwable, null)
    }

    private fun prepareLog(
        priority: Int,
        throwable: Throwable?,
        message: String?,
        vararg args: Any
    ) {
        var msg = message
        if (msg.isNullOrEmpty()) {
            if (throwable == null) {
                return
            }
            msg = getStackTraceString(throwable)
        }
        if (args.isNotEmpty()) {
            msg = formatMessage(msg, *args)
        }
        if (throwable != null) {
            msg += "\n ${getStackTraceString(throwable)}"
        }

        onLog(priority, getTag() ?: "", msg, throwable)
    }

    private fun formatMessage(message: String, vararg args: Any): String {
        return String.format(message, *args)
    }

    private fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    /**
     * 日志回调
     *
     * @param priority  日志级别, 参考[Log]的级别
     * */
    protected abstract fun onLog(
        priority: Int,
        tag: String,
        message: String,
        throwable: Throwable?
    )

}