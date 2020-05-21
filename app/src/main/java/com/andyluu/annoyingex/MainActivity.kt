package com.andyluu.annoyingex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exApp = (application as AnnoyingExApp)

        btnHereWeGo.setOnClickListener {
            if (!exApp.exMessages.isEmpty()) {
                exApp.message = exApp.exMessages[Random.nextInt(exApp.exMessages.size)]
            }
            (application as AnnoyingExApp).messagesManager.spamMessages(exApp.message)
        }

        btnClosure.setOnClickListener {
            exApp.messagesManager.stopMessages()
        }

        textMessage.text = exApp.message
    }

}
