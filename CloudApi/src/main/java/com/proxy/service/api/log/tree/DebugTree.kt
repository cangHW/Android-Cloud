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

        private var printEnable: Boolean = true

        fun setPrintEnable(enable: Boolean) {
            printEnable = enable
        }
    }

    override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
        if (!printEnable) {
            return
        }

        if (message.length < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                Log.println(priority, tag, message)
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
                    Log.wtf(tag, part)
                } else {
                    Log.println(priority, tag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }
}