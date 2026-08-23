package com.example.infrastructure.utils

import android.content.Context
import android.net.Uri
import java.io.FileNotFoundException
import com.example.domain.utils.IsFileAlailableByUri

class IsFileAvailableByUri(private var context: Context): IsFileAlailableByUri{
    override operator fun invoke(uri: String): Boolean{
        return if(uri!=null) {
            try {
                context.contentResolver.openInputStream(Uri.parse(uri))?.close()
                true
            } catch (e: FileNotFoundException) {
                false
            }
        } else false
    }
}