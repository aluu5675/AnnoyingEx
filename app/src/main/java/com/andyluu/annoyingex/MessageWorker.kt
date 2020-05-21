package com.andyluu.annoyingex

import android.content.Context
import android.util.Log

import androidx.work.Worker
import androidx.work.WorkerParameters

class MessageWorker(private val context: Context, workParams: WorkerParameters): Worker(context, workParams) {
    override fun doWork(): Result {
        Log.i("gggg", "fdsf")

        return Result.success()
    }

}