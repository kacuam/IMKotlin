package com.example.imkotlin.ui.activity

import android.os.Handler
import com.example.imkotlin.R
import com.example.imkotlin.contract.SplashContract
import com.example.imkotlin.presenter.SplashPresenter
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity(),SplashContract.View {

    val presenter = SplashPresenter(this)

    //伴生对象在类中只能存在一个
    companion object{
        val DELAY = 2000L
    }

    val handler by lazy {
        Handler()
    }

    override fun init() {
        super.init()
        presenter.checkLoginStatus()
    }

    override fun getLayoutResId(): Int =
        R.layout.activity_splash

    //延时2s，跳转到登录界面
    override fun onNotLoggedIn() {
        handler.postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, DELAY)
    }

    //跳转到主界面
    override fun onLoggedIn() {
        startActivity<MainActivity>()
        finish()
    }

}