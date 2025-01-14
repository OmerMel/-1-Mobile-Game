package com.example.hw1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class ScoreAdapter(
    private val scoresList: List<ScoreEntry>,
    private val onItemClick: (ScoreEntry) -> Unit
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    data class ScoreEntry(
        val pictureResId: Int,
        val name: String,
        val date: String,
        val score: Int,
        val latitude: Double? = null,
        val longitude: Double? = null
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.score_item, parent, false)
        return ScoreViewHolder(itemView, onItemClick) // Pass the click listener to ViewHolder
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val scoreEntry = scoresList[position]
        holder.bind(scoreEntry)
    }

    override fun getItemCount(): Int {
        return scoresList.size
    }

    class ScoreViewHolder(
        itemView: View,
        private val onItemClick: (ScoreEntry) -> Unit  // Add this parameter
    ) : RecyclerView.ViewHolder(itemView) {
        private val picture: AppCompatImageView = itemView.findViewById(R.id.score_IMG_picture)
        private val name: MaterialTextView = itemView.findViewById(R.id.score_LBL_name)
        private val date: MaterialTextView = itemView.findViewById(R.id.score_LBL_date)
        private val score: MaterialTextView = itemView.findViewById(R.id.score_LBL_score)

        fun bind(scoreEntry: ScoreEntry) {
            picture.setImageResource(scoreEntry.pictureResId)
            name.text = scoreEntry.name
            date.text = scoreEntry.date
            scoreEntry.score.toString().also { score.text = it }
            itemView.setOnClickListener {
                onItemClick(scoreEntry)
            }
        }
    }
}