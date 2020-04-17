package com.example.imkotlin.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.bmob.v3.util.V
import com.example.imkotlin.R
import com.example.imkotlin.adapter.ContactListAdapter
import com.example.imkotlin.adapter.EMContactListenerAdapter
import com.example.imkotlin.contract.ContactContract
import com.example.imkotlin.presenter.ContactPresenter
import com.example.imkotlin.ui.activity.AddFriendActivity
import com.example.imkotlin.widget.SlideBar
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

//联系人界面的View层
class ContactFragment : BaseFragment() ,ContactContract.View{

    override fun getLayoutResId(): Int = R.layout.fragment_contacts

    val presenter = ContactPresenter(this)

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
        EMClient.getInstance().contactManager().setContactListener(contactListener)  //setContactListener()里通过add方法添加到监听器集合，需要在销毁时解除出去，在onDestroy()方法执行
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
                recyclerView.smoothScrollToPosition(index)//getPosition(firstLetter)
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
            adapter = ContactListAdapter(
                context,
                presenter.contactListItems
            ) //ContactFragment -> ContactListAdapter -> ContactListItemView
                        //(contactListItems)
        }
    }

    private fun initSwipeRreshLayout() {
        //下拉刷新，内联扩展函数apply，在函数范围内，可以任意调用该对象的任意方法，并返回该对象
        swipeRefreshLayout.apply {
            //设置刷新圈的颜色
            setColorSchemeColors(resources.getColor(R.color.qq_blue, null))
            //切换到联系人页面就刷新
            isRefreshing = true
            //设置下拉刷新监听器,加载好友列表
            setOnRefreshListener { presenter.loadContacts() }
        }
    }

    private fun initHeader() {
        headerTitle.text = getString(R.string.contact)
        //在header里的add(ImageView)改为显示
        add.visibility = View.VISIBLE
        add.setOnClickListener { context?.startActivity<AddFriendActivity>() }
    }

    private fun getPosition(firstLetter: String): Int =
        presenter.contactListItems.binarySearch {
            //二分查找下标
            contactListItem -> contactListItem.firstLetter.minus(firstLetter[0]) //firstLetter加入[0]变为Char类型
            //minus是减的意思，这里比如contactListItem.firstLetter=a减firstLetter[0]=c为-2，所以在a+2上找到c返回c的postion
        }

    override fun onLoadContactsSuccess() {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter?.notifyDataSetChanged() //通知recyclerView更新列表，notifyDataSetChanged()是通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容
    }

    override fun onLoadContactsFaild() {
        swipeRefreshLayout.isRefreshing = false
        context?.toast(R.string.load_contacts_failed)
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().contactManager().removeContactListener(contactListener)
    }

}