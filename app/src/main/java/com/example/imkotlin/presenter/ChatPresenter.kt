package com.example.imkotlin.presenter

import com.example.imkotlin.adapter.EMCallBackAdapter
import com.example.imkotlin.contract.ChatContract
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import org.jetbrains.anko.doAsync

//聊天界面Presenter层,Model层
//ChatPresenter -> ChatActivity -> MessageListAdpter -> SendMessageItemView/ReceiveMessageItemView
class ChatPresenter(val view: ChatContract.View) : ChatContract.Presenter {

    companion object{
        val PAGE_SIZE = 10
    }

    val messages = mutableListOf<EMMessage>()

    //发送消息
    override fun sendMessage(contact: String, message: String) {
        //创建一条文本消息，第一个参数为消息文字内容，第二个参数为对方用户或者群聊的id
        val emMessage = EMMessage.createTxtSendMessage(message,contact)
        //监听消息状态
        //通过 emMessage 设置消息的发送及接收状态。 注意：需在sendMessage之前去设置此回调监听
        emMessage.setMessageStatusCallback(object : EMCallBackAdapter(){
            override fun onSuccess() {
                uiThread { view.onSendMessageSuccess() }
            }

            override fun onError(p0: Int, p1: String?) {
                uiThread { view.onSendMessageFailed() }
            }
        })
        //消息添加到消息集合中
        messages.add(emMessage)
        //通知view层已发送
        view.onStartSendMessage()
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(emMessage)
    }

    //接收消息
    override fun addMessage(username: String, p0: MutableList<EMMessage>?) {
        //加入当前的消息列表
        p0?.let { messages.addAll(it) }
        //更新消息为已读消息
        //获取跟联系人的会话，然后标记会话里面的消息为全部已读
        val conversation = EMClient.getInstance().chatManager().getConversation(username)  //获取会话未读消息对象
        conversation.markAllMessagesAsRead()  ///指定会话消息未读数清零
    }

    //加载聊天记录
    // 因为加载方法是同步方法，需要子线程操作，SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
    override fun loadMessages(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            //将加载的消息标记为已读
            conversation.markAllMessagesAsRead()
            messages.addAll(conversation.allMessages)  //获取此会话的所有聊天记录，加入到messages集合中
            uiThread { view.onMessageLoad() }
        }
    }

    //加载更多聊天记录
    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
    override fun loadMoreMessage(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            val startMsgId = messages[0].msgId  //获取消息集合的第一条消息的id
            //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
            val loadMoreMsgFromDB = conversation.loadMoreMsgFromDB(startMsgId, PAGE_SIZE)  //获取startMsgId之前的pagesize条消息
            messages.addAll(0,loadMoreMsgFromDB)  //从第一条的位置加入
            uiThread { view.onMoreMessageLoaded(loadMoreMsgFromDB.size) }  //提供loadMoreMsgFromDB.size的长度，方便view层的第一条消息显示在这长度的位置
        }
    }
}