package com.example.imkotlin.contract

//添加好友MVP协议
interface AddFriendContact {

    interface Presenter: BasePresenter{
        fun search(key:String)
    }

    interface View {
        fun onSearchSuccess()
        fun onSearchFailed()
    }

}