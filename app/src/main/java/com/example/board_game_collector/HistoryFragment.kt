package com.example.board_game_collector

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
import com.example.board_game_collector.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var username: String
    private lateinit var id: String
    private var bundle = bundleOf()
    private var g = ""
    private var e = ""
    private var t = ""
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        loadBundle()
        setBundle()
        loadBindings(view)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(getHistories(), this.findNavController(), username, t, g, e, false)
        binding.recyclerView.adapter = adapter
        return view
    }
    private fun loadBundle(){
        if (!arguments?.getString("username").isNullOrBlank()){
            username = arguments?.getString("username").toString()
        }
        if (!arguments?.getString("time").isNullOrBlank()){
            t = arguments?.getString("time").toString()
        }
        if (!arguments?.getString("games").isNullOrBlank()){
            g = arguments?.getString("games").toString()
        }
        if (!arguments?.getString("extras").isNullOrBlank()){
            e = arguments?.getString("extras").toString()
        }
        if (!arguments?.getString("id").isNullOrBlank()){
            id = arguments?.getString("id").toString()
        }
    }
    private fun setBundle(){
        bundle.putString("username", username)
        bundle.putString("id", id)
        bundle.putString("time", t)
        bundle.putString("games", g)
        bundle.putString("extras", e)
    }
    private fun loadBindings(view: View){
        binding = FragmentHistoryBinding.bind(view)
        binding.exit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToGames, bundle)
        }
    }

    private fun getHistories(): MutableList<MutableList<String>> {
        try{
            val o: MutableList<String> = mutableListOf()
            val d: MutableList<String> = mutableListOf()
            val r: MutableList<String> = mutableListOf()
            val ids: MutableList<String> = mutableListOf()
            val dh = MyDBHandler(context, this.toString(), null, 1)
            val c = dh.getHistories(id.toLong())
            c.moveToFirst()
            var i = 1
            while(!c.isAfterLast){
                o.add(i.toString())
                d.add(c.getString(3))
                r.add(c.getString(2))
                ids.add(c.getString(1))
                i++
                c.moveToNext()
            }
            dh.closeDB()
            return mutableListOf(o, d, r, ids)
        }catch (e: Exception) {}
        return mutableListOf(mutableListOf(""),mutableListOf(""),mutableListOf(""),mutableListOf(""))
    }
}