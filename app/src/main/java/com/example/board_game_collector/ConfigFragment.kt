package com.example.board_game_collector

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.board_game_collector.databinding.FragmentConfigBinding
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.zip.Inflater
import javax.xml.parsers.DocumentBuilderFactory

class ConfigFragment : Fragment() {
    private lateinit var binding: FragmentConfigBinding
    private lateinit var userName: String
    private lateinit var bundle : Bundle
    private lateinit var path : String
    private var dd: DataDownloader = DataDownloader()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_config, container, false)
        binding = FragmentConfigBinding.bind(view)
        binding.loginBtn.setOnClickListener{
            userName = binding.username.text.toString()
            if (userName == ""){
                Toast.makeText(context, "Wpisz nazwę użytkownika", Toast.LENGTH_SHORT).show()
            }
            else{
                path = context?.filesDir.toString()
                dd.downloadData(userName, path, false)
                Thread.sleep(1_500)
                if (dd.checkUser(path)){
                    Toast.makeText(context, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show()
                    bundle = bundleOf("username" to userName)
                    Navigation.findNavController(view).navigate(R.id.navigateToDashboard, bundle)
                }
                else{
                    Toast.makeText(context, "Użytkownik o podanej nazwie nie istnieje", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
}
