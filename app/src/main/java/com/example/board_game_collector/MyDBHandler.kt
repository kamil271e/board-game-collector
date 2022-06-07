package com.example.board_game_collector

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter

class MyDBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int):
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

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_GAMES (" +
                "$COLUMN_ID LONG PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_YEAR INTEGER," +
                "$COLUMN_RANKING INTEGER)"

        val CREATE_HIS_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_HISTORY (" +
                "$COLUMN_ID LONG PRIMARY KEY, $COLUMN_RANKING_HIS INTEGER, $COLUMN_DATE DATE)"

        db.execSQL(CREATE_GAMES_TABLE)
        db.execSQL(CREATE_HIS_TABLE)
        Log.i("ABCD_DATABASE", "CREATED SUCCESSFULLY")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    fun addGame(game: Game){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, game.id)
            put(COLUMN_TITLE, game.title)
            put(COLUMN_RANKING, game.ranking)
            put(COLUMN_YEAR, game.year)
        }
        db.insert(TABLE_GAMES, null, values)
        db.close()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addHistory(history: History){
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS.SSS")
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, history.id)
            put(COLUMN_DATE, dateFormat.format(history.date))
            put(COLUMN_RANKING_HIS, history.ranking)
        }
        db.insert(TABLE_HISTORY, null, values)
        db.close()
    }

    fun loadGames(){}

    fun loadHistory(){}

}
