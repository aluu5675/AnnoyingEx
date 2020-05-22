package com.andyluu.annoyingex

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MessagesManager(private val context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    private var workManager = WorkManager.getInstance(context)

    private val notificationCompatManager = NotificationManagerCompat.from(context)

    companion object {
        const val EX_MESSAGES_WORK_TAG = "EX_MESSAGES_WORK_TAG"

        const val EX_CHANNEL_ID = "EX_CHANNEL_ID"
    }

    init {
        createNotificationChannel()
    }

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

    fun spamMessages(message: String) {
        if (!isSpamMessagesRunning()) {
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<MessageWorker>(20, TimeUnit.MINUTES)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .addTag(EX_MESSAGES_WORK_TAG)
                .build()
            workManager.enqueue(workRequest)
        }
    }

    fun stopMessages() {
        workManager.cancelAllWorkByTag(EX_MESSAGES_WORK_TAG)
    }

    private fun isSpamMessagesRunning(): Boolean {
        return when (workManager.getWorkInfosByTag(EX_MESSAGES_WORK_TAG).get().firstOrNull()?.state) {
            WorkInfo.State.RUNNING,
            WorkInfo.State.ENQUEUED -> true
            else -> false
        }
    }

    fun messageNotification(message: String) {
        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingMainIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, EX_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chat_box_24dp)
            .setContentTitle("Leaf Me Alone")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingMainIntent)
            .setAutoCancel(true)
            .build()
        notificationCompatManager.notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Ex Message Notifications"
            val descriptionText = "Messages about getting back together"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(EX_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationCompatManager.createNotificationChannel(channel)
        }
    }
}