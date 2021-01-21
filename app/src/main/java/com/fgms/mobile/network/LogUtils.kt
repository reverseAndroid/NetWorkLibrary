package com.fgms.mobile.network

import android.text.TextUtils
import android.util.Log


object LogUtils {

    /**
     * 日志输出时的TAG
     */
    private val mTag = "NetWorkLibrary"

    /**
     * 日志输出级别NONE
     */
    val LEVEL_OFF = 0

    /**
     * 日志输出级别NONE
     */
    val LEVEL_ALL = 7

    /**
     * 日志输出级别V
     */
    val LEVEL_VERBOSE = 1

    /**
     * 日志输出级别D
     */
    val LEVEL_DEBUG = 2

    /**
     * 日志输出级别I
     */
    val LEVEL_INFO = 3

    /**
     * 日志输出级别W
     */
    val LEVEL_WARN = 4

    /**
     * 日志输出级别E
     */
    val LEVEL_ERROR = 5

    /**
     * 日志输出级别S,自定义定义的一个级别
     */
    val LEVEL_SYSTEM = 6

    /**
     * 是否允许输出log
     */
    private val mDebuggable = LEVEL_ALL

    /**
     * 用于记时的变量
     */
    private var mTimestamp: Long = 0

    /**
     * 写文件的锁对象
     */
    private val mLogLock = Any()

    /**---------------日志输出,已固定TAG  begin---------------**/
    /**---------------日志输出,已固定TAG  begin--------------- */
    /**
     * 以级别为 d 的形式输出LOG
     */
    fun v(msg: String) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(mTag, msg)
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    fun d(msg: String) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(mTag, msg)
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    fun i(msg: String) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(mTag, msg)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    fun w(msg: String) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, msg)
        }
    }

    /**
     * 以级别为 w 的形式输出Throwable
     */
    fun w(tr: Throwable) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, "", tr)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    fun w(msg: String?, tr: Throwable?) {
        if (mDebuggable >= LEVEL_WARN && null != msg) {
            Log.w(mTag, msg, tr)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    fun e(msg: String) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, msg)
        }
    }

    /**
     * 以级别为 s 的形式输出LOG,主要是为了System.out.println,稍微格式化了一下
     */
    fun sf(msg: String) {
        if (mDebuggable >= LEVEL_ERROR) {
            println("----------$msg----------")
        }
    }

    /**
     * 以级别为 s 的形式输出LOG,主要是为了System.out.println
     */
    fun s(msg: String?) {
        if (mDebuggable >= LEVEL_ERROR) {
            println(msg)
        }
    }

    /**
     * 以级别为 e 的形式输出Throwable
     */
    fun e(tr: Throwable?) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, "", tr)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    fun e(msg: String?, tr: Throwable?) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            Log.e(mTag, msg, tr)
        }
    }

    /**---------------日志输出,已固定TAG  end---------------**/

    /**---------------日志输出,未固定TAG  begin---------------**/
    /**---------------日志输出,已固定TAG  end--------------- */
    /**---------------日志输出,未固定TAG  begin--------------- */
    /**
     * 以级别为 d 的形式输出LOG
     */
    fun v(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(tag, msg)
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    fun d(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(tag, msg)
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    fun i(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(tag, msg)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    fun w(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(tag, msg)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    fun e(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(tag, msg)
        }
    }

    /**---------------日志输出,未固定TAG  end---------------**/

    /**---------------日志输出,未固定TAG  end--------------- */
    /**
     * 把Log存储到文件中
     *
     * @param log  需要存储的日志
     * @param path 存储路径
     */
//        fun log2File(log: String, path: String) {
//            log2File(log, path, true)
//        }
//
//        fun log2File(log: String, path: String?, append: Boolean) {
//            synchronized(mLogLock) {
//                FileUtils.writeFile(log + "\r\n", path, append)
//            }
//        }

    /**
     * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段起始点
     *
     * @param msg 需要输出的msg
     */
    fun msgStartTime(msg: String) {
        mTimestamp = System.currentTimeMillis()
        if (!TextUtils.isEmpty(msg)) {
            e("[Started：$mTimestamp]$msg")
        }
    }

    /**
     * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段结束点* @param msg 需要输出的msg
     */
    fun elapsed(msg: String) {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - mTimestamp
        mTimestamp = currentTime
        e("[Elapsed：$elapsedTime]$msg")
    }

    fun <T> printList(list: List<T>?) {
        if (list == null || list.size < 1) {
            return
        }
        val size = list.size
        i("---begin---")
        for (i in 0 until size) {
            i(i.toString() + ":" + list[i].toString())
        }
        i("---end---")
    }

    fun <T> printArray(array: Array<T>?) {
        if (array == null || array.size < 1) {
            return
        }
        val length = array.size
        i("---begin---")
        for (i in 0 until length) {
            i(i.toString() + ":" + array[i].toString())
        }
        i("---end---")
    }

    fun logParamter(url: String, vararg paramter: Array<String>) {
        e("url = $url")
        for (p in paramter) {
            e(p[0] + " = " + p[1])
        }
    }
}