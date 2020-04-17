package com.example.imkotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.imkotlin.R
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import kotlinx.android.synthetic.main.view_receive_message_item.view.*
import java.util.*

//ChatPresenter -> ChatActivity -> MessageListAdpter -> SendMessageItemView/ReceiveMessageItemView
class ReceiveMessageItemView(context: Context?, attrs: AttributeSet?=null) : RelativeLayout(context, attrs){

    init {
        View.inflate(context, R.layout.view_receive_message_item,this)
    }

    //接收消息的内容时间刷新
    fun bindView(emMessage: EMMessage, showTimestamp: Boolean) {
        updateMessage(emMessage)
        updateTimestamp(emMessage, showTimestamp)
    }

    private fun updateMessage(emMessage: EMMessage) {
        //判断是否是文本类型，或者其他类型
        if (emMessage.type == EMMessage.Type.TXT){
            receiveMessage.text = (emMessage.body as EMTextMessageBody).message
        } else {
            receiveMessage.text = context.getString(R.string.no_text_message)
        }
    }

    private fun updateTimestamp(emMessage: EMMessage, showTimestamp: Boolean) {
        if (showTimestamp){
            timestamp.visibility = View.VISIBLE
            timestamp.text = DateUtils.getTimestampString(Date(emMessage.msgTime)) //使用环信提供的DateUtils
        } else timestamp.visibility = View.GONE
    }

}