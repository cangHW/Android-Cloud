package com.proxy.service.api.log.tree

import com.proxy.service.api.log.base.LogTree

/**
 * @author: cangHX
 * @data: 2024/11/15 09:59
 * @desc:
 */
class TreeGroup : LogTree() {

    companion object {
        private val TREE_ARRAY_EMPTY: Array<LogTree> = emptyArray()
        private val FOREST: ArrayList<LogTree> = ArrayList()

        @Volatile
        private var forestAsArray: Array<LogTree> = TREE_ARRAY_EMPTY

        fun plant(tree: LogTree) {
            synchronized(FOREST) {
                FOREST.add(tree)
                forestAsArray = FOREST.toTypedArray()
            }
        }
    }

    override fun setTag(tag: String) {
        forestAsArray.forEach {
            it.setTag(tag)
        }
    }

    override fun v(message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.v(message, args)
        }
    }

    override fun v(throwable: Throwable, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.v(throwable, message, args)
        }
    }

    override fun v(throwable: Throwable) {
        forestAsArray.forEach {
            it.v(throwable)
        }
    }

    override fun d(message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.d(message, args)
        }
    }

    override fun d(throwable: Throwable, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.d(throwable, message, args)
        }
    }

    override fun d(throwable: Throwable) {
        forestAsArray.forEach {
            it.d(throwable)
        }
    }

    override fun i(message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.i(message, args)
        }
    }

    override fun i(throwable: Throwable, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.i(throwable, message, args)
        }
    }

    override fun i(throwable: Throwable) {
        forestAsArray.forEach {
            it.i(throwable)
        }
    }

    override fun w(message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.w(message, args)
        }
    }

    override fun w(throwable: Throwable, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.w(throwable, message, args)
        }
    }

    override fun w(throwable: Throwable) {
        forestAsArray.forEach {
            it.w(throwable)
        }
    }

    override fun e(message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.e(message, args)
        }
    }

    override fun e(throwable: Throwable, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.e(throwable, message, args)
        }
    }

    override fun e(throwable: Throwable) {
        forestAsArray.forEach {
            it.e(throwable)
        }
    }

    override fun wtf(message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.wtf(message, args)
        }
    }

    override fun wtf(throwable: Throwable, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.wtf(throwable, message, args)
        }
    }

    override fun wtf(throwable: Throwable) {
        forestAsArray.forEach {
            it.wtf(throwable)
        }
    }

    override fun log(priority: Int, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.log(priority, message, args)
        }
    }

    override fun log(priority: Int, throwable: Throwable, message: String, vararg args: Any) {
        forestAsArray.forEach {
            it.log(priority, throwable, message, args)
        }
    }

    override fun log(priority: Int, throwable: Throwable) {
        forestAsArray.forEach {
            it.log(priority, throwable)
        }
    }

    override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
        //nothing
    }
}