package com.example.imkotlin.presenter

import android.util.Log
import com.example.imkotlin.adapter.EMCallBackAdapter
import com.example.imkotlin.contract.LoginContract
import com.example.imkotlin.extention.isValidPassword
import com.example.imkotlin.extention.isValidUserName
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient

class LoginPresenter(val view: LoginContract.View): LoginContract.Presenter{

    override fun login(userName: String, password: String) {
        if (userName.isValidUserName()){
            //用户名合法，继续校验密码
            if (password.isValidPassword()){
                //密码合法，开始登录
                view.onStartLogin()
                //登录到环信
                loginEaseMob(userName,password)
            } else view.onPasswordError()
        } else view.onUserNameError()
    }

    //登录环信账号,model层
    private fun loginEaseMob(userName: String, password: String) {
        EMClient.getInstance().login(userName,password,object : EMCallBackAdapter() {
            //在子线程回调
            override fun onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
                //在主线程通知View层
                uiThread { view.onLoggedInSuccess() }
                Log.d("main", "登录聊天服务器成功！")
            }

            override fun onError(p0: Int, p1: String?) {
                uiThread { view.onLoggedInFailed() }
                Log.d("main", "登录聊天服务器失败！")
            }
        })
    }

}