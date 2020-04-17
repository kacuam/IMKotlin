package com.example.imkotlin.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.imkotlin.R
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import kotlinx.android.synthetic.main.view_add_friend_item.view.timestamp
import kotlinx.android.synthetic.main.view_send_message_item.view.*
import java.util.*

//ChatPresenter -> ChatActivity -> MessageListAdpter -> SendMessageItemView/ReceiveMessageItemView
class SendMessageItemView(context: Context?, attrs: AttributeSet?=null) : RelativeLayout(context, attrs){

    init {
        View.inflate(context,R.layout.view_send_message_item,this)
    }

    //对发送消息内容，时间，刷新进行操作
    fun bindView(emMessage: EMMessage, showTimestamp: Boolean) {
        updateTimestamp(emMessage, showTimestamp)
        updateMessage(emMessage)
        updateProgress(emMessage)
    }

    private fun updateProgress(emMessage: EMMessage) {
        //查看消息发送状态，let可以进行判空处理，用it表示引用对象
        emMessage.status().let {
            when (it) {
                EMMessage.Status.INPROGRESS -> {
                    sendMessageProgress.visibility = View.VISIBLE  //继续显示
                    sendMessageProgress.setImageResource(R.drawable.send_message_progress)  //保证显示图片不变
                    val animationDrawable = sendMessageProgress.drawable as AnimationDrawable //设置动画
                    animationDrawable.start() //打开动画
                }
                EMMessage.Status.SUCCESS -> sendMessageProgress.visibility = View.GONE
                EMMessage.Status.FAIL -> {
                    sendMessageProgress.visibility = View.VISIBLE
                    sendMessageProgress.setImageResource(R.mipmap.msg_error)
                }
            }
        }
    }

    private fun updateMessage(emMessage: EMMessage) {
        //判断是否是文本类型，或者其他类型
        if (emMessage.type == EMMessage.Type.TXT){
            sendMessage.text = (emMessage.body as EMTextMessageBody).message
        } else {
            sendMessage.text = context.getString(R.string.no_text_message)
        }
    }

    private fun updateTimestamp(emMessage: EMMessage, showTimestamp: Boolean) {
        if (showTimestamp){
            timestamp.visibility = View.VISIBLE
            timestamp.text = DateUtils.getTimestampString(Date(emMessage.msgTime)) //使用环信提供的DateUtils
        } else timestamp.visibility = View.GONE
    }

}
