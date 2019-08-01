package com.example.caffieneproductivityapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caffieneproductivityapp.persistence.Drink
import com.example.caffieneproductivityapp.persistence.Rating
import java.text.DateFormat
import java.util.*

class DrinkRatingAdapter internal constructor(context: Context) : RecyclerView.Adapter<DrinkRatingAdapter.DrinkRatingsViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var drinks = emptyList<Drink>()
    private var ratings = emptyList<Rating>()
    private var context = context

    inner class DrinkRatingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val drinkRatingsItemView: RelativeLayout = itemView.findViewById(R.id.drink_item)
        val drinkValueText: TextView = itemView.findViewById(R.id.drink_values)
        val viewRating : TextView = itemView.findViewById(R.id.view_rating_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkRatingsViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_drink_item, parent, false)
        return DrinkRatingsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DrinkRatingsViewHolder, position: Int) {
        val current = drinks[position]
        holder.drinkValueText.text = context.getString(R.string.drink_values_display,
            current.id,
            current.size.toString(),
            convertTimeToDate(current.startTime))

        if (current.id == drinks.size || containsRatingsForDrink(current.id)) {
            if (current.id == drinks.size) {
                holder.viewRating.text = context.getString(R.string.add_ratings)
                holder.viewRating.setBackgroundResource(R.drawable.rounded_corners_add)
            }
            holder.drinkRatingsItemView.setOnClickListener {
                val intent = Intent(context, ViewRatingDataActivity::class.java)
                intent.putExtra("DrinkID", current.id)
                intent.putExtra("Latest", current.id == drinks.size)
                context.startActivity(intent)
            }
        }
        else {
            holder.viewRating.text = context.getString(R.string.no_ratings)
            holder.viewRating.setBackgroundResource(R.drawable.rounded_corners_no_ratings)
        }


    }

    internal fun setDrinks(drinks: List<Drink>) {
        this.drinks = drinks
        notifyDataSetChanged()
    }

    internal fun setRatings(ratings: List<Rating>) {
        this.ratings = ratings
        notifyDataSetChanged()
    }

    private fun containsRatingsForDrink(id : Int) : Boolean {
        return this.ratings.any {
            it.drinkId == id
        }
    }

    private fun convertTimeToDate(time : Long) : String {
        return DateFormat.getDateTimeInstance().format(Date(time))
    }

    override fun getItemCount() = drinks.size
}