package com.example.imkotlin.ui.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.imkotlin.R
import com.example.imkotlin.adapter.EMMessageListenerAdapter
import com.example.imkotlin.factory.FragmentFactory
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            runOnUiThread { updateBottomBarUnReadCount() }  //监听器在子线程
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun init() {
        super.init()
        bottomBar.setOnTabSelectListener { tabId ->
            val beginTransaction = supportFragmentManager.beginTransaction()
            //使用内联扩展函数let可以判空执行，返回值是函数里面最后一行，或者指定return
            FragmentFactory.instance.getFragment(tabId)?.let {
                beginTransaction.replace(
                    R.id.fragment_frame,
                    it
                )
            } //beginTransaction.replace(R.id.fragment_frame, FragmentFactory.instance.getFragment(tabId))
            beginTransaction.commit()
        }

        EMClient.getInstance().chatManager().addMessageListener(messageListener)
        //注册连接状态监听
        EMClient.getInstance().addConnectionListener(object : EMConnectionListener {
            override fun onConnected() {
                //连接成功的
            }

            override fun onDisconnected(p0: Int) {
                //连接出错,下面的是显示帐号在其他设备登录
                if (p0 == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    //发生多设备登录，跳转到登录界面
                    startActivity<LoginActivity>()
                    toast(getString(R.string.relogin))
                    finish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        updateBottomBarUnReadCount()
    }

    private fun updateBottomBarUnReadCount() {
        //初始化bottombar未读消息的个数
        val tab = bottomBar.getTabWithId(R.id.tab_conversation)  //获取bottombar的tab
        tab.setBadgeCount(EMClient.getInstance().chatManager().unreadMessageCount)  //unreadMessageCount会遍历所有会话，所有未读消息加起来，未读消息总数自动显示
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }

}
