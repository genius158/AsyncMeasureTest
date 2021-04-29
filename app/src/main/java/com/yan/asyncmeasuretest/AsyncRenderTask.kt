package com.yan.asyncmeasuretest

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/4/28
 */
class AsyncRenderTask(private var runnable: Runnable? = null) : Runnable {
    companion object {
        // 处理同步问题
        private val taskSet = ConcurrentHashMap<AsyncRenderTask, Any>()

        fun clearTask() {
            taskSet.clear()
        }

        fun isTaskEmpty(): Boolean {
//            Log.e("AsyncRenderTask", "isTaskEmpty  " + taskSet.size)
            return taskSet.isEmpty()
        }

        fun provide(runnable: Runnable): AsyncRenderTask {
            val task = AsyncRenderTask(runnable)
            taskSet[task] = task
            return task
        }
    }

    override fun run() {
        taskSet.remove(this)
        runnable?.run()
    }

}