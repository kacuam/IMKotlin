package com.example.imkotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.imkotlin.R
import com.example.imkotlin.adapter.EMCallBackAdapter
import com.example.imkotlin.data.AddFriendListItem
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.view_add_friend_item.view.*
import kotlinx.android.synthetic.main.view_contact_item.view.userName
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

//添加好友列表
class AddFriendListItemView(context: Context?, attrs: AttributeSet?=null) : RelativeLayout(context, attrs){

    init {
        View.inflate(context, R.layout.view_add_friend_item,this)  //this是当前view布局
    }

    fun bindView(addFriendListItem: AddFriendListItem) {
        //是否添加好友判断
        if (addFriendListItem.isAdded) {
            //添加过了按键不能用
            add.isEnabled = false
            add.text = context.getString(R.string.already_added)
        } else {
            add.isEnabled = true
            add.text = context.getString(R.string.add)
        }
        userName.text = addFriendListItem.userName
        timestamp.text = addFriendListItem.timestamp

        add.setOnClickListener { addFreind(addFriendListItem.userName) }
    }

    //发送添加好友请求
    private fun addFreind(userName: String) {
        //参数为要添加的好友的username，添加理由，回调，aysncAddContact()是异步添加(async)
        EMClient.getInstance().contactManager().aysncAddContact(userName, null, object : EMCallBackAdapter(){
            override fun onSuccess() {
                context.runOnUiThread { toast(R.string.send_add_friend_success) }
            }

            override fun onError(p0: Int, p1: String?) {
                context.runOnUiThread { toast(R.string.send_add_friend_failed) }
            }
        })
    }
 
}