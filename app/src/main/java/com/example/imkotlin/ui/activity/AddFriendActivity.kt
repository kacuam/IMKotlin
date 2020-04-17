package com.example.imkotlin.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.imkotlin.R
import com.example.imkotlin.adapter.AddFriendListAdapter
import com.example.imkotlin.contract.AddFriendContact
import com.example.imkotlin.presenter.AddFriendPresenter
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

//添加好友界面
class AddFriendActivity : BaseActivity() ,AddFriendContact.View{

    val presenter = AddFriendPresenter(this)

    override fun getLayoutResId(): Int = R.layout.activity_add_friend

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.add_friend)

        recyclerView.apply {
            setHasFixedSize(true)  //设置固定大小
            layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
            adapter = AddFriendListAdapter(context,presenter.addFriendListItems)
            //AddFriendPresenter -> AddFriendActivity -> AddFriendListAdtapter -> AddFriendListItemView
        }

        search.setOnClickListener { search() }
        userName.setOnEditorActionListener { v, actionId, event ->
            search()
            true
            //true表示消费此事件，就是要用这个监听器就给true
        }
    }

    private fun search(){
        hideSoftKeyboard()
        showProgress(getString(R.string.searching))
        //清空搜索联系人列表
        //recyclerView.adapter?.notifyItemRangeRemoved(0, recyclerView.adapter!!.itemCount)
        val key = userName.text.trim().toString()
        presenter.search(key)
    }

    override fun onSearchSuccess() {
        dismissProgress()
        toast(R.string.search_success)
        recyclerView.adapter?.notifyDataSetChanged()  //通知recyclerView更新列表,刷新界面
    }

    override fun onSearchFailed() {
        dismissProgress()
        toast(R.string.search_failed)
    }

}