package com.example.testmusicplayercompose.utils

class SingleEvent <out T>(private var content:T){
    private var hasBeenHandled=false
    fun getContentNotHandled(): T?{ return if(hasBeenHandled) null else{ hasBeenHandled=true
        content }
    }
}