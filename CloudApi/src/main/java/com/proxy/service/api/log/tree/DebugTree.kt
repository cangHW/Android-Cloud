package com.proxy.service.api.log.tree

import android.text.TextUtils
import android.util.Log
import com.proxy.service.api.log.base.LogTree
import java.util.regex.Pattern

/**
 * @author: cangHX
 * @data: 2024/11/15 11:01
 * @desc:
 */
open class DebugTree : LogTree() {

    companion object {
        private const val MAX_LOG_LENGTH = 3000
        private const val MAX_TAG_LENGTH = 23
        private const val CALL_STACK_INDEX = 7
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

        private var printEnable: Boolean = true

        fun setPrintEnable(enable: Boolean) {
            printEnable = enable
        }
    }

    private fun getClassName(): String {
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

    override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
        if (!printEnable) {
            return
        }

        val tagO = if (TextUtils.isEmpty(tag)) {
            getClassName()
        } else {
            tag
        }

        if (message.length < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tagO, message)
            } else {
                Log.println(priority, tagO, message)
            }
            return
        }
        var i = 0
        while (i >= 0 && i < message.length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) {
                newline
            } else {
                message.length
            }
            do {
                val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                if (priority == Log.ASSERT) {
                    Log.wtf(tagO, part)
                } else {
                    Log.println(priority, tagO, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }
}