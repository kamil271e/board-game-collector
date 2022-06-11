package com.example.board_game_collector

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.time.LocalDateTime
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun XMLtoDB(path: String, context: Context?){
        val filename = "data.xml"
        val inDir = File(path, "XML")
        val games: MutableList<Game> = mutableListOf()
        val histories: MutableList<History> = mutableListOf()
        val titles: MutableList<String> = mutableListOf()
        val years: MutableList<String> = mutableListOf()
        val ids: MutableList<String> = mutableListOf()
        val rankings: MutableList<String> = mutableListOf()
        val curTime = LocalDateTime.now()

        if (inDir.exists()){
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("item")
                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val children = (itemNode as Element).childNodes
                        ids.add(itemNode.getAttribute("objectid"))
                        for (j in 0 until children.length) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "name" -> {
                                        if (titles.size > years.size){
                                            years.add("0")
                                        }
                                        titles.add(node.textContent)
                                        //Log.i("abcdebug", node.textContent.toString())
                                    }
                                    "yearpublished" -> {
                                        years.add(node.textContent)
                                        //Log.i("abcdebug", node.textContent.toString())
                                    }

                                }
                            }
                        }
                    }
                }
                val ranks: NodeList = xmlDoc.getElementsByTagName("ranks")
                for (k in 0 until ranks.length){
                    val itemNode : Node = ranks.item(k)
                    for (l in 0 until itemNode.childNodes.length){
                        val node = itemNode.childNodes.item(l)
                        if (node is Element && node.getAttribute("name") == "boardgame"){
                            rankings.add(node.getAttribute("value"))
                        }
                    }
                }
            }
        }

        for (i in 0 until ids.size){
            val g: Game = try{
                Game(ids[i].toLong(), titles[i], years[i].toInt(), rankings[i].toInt())
            }catch (e: Exception){
                Game(ids[i].toLong(), titles[i], years[i].toInt(), 0)
            }
            try{
                val h = History(ids[i].toLong(), curTime, rankings[i].toInt())
                histories.add(h)
            }catch (e: Exception){}
            games.add(g)
        }
        val dbHandler = MyDBHandler(context, this.toString(), null, 1)
        dbHandler.clearGames()
        dbHandler.loadGames(games)
        dbHandler.clearHistories()
        dbHandler.loadHistories(histories)
        //dbHandler.displayGames()
        //dbHandler.displayHistories()
        dbHandler.closeDB()
    }
}
