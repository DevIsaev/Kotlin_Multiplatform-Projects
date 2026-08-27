package com.example.testmusicplayercompose.logger

import android.util.Log
import com.example.domain.logger.Logger

class AndroidLogger: Logger {
    companion object{
        const val TAG="TAG"
    }


    override fun log(message: String) {
        Log.e(TAG,message)
    }
}