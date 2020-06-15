package com.example.dzj.mogemap.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author: dzj
 * @date: 2020/6/9 15:09
 *
 */
class ThreadUtil private constructor() {
    private var executor: ExecutorService? = null

    init {
        executor = Executors.newCachedThreadPool()
    }

    fun execute(runnable: Runnable) {
        executor!!.execute(runnable)
    }

    fun shutdow() {
        executor!!.shutdown()
    }

    fun shutdowNow() {
        executor!!.shutdownNow()
    }

    fun destory() {
        if (!executor!!.isShutdown) {
            executor!!.shutdownNow()
        }
        executor = null
        instance = null
    }

    companion object {
        @Volatile
        private var instance: ThreadUtil? = null

        fun getInstance() : ThreadUtil? {
            instance ?: synchronized(this) {
                instance ?: ThreadUtil().also { instance = it }
            }
            return instance
        }

        val isEmpty: Boolean
            get() = instance == null
    }

}