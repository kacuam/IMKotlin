package com.example.imkotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment :Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(getLayoutResId(),null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    open fun init(){}

    abstract fun getLayoutResId(): Int
}