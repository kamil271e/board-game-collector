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

class RecyclerAdapter(viewData: MutableList<MutableList<String>>, private val navController: NavController,
                      username: String, time: String, games: String, extras: String, isGame: Boolean): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var v1 = viewData[0] // column 1
    private var v2 = viewData[1]
    private var v3 = viewData[2]
    private var v4 = viewData[3]
    private var usr = username
    private var t = time
    private var g = games
    private var e = extras
    private var isgame = isGame
    private var bundle = bundleOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.tv1.text = v1[position]
        holder.tv2.text = v2[position]
        holder.tv3.text = v3[position]
    }

    override fun getItemCount(): Int {
        return v1.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tv1: TextView
        var tv2: TextView
        var tv3: TextView

        init {
            tv1 = itemView.findViewById(R.id.tv1)
            tv2 = itemView.findViewById(R.id.tv2)
            tv3 = itemView.findViewById(R.id.tv3)

            if (isgame){
                itemView.setOnClickListener {
                    val pos: Int = adapterPosition
                    Toast.makeText(itemView.context, v2[pos], Toast.LENGTH_LONG).show()
                    setBundle(v4[pos])
                    navController.navigate(R.id.navigateToHistory, bundle)
                }
            }
        }
    }
    fun setBundle(id: String){
        bundle.putString("username", usr)
        bundle.putString("id", id)
        bundle.putString("time", t)
        bundle.putString("games", g)
        bundle.putString("extras", e)
    }
}