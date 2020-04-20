package com.example.imkotlin.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.imkotlin.R
import com.example.imkotlin.data.ContactListItem
import com.example.imkotlin.widget.GroupContactListItemView
import com.hyphenate.chat.EMClient
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class GroupPickContactsAdapter(val context: Context, val contactListItems: MutableList<ContactListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ContactListItemViewHolder(GroupContactListItemView(context))  //传入ContactListItemView的holder
    }

    override fun getItemCount(): Int = contactListItems.size
    //View层
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val groupContactListItemView = holder.itemView as GroupContactListItemView//使用holder通过itemView返回ContactListItemView
        groupContactListItemView.bindView(contactListItems[position])  //通过position传入每个contactListItem
        //点击联系人跳转到聊天界面
        //val userName = contactListItems[position].userName
        //groupContactListItemView.setOnClickListener{context.startActivity<ChatActivity>("username" to userName)} //带参数跳转 key to value
    }

    class ContactListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}