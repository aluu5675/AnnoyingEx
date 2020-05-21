package com.andyluu.annoyingex

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class MessagesManager(private val context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    private var workManager = WorkManager.getInstance(context)

    fun fetchMessages(onMessagesReady: (ExMessages) -> Unit, onError: (() -> Unit)? = null) {
        val messagesURL = "https://raw.githubusercontent.com/echeeUW/codesnippets/master/ex_messages.json"

        val request = StringRequest(
            Request.Method.GET, messagesURL,
            { response ->
                val gson = Gson()
                val messages = gson.fromJson(response, ExMessages::class.java)
                onMessagesReady.invoke(messages)
            },
            {
                onError?.invoke()
            })

        queue.add(request)
    }

    fun spamMessages() {
        val constraints = Constraints.Builder()
            //.setRequiresCharging(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MessageWorker>(20, TimeUnit.MINUTES)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
    }
}