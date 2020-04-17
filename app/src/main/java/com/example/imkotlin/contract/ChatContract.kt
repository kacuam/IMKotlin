package com.example.imkotlin.contract

import com.hyphenate.chat.EMMessage

//发送消息MVP协议
interface ChatContract {

    interface Presenter: BasePresenter{
        fun sendMessage(contact:String, message: String)
        fun addMessage(username: String, p0: MutableList<EMMessage>?)
        fun loadMessages(username: String)
        fun loadMoreMessage(username: String)
    }

    interface View {
        fun onStartSendMessage()  //发送出去就刷新列表，无论成功还是失败
        fun onSendMessageSuccess()
        fun onSendMessageFailed()
        fun onMessageLoad()
        fun onMoreMessageLoaded(size: Int)
    }

}