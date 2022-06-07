package com.example.board_game_collector

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
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
import javax.xml.parsers.DocumentBuilderFactory

class DataDownloader{
    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class Downloader(val username: String, val filesDir: String, val stats: Boolean) : AsyncTask<String, Int, String>(){
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = if (stats) URL("https://www.boardgamegeek.com/xmlapi2/collection?username=$username&stats=1")
                else URL("https://www.boardgamegeek.com/xmlapi2/collection?username=$username")
                val connection = url.openConnection()
                Log.i("ABCD", "test1")
                connection.connect()
                Log.i("ABCD", "test2")
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
    fun checkUser(path: String): Boolean{
        val filename = "data.xml"
        val inDir = File(path, "XML")
        var message = ""
        if (inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("error")
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
    fun downloadData(username: String, filesDir: String, stats: Boolean){
        val dd = Downloader(username, filesDir, stats)
        dd.execute()
    }
    fun XMLtoDB(path: String){
        Log.i("ABCD_path", path)
        val filename = "data2.xml"
        val inDir = File(path, "XML")
        var title = ""
        if (inDir.exists()){
            val file = File(inDir, filename)
            /*if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("item")
                //Log.i("ABCD_items", items.toString())
                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        for (j in 0 until children.length) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "name" -> {
                                        title = node.textContent
                                    }
                                }
                            }
                        }
                    }
                }
            }*/
        }
    }
}
