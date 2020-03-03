package com.example.audition

class ChatMessage {
    var id: String? = null
    var user: String? = null
    var color: Int = 0
    var msg: String? = null

    // empty constructor used for getting object from database
    constructor() {}

    constructor(id: String, user: String, color: Int, msg: String) {
        this.id = id
        this.user = user
        this.color = color
        this.msg = msg
    }

    fun readUser(): String {
        return user.toString()
    }

    fun readMessage(): String {
        return msg.toString()
    }

}