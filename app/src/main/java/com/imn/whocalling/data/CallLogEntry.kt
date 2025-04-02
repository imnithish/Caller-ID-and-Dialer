package com.imn.whocalling.data

data class CallLogEntry(
    val number: String,
    val type: String,
    val date: String,
    val duration: String,
    val savedName: String?,
    val whoCallingName: String?,
    val isSpam: Boolean,
    val location: String?=null
)