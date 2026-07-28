package com.example.multiplatformtests.MusicPlayer.Control

import android.os.Environment
import java.io.File

actual object MediaFileLocator {
    actual fun getTracksDirectory(): String =
        AppContextHolder.context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)!!.absolutePath

    actual fun getClipsDirectory(): String =
        AppContextHolder.context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)!!.absolutePath
}

//actual object MediaFileLocator {
//    actual fun getTracksDirectory(): String =
//        File(Environment.getExternalStorageDirectory(), "TestFolder").absolutePath
//
//    actual fun getClipsDirectory(): String =
//        File(Environment.getExternalStorageDirectory(), "TestFolder").absolutePath
//}