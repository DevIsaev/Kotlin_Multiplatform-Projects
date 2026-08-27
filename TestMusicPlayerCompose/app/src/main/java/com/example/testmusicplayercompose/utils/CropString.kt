package com.example.testmusicplayercompose.utils



class CropString {
    companion object{
        operator fun invoke(string: String,length: Int):String{
            return if(string.length>length) string.substring(0,length)+"..." else string
        }
    }
}