package com.example.board_game_collector

import android.annotation.SuppressLint
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
    private val db = this.writableDatabase

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "bgc.db"

        const val TABLE_GAMES = "games"
        const val COLUMN_TITLE = "title"
        const val COLUMN_YEAR = "year"
        const val COLUMN_ID = "id"
        const val COLUMN_RANKING = "ranking"

        const val TABLE_HISTORY = "history"
        const val COLUMN_DATE = "date"
        const val COLUMN_RANKING_HIS = "ranking"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_GAMES (" +
                "$COLUMN_ID LONG PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_YEAR INTEGER," +
                "$COLUMN_RANKING INTEGER)"

        val CREATE_HIS_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_HISTORY (" +
                "$COLUMN_ID LONG PRIMARY KEY, $COLUMN_RANKING_HIS INTEGER, $COLUMN_DATE DATE)"

        Log.i("ABCD-here", "tuta")
        db.execSQL(CREATE_GAMES_TABLE)
        db.execSQL(CREATE_HIS_TABLE)
        Log.i("ABCD_DATABASE", "CREATED SUCCESSFULLY")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    private fun addGame(game: Game){
        val values = ContentValues().apply {
            put(COLUMN_ID, game.id)
            put(COLUMN_TITLE, game.title)
            put(COLUMN_RANKING, game.ranking)
            put(COLUMN_YEAR, game.year)
        }
        db.insert(TABLE_GAMES, null, values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addHistory(history: History){
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS.SSS")
        val values = ContentValues().apply {
            put(COLUMN_ID, history.id)
            put(COLUMN_DATE, dateFormat.format(history.date))
            put(COLUMN_RANKING_HIS, history.ranking)
        }
        db.insert(TABLE_HISTORY, null, values)
    }

    fun loadGames(games: MutableList<Game>){
        for (g in games){
            addGame(g)
        }
    }

    fun loadHistory(){}

    fun clearGames(){
        db.execSQL("DELETE FROM $TABLE_GAMES")
    }

    fun clearHistory(){}

    @SuppressLint("Recycle")
    fun displayDB(){
        val gamesQuery = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_RANKING!=0"
        val extrasQuery = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_RANKING==0"
        var c: Cursor = db.rawQuery(gamesQuery, null)
        c.moveToFirst()
        while(!c.isAfterLast){
            Log.i("ABCD+table", "${c.getString(0)} ${c.getString(1)} ${c.getString(2)} ${c.getString(3)}")
            c.moveToNext()
        }
        /*c.close()
        c = db.rawQuery(extrasQuery, null)
        while(!c.isAfterLast){
            Log.i("ABCD+table", "${c.getString(0)} ${c.getString(1)} ${c.getString(2)}")
            c.moveToNext()
        }*/
        c.close()
    }

    fun closeDB(){
        db.close()
    }

    fun getGames(): Cursor {
        return db.rawQuery("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_RANKING!=0", null)
    }
}
