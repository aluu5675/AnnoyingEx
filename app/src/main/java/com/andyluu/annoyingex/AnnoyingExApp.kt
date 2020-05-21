package com.andyluu.annoyingex

import android.app.Application


class AnnoyingExApp: Application() {

    lateinit var messagesManager: MessagesManager
        private set

    override fun onCreate() {
        super.onCreate()

        messagesManager = MessagesManager(this)
    }
}