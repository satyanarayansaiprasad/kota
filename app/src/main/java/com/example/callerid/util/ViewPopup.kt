package com.example.callerid.util

import android.view.View

object ViewPopup {

    var view: View? =  null

    fun set(view: View){
        this.view = view
    }

    fun get(): View? {
        return view
    }
    fun delete(){
        view = null
    }
}