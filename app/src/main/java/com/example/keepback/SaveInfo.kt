package com.example.keepback

data class SaveInfo(val size:String, val password:String, val trouble:Boolean) {
    constructor():this("noinfo","noinfo",false)
}