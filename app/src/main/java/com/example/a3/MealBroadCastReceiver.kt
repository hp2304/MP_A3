package com.example.a3

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class MealBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mealName = intent.getStringExtra("mealName")
        val mealTime = intent.getStringExtra("mealTime")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "meal_channel")
            .setSmallIcon(R.drawable.baseline_food_bank_24)
            .setContentTitle("Meal Reminder")
            .setContentText("It's time for your meal: $mealName at $mealTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
