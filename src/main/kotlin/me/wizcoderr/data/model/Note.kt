package me.wizcoderr.data.model

import java.util.*

data class Note(
    val id:String=Math.random().toString(),
    val noteTitle:String,
    val noteDescription:String,
    val date:Long= Date().time
)
