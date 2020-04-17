package com.example.imkotlin.ui.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar

abstract class BaseActivity : AppCompatActivity() {

    val progressDialog by lazy {
        ProgressDialog(this)
    }

    val inputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        init()
    }

    //初始化一些公共的功能，比如摇一摇，子类也可以覆写该方法，完成自己的初始化
    open fun init() {}

    //子类必须实现该方法返回一个布局资源的id
    abstract fun getLayoutResId(): Int

    fun showProgress(message: String) {
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    fun dismissProgress() {
        progressDialog.dismiss()
    }

    //提供隐藏软键盘方法
    fun hideSoftKeyboard() {
        //如果window上view获取焦点 && view不为空
        if (inputMethodManager.isActive && currentFocus != null) {
            //拿到view的token 不为空
            if (currentFocus.windowToken != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示
                inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}