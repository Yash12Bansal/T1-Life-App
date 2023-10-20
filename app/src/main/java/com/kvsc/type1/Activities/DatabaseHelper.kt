package com.kvsc.type1.Activities

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val create_table =
            "CREATE TABLE " + LIST + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "ITEM TEXT) "
        sqLiteDatabase.execSQL(create_table)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
//     sqLiteDatabase.execSQL("DROP IF TABLE EXISTS "+LIST);
//    onCreate(sqLiteDatabase);
    }

    fun adddata(item: String?): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL2, item)
        val result = sqLiteDatabase.insert(LIST, null, contentValues)
        return if (result == -1L) false else true
    }

    val listContent: Cursor
        get() {
            val sqLiteDatabase = this.writableDatabase
            return sqLiteDatabase.rawQuery("SELECT * FROM $LIST", null)
        }

    companion object {
        const val DATABASE_NAME = "saved_list"
        const val LIST = "mylist"
        const val COL1 = "ID"
        const val COL2 = "ITEM"
    }
}