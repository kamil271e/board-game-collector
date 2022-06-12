package com.example.board_game_collector

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.board_game_collector.databinding.FragmentDashboardBinding
import java.time.LocalDateTime

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var username : String
    private var bundle = bundleOf()
    private var t = ""
    private var g = ""
    private var e = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        loadBundle()
        loadBindings(view)
        updateViews()
        setBundle()
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
    }

    private fun setBundle(){
        bundle.clear()
        bundle.putString("username", username)
        bundle.putString("time", t)
        bundle.putString("games", g)
        bundle.putString("extras", e)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            val l = synchronize()
            g = l[0].toString()
            e = l[1].toString()
            val curTime = LocalDateTime.now().toString().split("T")
            t = "${curTime[0]},${curTime[1].slice(0 until curTime[1].length-4)}"
            Toast.makeText(context, "Synchronizacja zakończona", Toast.LENGTH_LONG).show()
            updateViews()
            setBundle()
        }
        binding.clearAll.setOnClickListener {
            val dbh = MyDBHandler(context, this.toString(), null, 1)
            dbh.clearAll()
            g = ""; e = ""; t = ""
            updateViews()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun synchronize(): List<Int> {
        val dd = DataDownloader()
        dd.downloadData(username, context?.filesDir.toString(), true)
        Thread.sleep(1_500)
        return dd.XMLtoDB(context?.filesDir.toString(), context)
    }

    private fun updateViews(){
        binding.helloTV.text = "Witaj $username"
        binding.datesTV.text = "Ostatnia synchronizacja: $t"
        binding.gamesTV.text = "Liczba gier: $g"
        binding.extrasTV.text = "Liczba dodatków: $e"
    }
}