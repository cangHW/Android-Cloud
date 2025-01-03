package com.proxy.service.api.log.base

import android.util.Log
import com.proxy.service.api.log.tree.DebugTree
import com.proxy.service.api.log.tree.DebugTree.Companion
import java.io.PrintWriter
import java.io.StringWriter
import java.util.regex.Pattern

/**
 * @author: cangHX
 * @data: 2024/11/15 09:58
 * @desc:
 */
abstract class LogTree : IL {

    companion object {
        private const val MAX_TAG_LENGTH = 23
        private const val CALL_STACK_INDEX = 5
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }

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

    protected fun getClassName(): String {
        val stackTrace = Throwable().stackTrace
        check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements: are you using proguard?" }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX])
    }

    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        return if (tag.length <= MAX_TAG_LENGTH) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
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

        onLog(priority, getTag() ?: getClassName(), msg, throwable)
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