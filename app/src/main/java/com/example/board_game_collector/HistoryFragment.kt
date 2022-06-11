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
    private lateinit var bundle : Bundle
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        loadBundle()
        loadBindings(view)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(getHistories(), this.findNavController(), username, false)
        binding.recyclerView.adapter = adapter
        return view
    }
    private fun loadBundle(){
        if (!arguments?.getString("username_id").isNullOrBlank()){
            username = arguments?.getString("username_id").toString().split(" ")[0]
            id = arguments?.getString("username_id").toString().split(" ")[1]
        }
        bundle = bundleOf("username_id" to username)
    }
    private fun loadBindings(view: View){
        binding = FragmentHistoryBinding.bind(view)
        binding.exit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToGames, bundle)
        }
    }

    private fun getHistories(): MutableList<MutableList<String>> {
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
            Log.i("abcd", "$i ${c.getString(2)} ${c.getString(3)}")
            c.moveToNext()
        }
        dh.closeDB()
        return mutableListOf(o, d, r, ids)
    }
}