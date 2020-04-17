package com.example.imkotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.imkotlin.R
import com.example.imkotlin.data.ContactListItem
import kotlinx.android.synthetic.main.view_contact_item.view.*

//联系人列表条目，初始化AttributeSet = null
class ContactListItemView(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_contact_item,this)
    }

    //ContactPresenter -> ContactFragment -> ContactListAdapter -> ContactListItemView -> ContactListItems => ContactListItem 这里是修改ContactListItemView里的内容
    fun bindView(contactListItem: ContactListItem) {
        if (contactListItem.showFirstLetter){
            firstLetter.visibility = View.VISIBLE
            firstLetter.text = contactListItem.firstLetter.toString()
        } else firstLetter.visibility = View.GONE
        userName.text = contactListItem.userName
    }
}