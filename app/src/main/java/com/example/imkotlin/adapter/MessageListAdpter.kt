package com.example.imkotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.imkotlin.widget.ReceiveMessageItemView
import com.example.imkotlin.widget.SendMessageItemView
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils

//ChatPresenter -> ChatActivity -> MessageListAdpter -> SendMessageItemView/ReceiveMessageItemView
class MessageListAdpter(val context: Context, val messages: List<EMMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val ITEM_TYPE_SEND_MESSAGE = 0
        val ITEM_TYPE_RECEIVE_MESSAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].direct() == EMMessage.Direct.SEND){  //direct()是EMMessage提供分辨消息类型
            return ITEM_TYPE_SEND_MESSAGE
        } else {
            return ITEM_TYPE_RECEIVE_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_SEND_MESSAGE) {
            SendMessageViewHolder(SendMessageItemView(context))
        } else ReceiveMessageViewHolder(ReceiveMessageItemView(context))
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //消息时间间隔是否显示，因为需要position，所以在这里操作
        val showTimestamp = isShowTimestamp(position)
        if (getItemViewType(position) == ITEM_TYPE_SEND_MESSAGE){
            val sendMessageItemView = holder.itemView as SendMessageItemView
            sendMessageItemView.bindView(messages[position], showTimestamp)
        } else {
            val receiveMessageItemView = holder.itemView as ReceiveMessageItemView
            receiveMessageItemView.bindView(messages[position], showTimestamp)
        }
    }

    private fun isShowTimestamp(position: Int): Boolean {
        //如果是第一条消息或者前一条消息间隔时间比较长就显示,返回ture
        var showTimestamp = true
        if (position>0) {
            showTimestamp = !DateUtils.isCloseEnough(messages[position].msgTime, messages[position-1].msgTime) //目前消息比较上一条消息的时间间隔
        }
        return showTimestamp
    }

    class SendMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ReceiveMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}