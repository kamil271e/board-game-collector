package com.example.board_game_collector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.board_game_collector.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var username: String
    private lateinit var bundle : Bundle
    private var dd: DataDownloader = DataDownloader()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_games, container, false)
        loadBundle()
        loadBindings(view)
        //synchronize()
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

    private fun synchronize(){
        //dd.downloadData(username, context?.filesDir.toString(), true)
        Thread.sleep(1_500)
        //dd.XMLtoDB(context?.filesDir.toString())
    }
}