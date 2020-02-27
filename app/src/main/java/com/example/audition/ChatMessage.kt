package com.example.audition

class ChatMessage {
    var message: String? = null
    var username: String? = null
    var id: String? = null

    constructor() {

    }

    constructor(text: String, name: String, identifier: String) {
        this.message = text
        this.username = name
        this.id = identifier
    }

    fun readMessage(): String {
        return message.toString()
    }

    fun readUser(): String {
        return username.toString()
    }

}