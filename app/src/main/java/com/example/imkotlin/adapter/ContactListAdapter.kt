package com.example.imkotlin.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.imkotlin.R
import com.example.imkotlin.data.ContactListItem
import com.example.imkotlin.ui.activity.ChatActivity
import com.example.imkotlin.widget.ContactListItemView
import com.hyphenate.chat.EMClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class ContactListAdapter(val context: Context, val contactListItems: MutableList<ContactListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ContactListItemViewHolder(ContactListItemView(context))  //传入ContactListItemView的holder
    }

    override fun getItemCount(): Int = contactListItems.size
    //View层
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contactListItemView = holder.itemView as ContactListItemView //使用holder通过itemView返回ContactListItemView
        contactListItemView.bindView(contactListItems[position])  //通过position传入每个contactListItem
        //ContactListAdapter -> ContactListItemView
                    //(contactListItems)

        //点击联系人跳转到聊天界面
        val userName = contactListItems[position].userName
        contactListItemView.setOnClickListener{context.startActivity<ChatActivity>("username" to userName)} //带参数跳转 key to value
        //长按弹出AlertDialog对话框删除好友
        contactListItemView.setOnLongClickListener {
            val message = String.format(context.getString(R.string.delete_friend_message),userName)  //是否与%s友尽？
            AlertDialog.Builder(context)
                .setTitle(R.string.delete_friend_title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.confirm,DialogInterface.OnClickListener { dialog, which ->
                    deleteFriend(userName)
                })
                .show()
            true
        }
    }

    //Model层
    private fun deleteFriend(userName: String) {
        //doAsync { EMClient.getInstance().contactManager().deleteContact(userName) }
        EMClient.getInstance().contactManager().aysncDeleteContact(userName, object : EMCallBackAdapter(){  //Alt+Insert
            override fun onSuccess() {
               context.runOnUiThread { toast(R.string.delete_friend_success) }
            }

            override fun onError(p0: Int, p1: String?) {
                context.runOnUiThread { toast(R.string.delete_friend_failed) }
            }
        })
    }

    class ContactListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}