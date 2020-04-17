package com.example.imkotlin.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.imkotlin.R
import com.example.imkotlin.adapter.EMMessageListenerAdapter
import com.example.imkotlin.adapter.MessageListAdpter
import com.example.imkotlin.contract.ChatContract
import com.example.imkotlin.presenter.ChatPresenter
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

//聊天界面View层
//ChatPresenter -> ChatActivity -> MessageListAdpter -> SendMessageItemView/ReceiveMessageItemView
class ChatActivity: BaseActivity(), ChatContract.View {

    override fun getLayoutResId(): Int = R.layout.activity_chat

    val presenter = ChatPresenter(this)
    lateinit var username : String

    //消息监听器（不需要的时候移除listener，如在activity的onDestroy()时）
    val msgListener = object : EMMessageListenerAdapter() {

        //收到消息,onMessageReceived是在子线程回调
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            //消息加入presenter里放入消息集合,只要有消息，就说明接收成功了，已放入集合
            presenter.addMessage(username, p0)
            //监听到消息就刷新列表
            runOnUiThread{
                recyclerView.adapter?.notifyDataSetChanged()
                //收消息后滚到底部
                scrollToBottom()
            }
        }
    }

    override fun init() {
        super.init()
        initHeader()
        initEditText()
        initRecyclerView()
        //发送消息可以不在主线程，但接收消息有随时的监听器，需要在主线程
        //接收消息，通过注册消息监听来接收消息
        EMClient.getInstance().chatManager().addMessageListener(msgListener)
        send.setOnClickListener { send() }
        //加载聊天记录
        presenter.loadMessages(username)
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MessageListAdpter(context,presenter.messages)
            //添加滚动监听器
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    //当RecyclerView是一个空闲的状态，检查是否滑倒顶部，要加载更多数据，SCROLL_STATE_IDLE就是空闲状态
                    if (newState == RecyclerView.SCROLL_STATE_IDLE){
                        //如果第一个可见条目的位置为0，就是滑动顶部，使用LinearLayoutManager观察第一个条目的position位置
                        val linearLayoutManager = layoutManager as LinearLayoutManager
                        if (linearLayoutManager.findFirstVisibleItemPosition() == 0){
                            //加载更多数据
                            presenter.loadMoreMessage(username)
                        }
                    }
                }
            })
        }
    }

    fun send(){
        hideSoftKeyboard()
        val message = edit.text.trim().toString()
        presenter.sendMessage(username,message)
    }

    private fun initEditText() {
        edit.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                //如果用户输入的文本长度大于0，发送按钮enable
                send.isEnabled = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        //在软键盘里发送
        edit.setOnEditorActionListener { v, actionId, event ->
            send()
            true
        }
    }

    private fun initHeader() {
        //显示返回按钮
        back.visibility = View.VISIBLE
        back.setOnClickListener { finish() }

        //contactListItemView.setOnClickListener{context.startActivity<ChatActivity>("username" to userName)} //带参数跳转 key to value
        //获取聊天用户名
        username = intent.getStringExtra("username")
        val titleString = String.format(getString(R.string.chat_title),username)
        headerTitle.text = titleString
    }

    override fun onStartSendMessage() {
        //通知RecyclerView刷新列表
        recyclerView.adapter?.notifyDataSetChanged()  //通知recyclerView更新列表,刷新界面
    }

    override fun onSendMessageSuccess() {
        recyclerView.adapter?.notifyDataSetChanged()
        toast(R.string.send_message_success)
        //清空编辑框
        edit.text.clear()
        //发消息后滚到底部
        scrollToBottom()
    }

    private fun scrollToBottom() {
        recyclerView.scrollToPosition(presenter.messages.size - 1)
    }

    override fun onSendMessageFailed() {
        toast(R.string.send_message_failed)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onMessageLoad() {
        recyclerView.adapter?.notifyDataSetChanged()
        scrollToBottom()
    }

    override fun onMoreMessageLoaded(size: Int) {
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scrollToPosition(size)  //滚到第一条消息显示在这长度的位置
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(msgListener)
    }

}
