package com.imn.whocalling.util

enum class CallType(val value: String) {
    Incoming("Incoming"),
    Outgoing("Outgoing"),
    OutgoingNotAccepted("Not Answered"),
    Missed("Missed"),
    Rejected("Rejected"),
    Unknown("Unknown"),
}