package com.example.board_game_collector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.board_game_collector.databinding.FragmentExtrasBinding

class ExtrasFragment : Fragment() {
    private lateinit var binding: FragmentExtrasBinding
    private lateinit var username: String
    private lateinit var bundle: Bundle
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_extras, container, false)
        loadBundle()
        loadBindings(view)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(getExtras(), this.findNavController(), username, false)
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
        binding = FragmentExtrasBinding.bind(view)
        binding.exit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToDashboard, bundle)
        }
    }
    private fun getExtras(): MutableList<MutableList<String>>{
        val o: MutableList<String> = mutableListOf()
        val t: MutableList<String> = mutableListOf()
        val y: MutableList<String> = mutableListOf()
        val dh = MyDBHandler(context, this.toString(), null, 1)
        val c = dh.getExtras()
        c.moveToFirst()
        var i = 1
        while(!c.isAfterLast){
            o.add(i.toString())
            t.add(c.getString(1))
            var yr = c.getString(2)
            if (yr == "0") yr = "-"
            y.add(yr)
            i++
            c.moveToNext()
        }
        dh.closeDB()
        return mutableListOf(o, t, y)
    }
}