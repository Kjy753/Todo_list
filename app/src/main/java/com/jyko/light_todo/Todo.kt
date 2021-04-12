package com.jyko.light_todo


data class Todo(
    val text:String,
    val input_date:String,
    var isDone:Boolean = false
    )