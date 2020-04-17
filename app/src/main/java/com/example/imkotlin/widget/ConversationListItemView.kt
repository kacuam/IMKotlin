package com.example.imkotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.imkotlin.R
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import kotlinx.android.synthetic.main.view_conversation_item.view.*
import java.util.*

class ConversationListItemView(context: Context?, attrs: AttributeSet?=null) : RelativeLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_conversation_item,this)
    }

    fun bindView(emConversation: EMConversation) {
        //会话人名字
        userName.text = emConversation.conversationId()
        //最后一条消息
        if (emConversation.lastMessage.type == EMMessage.Type.TXT){
            val body = emConversation.lastMessage.body as EMTextMessageBody
            lastMessage.text = body.message
        } else lastMessage.text = context.getString(R.string.no_text_message)
        //最后一条消息时间
        val timestampString = DateUtils.getTimestampString(Date(emConversation.lastMessage.msgTime))
        timestamp.text = timestampString
        //未读消息总数
        if (emConversation.unreadMsgCount > 0){
            unreadCount.visibility = View.VISIBLE
            unreadCount.text = emConversation.unreadMsgCount.toString()
        } else unreadCount.visibility = View.GONE
    }

}