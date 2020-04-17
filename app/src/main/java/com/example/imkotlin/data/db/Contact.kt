package com.example.imkotlin.data.db

//联系人实体类：委托数据类
data class Contact(val map: MutableMap<String, Any?>){
    val _id by map
    val name by map  //委托map处理提供name值
}
//Contact.map
//Contact._id
//Contact.name
