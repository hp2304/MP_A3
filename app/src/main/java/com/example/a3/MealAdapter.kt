package com.example.a3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealAdapter(
    private val mealList: ArrayList<Meal>,
    private val onMealClickListener: OnMealClickListener
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = mealList[position]
        holder.textViewMealName.text = meal.mealName
        holder.textViewMealTime.text = meal.mealTime
//        holder.itemView.setOnClickListener {
//            onMealClickListener.onMealClick(meal)
//        }
        holder.itemView.setOnLongClickListener{
            onMealClickListener.onMealLongClick(meal)
            true
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMealName: TextView = itemView.findViewById(R.id.textViewMealName)
        val textViewMealTime: TextView = itemView.findViewById(R.id.textViewMealTime)
    }

    interface OnMealClickListener {
//        fun onMealClick(meal: Meal)
        fun onMealLongClick(meal: Meal)
    }

}
