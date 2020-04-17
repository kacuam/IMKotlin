package com.example.imkotlin.factory

import android.support.v4.app.Fragment
import com.example.imkotlin.R
import com.example.imkotlin.ui.fragment.ContactFragment
import com.example.imkotlin.ui.fragment.ConversationFragment
import com.example.imkotlin.ui.fragment.DynamicFragment

//单例
class FragmentFactory private constructor() {

    val conversations by lazy {
        ConversationFragment()
    }

    val contact by lazy {
        ContactFragment()
    }

    val dynamic by lazy {
        DynamicFragment()
    }

    companion object{
        val instance = FragmentFactory()
    }

    fun getFragment(tabId: Int): Fragment? {
        when(tabId) {
            R.id.tab_conversation -> return conversations
            R.id.tab_contacts -> return  contact
            R.id.tab_dynamic -> return dynamic
        }
        return null
    }
}