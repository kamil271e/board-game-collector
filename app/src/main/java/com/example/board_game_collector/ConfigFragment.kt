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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_config, container, false)
        binding = FragmentConfigBinding.bind(view)
        binding.loginBtn.setOnClickListener{
            userName = binding.username.text.toString()
            Log.i("ABCDUsername", userName)
            if (userName == ""){
                Toast.makeText(context, "Wpisz nazwę użytkownika", Toast.LENGTH_SHORT).show()
            }
            else{
                downloadData()
                Thread.sleep(1_500)  //
                if (checkUser(context?.filesDir.toString())){
                    Toast.makeText(context, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.navigateToDashboard)
                }
                else{
                    Toast.makeText(context, "Użytkownik o podanej nazwie nie istnieje", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DataDownloader(val username: String, val filesDir: String, val stats: Boolean) : AsyncTask<String, Int, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = if (stats) URL("https://www.boardgamegeek.com/xmlapi2/collection?username=$username&stats=1")
                else URL("https://www.boardgamegeek.com/xmlapi2/collection?username=$username")
                val connection = url.openConnection()
                //connection.setRequestProperty("Accept-Encoding", "identity")
                connection.connect()

                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                if (!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/data.xml")
                val data = ByteArray(1024)
                var count: Int
                var total: Long = 0
                var progress = 0
                count = isStream.read(data)
                while (count != -1) {
                    total += count.toLong()
                    val progressTemp = total.toInt() * 100 / lengthOfFile
                    if (progressTemp % 10 == 0 && progress != progressTemp) {
                        progress = progressTemp
                    }
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            } catch (e: MalformedURLException) {
                return "Zły URL"
            } catch (e: FileNotFoundException) {
                return "Brak pliku"
            } catch (e: IOException) {
                return "Wyjątek IO"
            }
            Log.i("ABCD_readXML", "WCZYTANO NOWY XML!")
            return "success"
        }
    }

    private fun checkUser(path: String): Boolean{
        val filename = "data.xml"
        val inDir = File(path, "XML")
        var message = ""
        if (inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

                val items: NodeList = xmlDoc.getElementsByTagName("error")
                Log.i("ABCD_errorLen", items.length.toString())
                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        for (j in 0 until children.length) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "message" -> {
                                        message = node.textContent
                                        Log.i("ABCD_Message", message)
                                    }
                                }
                            }
                        }
                    }
                }
                if (message == "Invalid username specified") {
                    return false
                }
            }
        }
        return true
    }

    private fun downloadData(){
        val dd = DataDownloader(userName, context?.filesDir.toString(), false)
        dd.execute()
    }
}
