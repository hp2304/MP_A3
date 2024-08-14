package com.example.a3

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var editTextMealName: EditText
    private lateinit var editTextMealTime: TextView
    private lateinit var buttonSaveMeal: Button
    private var mealList: ArrayList<Meal>? = null
    private var hour =0
    private var minute =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        buttonSaveMeal = findViewById(R.id.buttonSaveMeal)
        editTextMealName = findViewById(R.id.editTextMealName)
        editTextMealTime = findViewById(R.id.editTextMealTime)
        mealList = ArrayList()

        // Create notification channel
        createNotificationChannel()

        editTextMealTime.setOnClickListener {
            showTimePickerDialog()
        }

        buttonSaveMeal.setOnClickListener { v: View? ->
            val mealName = editTextMealName.text.toString()
            val mealTime = editTextMealTime.text.toString()
            if (mealName.isNotEmpty() && mealTime.isNotEmpty()) {
                mealList!!.add(Meal(mealName, mealTime))

                val intent = Intent(this, Meal_List::class.java)
                intent.putParcelableArrayListExtra("mealList", mealList)
                startActivity(intent)

                // Schedule notification
                scheduleMealNotification(mealName, mealTime)
            } else {
                Toast.makeText(this, "Please enter both name and time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editTextMealTime.text = formattedTime
            }, hour, minute, true)

        timePickerDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_meal -> {
                // Handle "Add Meal" action
                return true
            }
            R.id.view_meals -> {
                val intent = Intent(this, Meal_List::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleMealNotification(mealName: String, mealTime: String) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MealBroadCastReceiver::class.java).apply {
            putExtra("mealName", mealName)
            putExtra("mealTime", mealTime)
        }

        // Use a unique request code for each meal
        val requestCode = mealName.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val mealTimeParsed = dateFormat.parse(mealTime)

        mealTimeParsed?.let {
            val calendar = Calendar.getInstance().apply {
                time = mealTimeParsed
                // Adjust to today's date
                set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
                set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                // Ensure the time is set correctly
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val triggerTime = calendar.timeInMillis
            if (triggerTime > System.currentTimeMillis()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                Toast.makeText(this, "Notification scheduled for $mealTime", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Scheduled time is in the past", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Failed to parse meal time", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Meal Reminder"
            val descriptionText = "Channel for meal time reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("meal_channel", name, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
