package com.example.board_game_collector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.board_game_collector.databinding.FragmentConfigBinding

class ConfigFragment : Fragment() {
    private lateinit var binding: FragmentConfigBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_config, container, false)
        binding = FragmentConfigBinding.bind(view)
        binding.loginBtn.setOnClickListener{
            if (binding.username.text.toString() == "admin"){
                Toast.makeText(context, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.navigateToDashboard)
            }
            else if (binding.username.text.toString() == ""){
                Toast.makeText(context, "Wpisz nazwę użytkownika", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Użytkownik o podanej nazwie nie istnieje", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}