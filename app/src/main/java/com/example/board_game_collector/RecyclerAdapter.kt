package com.example.board_game_collector

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val navController: NavController/*, private val ld: MutableList<MutableList<String>>*/): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var orders = arrayOf("1","2","3","1","2","3","1","2","3","1","2","3")
    private var titles = arrayOf("abcd (2010)", "udsfus (1992)", "jnsdfb (1992)", "abcd (2010)", "udsfus (1992)", "jnsdfb (1992)", "abcd (2010)", "udsfus (1992)", "jnsdfb (1992)", "abcd (2010)", "udsfus (1992)", "jnsdfb (1992)")
    private var rankings = arrayOf("1","3244","3134", "1","3244","3134", "1","3244","3134", "1","3244","3134")
    //private var orders: MutableList<String> = mutableListOf()
    //private var titles: MutableList<String> = mutableListOf()
    //private var rankings: MutableList<String> = mutableListOf()
    //private var dbHandler = MyDBHandler(navController.context, this.toString(), null, 1)
    //private var ld = load()
    //private var orders = ld[0]
    //private var titles = ld[1]
    //private var rankings = ld[2]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        //loadDBData()
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        //Log.i("abcdesksdmf", titles[position])
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
    /*private fun loadDBData(){
        val c = dbHandler.getGames()
        c.moveToFirst()
        var i = 0
        while(!c.isAfterLast){
            orders.add(i.toString())
            titles.add("${c.getString(1)} ${c.getString(2)}")
            rankings.add(c.getString(3))
            i++
            c.moveToNext()
        }
        dbHandler.closeDB()
        Log.i("ABCDsks", rankings.toString())
    }*/
    private fun load(): MutableList<MutableList<String>> {
        val o: MutableList<String> = mutableListOf()
        val t: MutableList<String> = mutableListOf()
        val r: MutableList<String> = mutableListOf()
        val dh = MyDBHandler(navController.context, this.toString(), null, 1)
        val c = dh.getGames()
        c.moveToFirst()
        var i = 0
        while(!c.isAfterLast){
            o.add(i.toString())
            t.add("${c.getString(1)} ${c.getString(2)}")
            r.add(c.getString(3))
            i++
            c.moveToNext()
        }
        dh.closeDB()
        Log.i("ABCDsks", rankings.toString())
        return mutableListOf(o, t, r)
    }
}