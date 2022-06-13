package com.example.board_game_collector

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.board_game_collector.databinding.FragmentConfigBinding

class ConfigFragment : Fragment() {
    private lateinit var binding: FragmentConfigBinding
    private lateinit var username: String
    private lateinit var path : String
    private var bundle = bundleOf()
    private var dd: DataDownloader = DataDownloader()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_config, container, false)
        binding = FragmentConfigBinding.bind(view)
        binding.loginBtn.setOnClickListener{
            username = binding.username.text.toString()
            if (username == ""){
                Toast.makeText(context, "Wpisz nazwę użytkownika", Toast.LENGTH_SHORT).show()
            }
            else{
                login(view)
            }
        }
        return view
    }

    private fun login(view: View){
        path = context?.filesDir.toString()
        dd.downloadData(username, path, false)
        Thread.sleep(1_500)
        if (dd.checkUser(path)){
            Toast.makeText(context, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show()
            bundle.putString("username", username)
            bundle.putString("start","1")
            Navigation.findNavController(view).navigate(R.id.navigateToDashboard, bundle)
        }
        else{
            Toast.makeText(context, "Użytkownik o podanej nazwie nie istnieje", Toast.LENGTH_SHORT).show()
        }
    }
}
