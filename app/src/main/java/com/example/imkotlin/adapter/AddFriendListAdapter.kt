package com.example.imkotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.imkotlin.data.AddFriendListItem
import com.example.imkotlin.widget.AddFriendListItemView

//AddFriendPresenter -> AddFriendActivity -> AddFriendListAdtapter -> AddFriendListItemView
class AddFriendListAdapter(val context: Context, val addFriendListItems: MutableList<AddFriendListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return AddFriendListItemViewHolder(AddFriendListItemView(context))
    }

    override fun getItemCount(): Int = addFriendListItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val addFriendListItemView = holder.itemView as AddFriendListItemView  //使用holder通过itemView返回AddFriendListItemView
        addFriendListItemView.bindView(addFriendListItems[position])
    }


    class AddFriendListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}