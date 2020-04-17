package com.example.imkotlin.presenter

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.imkotlin.contract.AddFriendContact
import com.example.imkotlin.data.AddFriendListItem
import com.example.imkotlin.data.db.IMDatabase
import com.hyphenate.chat.EMClient
import org.jetbrains.anko.doAsync

//添加联系人presenter层和model层
//(AddFriendListItem => AddFriendListItems) -> AddFriendPresenter -> AddFriendActivity -> AddFriendListAdtapter -> AddFriendListItemView
class AddFriendPresenter(val view: AddFriendContact.View): AddFriendContact.Presenter {

    val addFriendListItems = mutableListOf<AddFriendListItem>()  //创建AddFriendItem的集合addFriendListItems

    //用户查询
    override fun search(key: String) {
        val query = BmobQuery<BmobUser>()
        query.addWhereContains("username",key)  //查询username字段的值含有key(字符串)的数据
            .addWhereNotEqualTo("username",EMClient.getInstance().currentUser)  //排除自己
        query.findObjects(object : FindListener<BmobUser>(){
            override fun done(p0: MutableList<BmobUser>?, p1: BmobException?) {
                if (p1 == null) {
                    //处理耗时数据
                    //创建AddFriendItem的集合
                    //获取联系人的数据/获取好友列表
                    val allContact = IMDatabase.instance.getAllContact()
                    doAsync {
                        p0?.forEach{
                            //比对是否已经添加过好友
                            var isAdded = false
                            for (contact in allContact) {
                                if (contact.name == it.username){
                                    isAdded = true
                                }
                            }

                            val addFriendItem = AddFriendListItem(it.username, it.createdAt, isAdded) //用户名，创建时间，是否添加过
                            addFriendListItems.add(addFriendItem)
                        }
                        uiThread { view.onSearchSuccess() }  //回主线程通知
                    }

                }
                else view.onSearchFailed()
            }
        })
    }

}
