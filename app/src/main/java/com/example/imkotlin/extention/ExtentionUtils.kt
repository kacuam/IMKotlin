package com.example.imkotlin.extention

//提供一个String类的方法
fun String.isValidUserName(): Boolean = this.matches(Regex("^[a-zA-Z]\\w{2,19}$"))
fun String.isValidPassword(): Boolean = this.matches(Regex("^[0-9]{3,20}$"))

// MutableMap<K,V>类的扩展方法
//将MutableMap转换为可变长的Pair类型的数组,不是直接变为Pair类型
fun <K,V> MutableMap<K,V>.toVarargArray(): Array<Pair<K, V>> =
    map {
        Pair(it.key,it.value)
    }.toTypedArray()
//map{}返回List
//public inline fun <reified T> Collection<T>.toTypedArray(): Array<T>