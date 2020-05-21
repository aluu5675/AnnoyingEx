package com.andyluu.annoyingex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var exMessages: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var message = ""
        (application as AnnoyingExApp).messagesManager.fetchMessages({listOfMessages ->
            exMessages = listOfMessages.messages
        }, {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            message = "unable to retrieve message"
        })

        btnHereWeGo.setOnClickListener {
            if (!exMessages.isEmpty()) {
                message = exMessages[Random.nextInt(exMessages.size)]
                Log.i("gggggg", message)
            }
            (application as AnnoyingExApp).messagesManager.spamMessages(message)
        }

        btnClosure.setOnClickListener {
            (application as AnnoyingExApp).messagesManager.stopMessages()
        }
    }

}
