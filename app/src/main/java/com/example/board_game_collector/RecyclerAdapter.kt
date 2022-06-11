package com.example.board_game_collector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.board_game_collector.databinding.FragmentHistoryBinding

class RecyclerAdapter(ld: MutableList<MutableList<String>>, private val navController: NavController, username: String, specType: Boolean): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var v1 = ld[0]
    private var v2 = ld[1]
    private var v3 = ld[2]
    private var v4 = ld[3]
    private var usr = username
    private var type = specType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.ordNum.text = v1[position]
        holder.titleYear.text = v2[position]
        holder.ranking.text = v3[position]
    }

    override fun getItemCount(): Int {
        return v1.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var ordNum: TextView
        var titleYear: TextView
        var ranking: TextView

        init {
            ordNum = itemView.findViewById(R.id.ordNum)
            titleYear = itemView.findViewById(R.id.titleYear)
            ranking = itemView.findViewById(R.id.rank)

            if (type){
                itemView.setOnClickListener {
                    val pos: Int = adapterPosition
                    Toast.makeText(itemView.context, v2[pos], Toast.LENGTH_LONG).show()
                    val bundle = bundleOf("username_id" to "$usr ${v4[pos]}")
                    navController.navigate(R.id.navigateToHistory, bundle)
                }
            }
        }
    }
}