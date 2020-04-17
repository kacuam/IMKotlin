package com.example.imkotlin.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.imkotlin.app.IMApplication
import org.jetbrains.anko.db.*

//数据库保存联系人
//1、创建联系人表
//2、创建DatabaseHelper: ManagedSQLiteOpenHelper -> SQLiteDatabase
class DatabaseHelper(ctx: Context = IMApplication.instance) : ManagedSQLiteOpenHelper(ctx, NAME,null, VERSION) {

    companion object {
        val NAME = "im.db"
        val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(ContactTable.NAME,true,  //true-如果不存在就执行
            ContactTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT, ///属性:数据类型，主键，自增
            ContactTable.CONTACT to TEXT)  //属性:文本
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(ContactTable.NAME,true)  //true-如果存在就执行
        onCreate(db)
    }

}