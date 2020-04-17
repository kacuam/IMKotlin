package com.example.imkotlin.data

import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobFile

data class User(val nickname: String, val age: Int, val gender: Int ,val avatar: BmobFile) : BmobUser()