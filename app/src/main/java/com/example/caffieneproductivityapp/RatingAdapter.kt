package com.example.caffieneproductivityapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caffieneproductivityapp.persistence.Rating
import java.text.DateFormat
import java.util.*

class RatingAdapter internal constructor(context: Context) : RecyclerView.Adapter<RatingAdapter.RatingsViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var ratings = emptyList<Rating>()
    private val context = context

    inner class RatingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingsValueText : TextView = itemView.findViewById(R.id.rating_values)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingAdapter.RatingsViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_rating_item, parent, false)
        return RatingsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RatingsViewHolder, position: Int) {
        val current = ratings[position]
        holder.ratingsValueText.text = context.getString(
            R.string.rating_values_display,
            current.productivityRating,
            convertTimeToDate(current.timeStamp))
    }

    internal fun setRatings(ratings: List<Rating>) {
        this.ratings = ratings
        notifyDataSetChanged()
    }

    override fun getItemCount() = ratings.size

    private fun convertTimeToDate(time : Long) : String {
        return DateFormat.getDateTimeInstance().format(Date(time))
    }
}