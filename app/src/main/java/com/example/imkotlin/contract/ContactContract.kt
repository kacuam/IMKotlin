package com.example.imkotlin.contract

//联系人界面MVP协议 - view - presenter - model
interface ContactContract {

    interface Presenter: BasePresenter{
        fun loadContacts()
    }

    interface View {
        fun onLoadContactsSuccess()
        fun onLoadContactsFaild()
    }
}