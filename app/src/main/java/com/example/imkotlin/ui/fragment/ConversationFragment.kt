package com.example.imkotlin.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.imkotlin.R
import com.example.imkotlin.adapter.ConversationListAdapter
import com.example.imkotlin.adapter.EMMessageListenerAdapter
import com.example.imkotlin.ui.activity.GroupPickContactsActivity
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

//会话界面布局
class ConversationFragment: BaseFragment() {

    val conversations = mutableListOf<EMConversation>()

    //注册消息监听来接收消息
    val messageListener = object : EMMessageListenerAdapter(){
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            loadConversations()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_conversation

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.message)
        group_pick.visibility = View.VISIBLE
        group_pick.setOnClickListener { context?.startActivity<GroupPickContactsActivity>() }

        recyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
            adapter = ConversationListAdapter(context, conversations)
        }

        //监听收到消息就刷新会话列表
        EMClient.getInstance().chatManager().addMessageListener(messageListener)

//        loadConversations()   //留给onResume加载一次就好
    }

    //加载会话列表，耗时工作放入子线程,这里应该在presenter层操作的
    private fun loadConversations() {
        doAsync {
            //清空会话列表(在重新加载)
            conversations.clear()
            val allConversations = EMClient.getInstance().chatManager().allConversations
            conversations.addAll(allConversations.values)  //allConversations是map集合，只需拿到values放入mutablelist集合就好
            uiThread { recyclerView.adapter?.notifyDataSetChanged() }
        }
    }

    override fun onResume() {
        super.onResume()
        loadConversations()
    }

    //不需要的时候在activity的onDestroy()移除listener
    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }

}