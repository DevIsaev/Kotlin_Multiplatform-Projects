package com.example.domain.utils

interface IsFileAlailableByUri {
    operator fun invoke(uri:String): Boolean
}