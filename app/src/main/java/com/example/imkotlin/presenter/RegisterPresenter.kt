package com.example.imkotlin.presenter

import android.support.design.widget.Snackbar
import cn.bmob.v3.BmobUser
import com.example.imkotlin.contract.RegisterContract
import com.example.imkotlin.extention.isValidPassword
import com.example.imkotlin.extention.isValidUserName
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.imkotlin.data.User
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class RegisterPresenter(val view: RegisterContract.View) : RegisterContract.Presenter {

    override fun register(userName: String, password: String, confirmPassword: String) {
        //检查用户名
        if (userName.isValidUserName()){
            //检查密码
            if (password.isValidPassword()){
                //检查确认密码
                if (password.equals(confirmPassword)){
                    //密码和确认密码一致
                    view.onStartRegister()
                    //开始注册
                    registerBmob(userName,password)
                } else view.onConfirmPasswordError()
            } else view.onPasswordError()
        } else view.onUserNameError()
    }

    private fun registerBmob(userName: String, password: String) {
        val user = BmobUser()  //userName和password直接放入BmobUser()，User表是本地数据库
        user.username = userName
        user.setPassword(password)
        user.signUp(object : SaveListener<BmobUser>() {
            override fun done(user: BmobUser?, e: BmobException?) {
                if (e == null) {
//                    Snackbar.make(, "注册成功", Snackbar.LENGTH_LONG).show()
                    //注册成功
                    //注册到环信
                    registerEaseMob(userName,password)
                } else {
//                    Snackbar.make(view, "尚未失败：" + e.message, Snackbar.LENGTH_LONG).show()
                    //注册失败
                    //202	username '%s' already taken.	用户名已经存在
                    if (e.errorCode == 202) view.onUserExist()
                    else view.onRegisterFailed()
                }
            }
        })
    }

    private fun registerEaseMob(userName: String,password: String) {
        doAsync {
            try {
                //注册失败会抛出HyphenateException
                EMClient.getInstance().createAccount(userName, password)//同步方法不能主线程操作
                //注册成功
                uiThread { view.onRegisterSuccess() } //需要回到主线程通知view层，可调用doAsync里的uiThread方法
            } catch (e: HyphenateException){
                //注册失败
                uiThread { view.onRegisterFailed() }
            }

        }

    }


}