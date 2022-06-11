package com.example.board_game_collector

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.board_game_collector.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var username: String
    private lateinit var bundle : Bundle
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_games, container, false)
        loadBundle()
        loadBindings(view)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(getGames())
        binding.recyclerView.adapter = adapter
        return view
    }

    private fun loadBundle(){
        if (!arguments?.getString("username").isNullOrBlank()){
            username = arguments?.getString("username").toString()
        }
        bundle = bundleOf("username" to username)
    }

    private fun loadBindings(view: View){
        binding = FragmentGamesBinding.bind(view)
        loadBundle()
        binding.exit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToDashboard, bundle)
        }
    }

    private fun getGames(): MutableList<MutableList<String>> {
        val o: MutableList<String> = mutableListOf()
        val t: MutableList<String> = mutableListOf()
        val r: MutableList<String> = mutableListOf()
        val dh = MyDBHandler(context, this.toString(), null, 1)
        val c = dh.getGames()
        c.moveToFirst()
        var i = 1
        while(!c.isAfterLast){
            o.add(i.toString())
            var yr = c.getString(2)
            if (yr == "0") yr = ""
            t.add("${c.getString(1)} $yr")
            r.add(c.getString(3))
            i++
            c.moveToNext()
        }
        dh.closeDB()
        return mutableListOf(o, t, r)
    }
}