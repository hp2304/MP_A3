package com.example.a3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class MealDetailDialogFragment : DialogFragment() {

    private lateinit var meal: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            meal = it.getParcelable("meal")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_detail_dialog, container, false)
        val textViewMealName: TextView = view.findViewById(R.id.textViewMealName)
        val textViewMealTime: TextView = view.findViewById(R.id.textViewMealTime)

        textViewMealName.text = meal.mealName
        textViewMealTime.text = meal.mealTime

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(meal: Meal) =
            MealDetailDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("meal", meal)
                }
            }
    }
}
