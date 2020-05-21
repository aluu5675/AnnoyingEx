package com.andyluu.annoyingex

import android.app.Application
import android.widget.Toast


class AnnoyingExApp: Application() {

    lateinit var messagesManager: MessagesManager
        private set

    var exMessages: List<String> = listOf()

    var message = ""

    override fun onCreate() {
        super.onCreate()

        messagesManager = MessagesManager(this)

        messagesManager.fetchMessages({listOfMessages ->
            exMessages = listOfMessages.messages
        }, {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            message = "unable to retrieve message"
        })
    }
}