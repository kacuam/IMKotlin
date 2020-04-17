package com.example.imkotlin.data.db

//1、定义lContactTable类
//2、创建了ContactTable的一个实例，通过类名，直接访问实例，实现单例的一种方式
//在Kotlin中，单例模式的实现只需要一个object关键字即可,直接声明object对象,其内部不允许声明构造方法,调用方式：类名.方法名()
object ContactTable{
    val NAME = "contact"  //表名

    //定义字段
    val ID = "_id"
    val CONTACT = "name"
}

//数据库保存联系人
//1、创建联系人表
//2、创建DatabaseHelper: SQLiteOpenHelper -> SQLiteDatabase