package com.andyluu.annoyingex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.WorkManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var workManager = WorkManager.getInstance(this)

    var exMessages: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as AnnoyingExApp).messagesManager.fetchMessages({listOfMessages ->
            exMessages = listOfMessages.messages
        }, {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        })

        btnHereWeGo.setOnClickListener {
            (application as AnnoyingExApp).messagesManager.spamMessages()
        }
    }

}
