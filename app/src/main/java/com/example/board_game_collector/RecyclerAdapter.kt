package com.example.board_game_collector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(ld: MutableList<MutableList<String>>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var orders = ld[0]
    private var titles = ld[1]
    private var rankings = ld[2]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.titleYear.text = titles[position]
        holder.ordNum.text = orders[position]
        holder.ranking.text = rankings[position]
    }

    override fun getItemCount(): Int {
        return rankings.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var ordNum: TextView
        var titleYear: TextView
        var ranking: TextView

        init {
            ordNum = itemView.findViewById(R.id.ordNum)
            titleYear = itemView.findViewById(R.id.titleYear)
            ranking = itemView.findViewById(R.id.rank)
        }
    }
}