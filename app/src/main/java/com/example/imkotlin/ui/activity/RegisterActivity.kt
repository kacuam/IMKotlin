package com.example.imkotlin.ui.activity

import com.example.imkotlin.R
import com.example.imkotlin.contract.RegisterContract
import com.example.imkotlin.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.userName
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast

class RegisterActivity: BaseActivity(),RegisterContract.View {

    val presenter = RegisterPresenter(this)

    override fun init() {
        super.init()
        //注册按钮和确认密码软键盘进入注册流程
        register.setOnClickListener{register()}
        confirmPassword.setOnEditorActionListener { v, actionId, event ->
            register()
            true
        }
    }

    fun register(){
        //隐藏软键盘
        hideSoftKeyboard()
        val userNameString = userName.text.trim().toString()
        val passwordString = password.text.trim().toString()
        val confirmPasswordString = confirmPassword.text.trim().toString()
        presenter.register(userNameString,passwordString,confirmPasswordString)
    }

    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun onPasswordError() {
        password.error = getString(R.string.password_error)
    }

    override fun onConfirmPasswordError() {
        confirmPassword.error = getString(R.string.confirm_password_error)
    }

    override fun onStartRegister() {
        showProgress(getString(R.string.registering))
    }

    override fun onRegisterSuccess() {
        dismissProgress()
        toast(R.string.register_success)
        finish()
    }

    override fun onRegisterFailed() {
        dismissProgress()
        toast(R.string.register_failed)
    }

    override fun onUserExist() {
        dismissProgress()
        toast(R.string.user_already_exist)
    }

    override fun getLayoutResId(): Int = R.layout.activity_register

}