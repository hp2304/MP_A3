package com.example.a3

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class Meal_List : AppCompatActivity() {

    private lateinit var recyclerViewMeals: RecyclerView
    private lateinit var mealAdapter: MealAdapter
    private var mealList: ArrayList<Meal>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)

        recyclerViewMeals = findViewById(R.id.recyclerViewMeals)

        if (intent.hasExtra("mealList")) {
            mealList = intent.getParcelableArrayListExtra("mealList")

            mealList?.let {
                mealAdapter = MealAdapter(it, object : MealAdapter.OnMealClickListener {


                    override fun onMealLongClick(meal: Meal) {
                        // Pass the data to DialogFragment
                        val dialogFragment = MealDetailDialogFragment.newInstance(meal)
                        dialogFragment.show(supportFragmentManager, "mealDetailDialog")

                    }


                })

                recyclerViewMeals.layoutManager = LinearLayoutManager(this)
                recyclerViewMeals.adapter = mealAdapter
            }


        }
    }


}
