package com.example.board_game_collector

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import java.time.Duration
import java.time.LocalDate
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
        isStart(view)
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

    private fun isStart(view: View){
        if (!arguments?.getString("start").isNullOrBlank()){
            clearAll()
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
            Toast.makeText(context, "Gry", Toast.LENGTH_SHORT).show()
        }
        binding.extras.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateToExtras, bundle)
            Toast.makeText(context, "Dodatki", Toast.LENGTH_SHORT).show()

        }
        binding.syncBtn.setOnClickListener {
            try{
                val prevT = LocalDateTime.parse(t.split(",")[0] + "T" + t.split(",")[1])
                val curT = LocalDateTime.now()
                val diff = Duration.between(prevT, curT).toMinutes()
                if (diff < 24*60){
                    val alert = AlertDialog.Builder(context)
                    alert.setTitle("Potwierdzenie")
                    alert.setMessage("Czy na pewno chcesz dokonać synchronizacji? Nie upłynęły 24 godziny od poprzedniej.")
                    alert.setPositiveButton("Tak"){_,_ ->
                        doSynchronize()
                    }
                    alert.setNegativeButton("Nie"){_,_ ->
                        Toast.makeText(context, "Anulowano", Toast.LENGTH_SHORT).show()
                    }
                    alert.show()
                }
            }catch (e:Exception){
                doSynchronize()
            }
        }
        binding.clearAll.setOnClickListener {
            val alert = AlertDialog.Builder(context)
            alert.setTitle("Potwierdzenie")
            alert.setMessage("Czy na pewno chcesz trwale usunąć dane?")
            alert.setPositiveButton("Tak"){_,_ ->
                clearAll()
            }
            alert.setNegativeButton("Nie"){_,_ ->
                Toast.makeText(context, "Anulowano", Toast.LENGTH_SHORT).show()
            }
            alert.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun synchronize(): List<Int> {
        val dd = DataDownloader()
        dd.downloadData(username, context?.filesDir.toString(), true)
        Thread.sleep(1_500)
        return dd.XMLtoDB(context?.filesDir.toString(), context)
    }

    @SuppressLint("SetTextI18n")
    private fun updateViews(){
        binding.helloTV.text = "Witaj $username"
        binding.datesTV.text = "Ostatnia synchronizacja: $t"
        binding.gamesTV.text = "Liczba gier: $g"
        binding.extrasTV.text = "Liczba dodatków: $e"
    }

    private fun clearAll(){
        val dbh = MyDBHandler(context, this.toString(), null, 1)
        dbh.clearAll()
        dbh.closeDB()
        g = ""; e = ""; t = ""
        updateViews()
        setBundle()
        Toast.makeText(context, "Wyczyszono wszystkie dane", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doSynchronize(){
        val l = synchronize()
        g = l[0].toString()
        e = l[1].toString()
        val curTime = LocalDateTime.now().toString().split("T")
        t = "${curTime[0]},${curTime[1].slice(0 until curTime[1].length-7)}"
        Toast.makeText(context, "Synchronizacja zakończona", Toast.LENGTH_LONG).show()
        updateViews()
        setBundle()
    }
}