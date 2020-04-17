package com.example.imkotlin.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.example.imkotlin.R
import com.example.imkotlin.contract.LoginContract
import com.example.imkotlin.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity: BaseActivity(),LoginContract.View{

    val presenter = LoginPresenter(this)

    override fun init() {
        super.init()
        //登录按钮登录
        login.setOnClickListener { login() }
        //键盘按钮登录
        password.setOnEditorActionListener { v, actionId, event ->
            login()
            true
        }
        //新用户注册
        newUser.setOnClickListener{startActivity<RegisterActivity>()}
    }

    private fun login(){
        //隐藏软键盘
        hideSoftKeyboard()
        //动态申请环信数据库权限
        if (hasWriteExternalStoragePermission()) {
            val userNameString = userName.text.trim().toString()
            val passwordString = password.text.trim().toString()
            presenter.login(userNameString, passwordString)
        } else applyWriteExteranlStoragePermission()
    }

    //检查是否有写磁盘的权限
    private fun hasWriteExternalStoragePermission(): Boolean {
        val result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    //添加写磁盘的权限
    private fun applyWriteExteranlStoragePermission() {
        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this,permission,0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //用户同意权限，开始登录(回调login())
            login()
        } else toast(R.string.permission_denied)
    }

    override fun getLayoutResId(): Int = R.layout.activity_login

    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun onPasswordError() {
        password.error = getString(R.string.password_error)
    }

    override fun onStartLogin() {
        //弹出进度条
        showProgress(getString(R.string.logging))
    }

    override fun onLoggedInSuccess() {
        //隐藏进度条
        dismissProgress()
        //进入主界面
        startActivity<MainActivity>()
        //退出LoginActivity
        finish()
    }

    override fun onLoggedInFailed() {
        //隐藏进度条
        dismissProgress()
        //弹出toast
        toast(R.string.login_failed)
    }

}