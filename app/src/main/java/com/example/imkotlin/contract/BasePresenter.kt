package com.example.imkotlin.contract

import android.os.Handler
import android.os.Looper

interface BasePresenter{

    companion object{
        val handler by lazy {
            //创建一个在主线程的Looper的handler
            Handler(Looper.getMainLooper())
        }
    }

    //接收一个无返回参数的主线程方法
    fun uiThread(f: () -> Unit) {
        handler.post { f() }
    }
}
