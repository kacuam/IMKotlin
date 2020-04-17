package com.example.imkotlin.ui.fragment

import com.example.imkotlin.R
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.fragment_dynamic.*
import kotlinx.android.synthetic.main.header.*
import com.hyphenate.EMCallBack
import com.example.imkotlin.adapter.EMCallBackAdapter
import com.example.imkotlin.ui.activity.LoginActivity
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class DynamicFragment : BaseFragment() {

    override fun getLayoutResId(): Int = R.layout.fragment_dynamic

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.dynamic)
        //退出账户名字更改为当前
        val logoutString = String.format(getString(R.string.logout),EMClient.getInstance().currentUser) //val s = String.format("hi,%s","加藤鹰"),s = "hi,加藤鹰",这里是字符类型格式化,%s 字符串类型
        logout.text = logoutString

        logout.setOnClickListener{logout()}
    }
    //这里应该在modle层
    //环信退出登录, EMCallBack()使用自定义的EMCallBackAdapter()
    private fun logout() {
        EMClient.getInstance().logout(true, object : EMCallBackAdapter() {

            override fun onSuccess() {
                //切换到主线程
                context?.runOnUiThread {
                    toast(R.string.logout_success)
                    startActivity<LoginActivity>()
                    activity?.finish()
                }

            }

            override fun onError(code: Int, message: String?) {
                context?.runOnUiThread { toast(R.string.logout_failed) }

            }
        })
    }

}