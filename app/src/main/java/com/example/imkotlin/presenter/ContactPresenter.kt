package com.example.imkotlin.presenter

import com.example.imkotlin.contract.ContactContract
import com.example.imkotlin.data.ContactListItem
import com.example.imkotlin.data.db.Contact
import com.example.imkotlin.data.db.IMDatabase
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import org.jetbrains.anko.doAsync

//(ContactListItem => ContactListItems) -> ContactPresenter -> ContactFragment -> ContactListAdapter -> ContactListItemView
//联系人界面Presenter层和Model层
class ContactPresenter(val view: ContactContract.View) : ContactContract.Presenter {

    val contactListItems = mutableListOf<ContactListItem>()

    //Model层
    override fun loadContacts() {
        doAsync {
            //再次加载数据，先清空集合
            contactListItems.clear()
            //清空数据库
            IMDatabase.instance.delectAllContact()
            try {
                //获取好友列表,这里是同步方法
                val usernames = EMClient.getInstance().contactManager().allContactsFromServer
                //根据首字符排序
                usernames.sortBy { it[0] }
                usernames.forEachIndexed { index, it ->
                    //是否显示首字符
                    val showFirstLetter = index == 0 || it[0] != usernames[index-1][0] //如果是第一条item或者当前首字母不相同于前一个item的首字母，就返回true
                    val contactListItem = ContactListItem(it,it[0].toUpperCase(),showFirstLetter)
                    contactListItems.add(contactListItem)  //ContactPresenter -> ContactFragment -> ContactListAdapter -> ContactListItemView
                                                                        //(contactListItems)
                    //创建联系人，保存到数据库
                    val contract = Contact(mutableMapOf("name" to it))  //保存键值对
                    IMDatabase.instance.saveContact(contract)
                }
                //回到主线程通知
                uiThread { view.onLoadContactsSuccess() }
            } catch (e: HyphenateException) {
                uiThread { view.onLoadContactsFaild() }
            }
        }

    }

}