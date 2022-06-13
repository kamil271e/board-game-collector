package com.example.board_game_collector

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter

class MyDBHandler(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    private var db = this.writableDatabase

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "bgc.db_"

        const val TABLE_GAMES = "games"
        const val COLUMN_TITLE = "title"
        const val COLUMN_YEAR = "year"
        const val COLUMN_ID = "game_id"
        const val COLUMN_RANKING = "ranking"

        const val TABLE_HISTORY = "history"
        const val COLUMN_DATE = "date"
        const val COLUMN_RANKING_HIS = "ranking"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.i("abcd", "imheredbcreate")
        val CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_GAMES (" +
                "$COLUMN_ID LONG PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_YEAR INTEGER," +
                "$COLUMN_RANKING INTEGER)"

        val CREATE_HIS_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_HISTORY (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ID LONG, $COLUMN_RANKING_HIS INTEGER, $COLUMN_DATE DATE)"

        db.execSQL(CREATE_GAMES_TABLE)
        db.execSQL(CREATE_HIS_TABLE)
        this.db = db
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    fun createTables(){
        val CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_GAMES (" +
                "$COLUMN_ID LONG PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_YEAR INTEGER," +
                "$COLUMN_RANKING INTEGER)"

        val CREATE_HIS_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_HISTORY (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ID LONG, $COLUMN_RANKING_HIS INTEGER, $COLUMN_DATE DATE)"

        db.execSQL(CREATE_GAMES_TABLE)
        db.execSQL(CREATE_HIS_TABLE)
    }

    private fun addGame(game: Game){
        try{
            val values = ContentValues().apply {
                put(COLUMN_ID, game.id)
                put(COLUMN_TITLE, game.title)
                put(COLUMN_RANKING, game.ranking)
                put(COLUMN_YEAR, game.year)
            }
            db.insert(TABLE_GAMES, null, values)
        }catch (e: Exception){ }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addHistory(history: History){
        try{
            //val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS.SSS")
            val values = ContentValues().apply {
                put(COLUMN_ID, history.id)
                put(COLUMN_DATE, history.date.toString())
                //Log.i("abcdate", history.date.toString())
                put(COLUMN_RANKING_HIS, history.ranking)
            }
            db.insert(TABLE_HISTORY, null, values)
        }
        catch(e: Exception){}
    }

    fun loadGames(games: MutableList<Game>){
        for (g in games){
            addGame(g)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadHistories(histories: MutableList<History>) {
        for (h in histories){
            addHistory(h)
        }
    }

    fun clearGames(){
        try{ db.execSQL("DELETE FROM $TABLE_GAMES") }
        catch (e:Exception) {}
    }

    fun clearHistories(){
        try { db.execSQL("DELETE FROM $TABLE_HISTORY") }
        catch (e:Exception) {}
    }

    fun amountGames(): Int{
        try {
            val gamesQuery = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_RANKING!=0"
            val c: Cursor = db.rawQuery(gamesQuery, null)
            var i = 0
            c.moveToFirst()
            while(!c.isAfterLast){
                //Log.i("Game", "${c.getString(0)} ${c.getString(1)} ${c.getString(2)} ${c.getString(3)}")
                c.moveToNext()
                i++
            }
            c.close()
            return i
        }catch (e:Exception) {}
        return 0
    }

    fun amountHistories(): Int{
        try {
            val histQuery = "SELECT * FROM $TABLE_HISTORY"
            val c: Cursor = db.rawQuery(histQuery, null)
            var i = 0
            c.moveToFirst()
            while(!c.isAfterLast){
                //Log.i("History", "${c.getString(0)} ${c.getString(1)} ${c.getString(2)} ${c.getString(3)}")
                c.moveToNext()
                i++
            }
            c.close()
            return i
        }catch (e:Exception) {}
        return 0
    }

    fun amountExtras(): Int{
        try {
            val gamesQuery = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_RANKING=0"
            val c: Cursor = db.rawQuery(gamesQuery, null)
            var i = 0
            c.moveToFirst()
            while(!c.isAfterLast){
                Log.i("Game", "${c.getString(0)} ${c.getString(1)} ${c.getString(2)} ${c.getString(3)}")
                c.moveToNext()
                i++
            }
            c.close()
            return i
        }catch (e:Exception) {}
        return 0
    }

    fun closeDB(){
        db.close()
        Log.i("DB_CLOSE", "DB SUCCESSFULLY CLOSED")
    }

    fun clearAll(){
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        Log.i("DB_info", "Tables dropped")
    }

    fun getGames(): Cursor {
        return db.rawQuery("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_RANKING!=0", null)
    }

    fun getExtras(): Cursor {
        return db.rawQuery("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_RANKING=0", null)
    }

    fun getHistories(id: Long): Cursor {
        return db.rawQuery("SELECT * FROM $TABLE_HISTORY WHERE $COLUMN_ID = $id", null)
    }
}
