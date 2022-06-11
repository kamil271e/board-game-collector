package com.example.board_game_collector

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.board_game_collector.databinding.FragmentDashboardBinding
import kotlinx.serialization.json.Json

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var username : String
    private lateinit var bundle : Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        loadBundle()
        loadBindings(view)
        return view
    }

    private fun loadBundle(){
        if (!arguments?.getString("username").isNullOrBlank()){
            username = arguments?.getString("username").toString()
        }
        bundle = bundleOf("username" to username)
    }

    @SuppressLint("SetTextI18n")
    private fun loadBindings(view: View){
        binding = FragmentDashboardBinding.bind(view)
        binding.logoutBtn.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateToConfig)
            Toast.makeText(context, "Wylogowano", Toast.LENGTH_SHORT).show()
        }
        binding.games.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateToGames, bundle)
        }
        binding.extras.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateToExtras, bundle)
        }
        binding.syncBtn.setOnClickListener {
            synchronize()
            Toast.makeText(context, "Synchronizacja zakończona", Toast.LENGTH_LONG).show()
        }
        binding.helloTV.text = "Witaj $username"
    }

    private fun synchronize(){
        val dd = DataDownloader()
        dd.downloadData(username, context?.filesDir.toString(), true)
        Thread.sleep(1_500)
        dd.XMLtoDB(context?.filesDir.toString(), context)
    }
}