package com.example.instargram

data class HomeData(
    var comments:MutableList<Any> = mutableListOf(),
    var description:String = "",
    var image:String = "",
    var likes:String? = null,
    var name:String = ""


)
