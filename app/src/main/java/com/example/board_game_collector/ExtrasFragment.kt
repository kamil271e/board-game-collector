package com.example.board_game_collector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.board_game_collector.databinding.FragmentExtrasBinding

class ExtrasFragment : Fragment() {
    private lateinit var binding: FragmentExtrasBinding
    private lateinit var username: String
    private lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_extras, container, false)
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

    private fun loadBindings(view: View){
        binding = FragmentExtrasBinding.bind(view)
        binding.exit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToDashboard, bundle)
        }
    }
}