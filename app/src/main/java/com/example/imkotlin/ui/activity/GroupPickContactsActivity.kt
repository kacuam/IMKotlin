package com.example.imkotlin.ui.activity

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.imkotlin.R
import com.example.imkotlin.adapter.EMContactListenerAdapter
import com.example.imkotlin.adapter.GroupPickContactsAdapter
import com.example.imkotlin.contract.GroupPickContactsContract
import com.example.imkotlin.presenter.GroupPickContactsPresenter
import com.example.imkotlin.widget.SlideBar
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.activity_group_pick_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

@SuppressLint("Registered")
class GroupPickContactsActivity : BaseActivity(),GroupPickContactsContract.View{

    override fun getLayoutResId(): Int = R.layout.activity_group_pick_contacts

    val presenter = GroupPickContactsPresenter(this)

    val contactListener = object : EMContactListenerAdapter(){

        override fun onContactDeleted(p0: String?) {
            //被删除时回调此方法,重新获取联系人数据
            presenter.loadContacts()
        }

        override fun onContactAdded(p0: String?) {
            //重新获取联系人数据
            presenter.loadContacts()
        }
    }

    override fun init() {
        super.init()
        initHeader()
        initSwipeRreshLayout()
        initRecyclerView()
        //监听好友状态事件,这里不应该是在model层通过主线程回调
        EMClient.getInstance().contactManager().setContactListener(contactListener)
        initSlideBar()
        //加载好友列表
        presenter.loadContacts()
    }

    private fun initSlideBar() {
        //slidebar监听到就返回onSectionChange和onSlideFinish方法
        slideBar.onSectionChangeListener = object : SlideBar.OnSectionChangeListener {

            override fun onSectionChange(firstLetter: String, index: Int) {
                //设置显示TextView的图片可见和字母
                section.visibility = View.VISIBLE
                section.text = firstLetter
                //RecycleView跟随滚动，在可视范围内position对应的是recyclerView里的每个contactListItem
                recyclerView.smoothScrollToPosition(index)
            }

            override fun onSlideFinish() {
                //设置TextView不可见
                section.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true) //确保尺寸是通过用户输入,RecyclerView的尺寸为一个常数,避免尺寸改变浪费性能资源
            layoutManager = LinearLayoutManager(context)
            adapter = GroupPickContactsAdapter(
                context,
                presenter.contactListItems
            )
        }
    }

    private fun initSwipeRreshLayout() {
        //下拉刷新，内联扩展函数apply，在函数范围内，可以任意调用该对象的任意方法，并返回该对象
        swipeRefreshLayout.apply {
            //设置刷新圈的颜色
            setColorSchemeColors(resources.getColor(R.color.btn_green, null))
            //切换到联系人页面就刷新
            isRefreshing = true
            //设置下拉刷新监听器,加载好友列表
            setOnRefreshListener { presenter.loadContacts() }
        }
    }

    private fun initHeader() {
        headerTitle.text = getString(R.string.contact)
        confirm.visibility = View.VISIBLE
//        confirm.setOnClickListener { startActivity<AddFriendActivity>() }
        back.visibility = View.VISIBLE
        back.setOnClickListener { finish() }
    }

    override fun onLoadContactsSuccess() {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onLoadContactsFaild() {
        swipeRefreshLayout.isRefreshing = false
        toast(R.string.load_contacts_failed)
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().contactManager().removeContactListener(contactListener)
    }


}