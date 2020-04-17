package com.example.imkotlin.presenter

import com.example.imkotlin.contract.SplashContract
import com.hyphenate.chat.EMClient

class SplashPresenter(val view: SplashContract.View) : SplashContract.Presenter{

    override fun checkLoginStatus() {
        if (isLoggedIn()) view.onLoggedIn() else view.onNotLoggedIn()
    }

    //是否登录环信的服务器，model层
    private fun isLoggedIn(): Boolean =
        EMClient.getInstance().isConnected && EMClient.getInstance().isLoggedInBefore

}