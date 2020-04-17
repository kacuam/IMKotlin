package com.example.imkotlin.contract

//登录界面MVP协议
interface LoginContract {
    //presenter层
    interface Presenter: BasePresenter{

        fun login(userName: String,password: String)

    }
    //view层
    interface View {
        fun onUserNameError()
        fun onPasswordError()
        fun onStartLogin()
        fun onLoggedInSuccess()
        fun onLoggedInFailed()
    }

}