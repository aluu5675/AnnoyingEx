package com.andyluu.annoyingex

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class MessageWorker(private val context: Context, workParams: WorkerParameters): Worker(context, workParams) {
    override fun doWork(): Result {
        val exApp = (applicationContext as AnnoyingExApp)
        exApp.messagesManager.messageNotification(exApp.message)
        return Result.success()
    }

}