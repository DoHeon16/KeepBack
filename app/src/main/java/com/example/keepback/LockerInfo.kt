package com.example.keepback

data class LockerInfo (val line:String, val name:String, val location:String, var num:Int, var large:Int, var medium:Int,var small:Int, var control:Int) {
    constructor():this("noinfo","noinfo","noinfo",0,0,0,0,0)
}