package com.proxy.service.api.log.tree

import android.util.Log

/**
 * @author: cangHX
 * @data: 2024/11/15 11:01
 * @desc:
 */
class ReleaseTree : DebugTree() {

    override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
        if (priority == Log.DEBUG) {
            return
        }
        super.onLog(priority, tag, message, throwable)
    }
}