package com.example.imkotlin.contract

interface GroupPickContactsContract {

    interface Presenter: BasePresenter {
        fun loadContacts()
    }

    interface View {
        fun onLoadContactsSuccess()
        fun onLoadContactsFaild()
    }

}