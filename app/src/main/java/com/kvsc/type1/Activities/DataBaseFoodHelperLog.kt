package com.kvsc.type1.Activities

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kvsc.type1.ClassesHere.Constants.CALORIES
import com.kvsc.type1.ClassesHere.Constants.CARBS
import com.kvsc.type1.ClassesHere.Constants.DB_CATEGORY_ENTERED
import com.kvsc.type1.ClassesHere.Constants.FAT
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME_STAMP
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_ID_LOG
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
import com.kvsc.type1.ClassesHere.Constants.PROTEIN
import com.kvsc.type1.ClassesHere.Constants.USER_DATE_ENTERED
import com.kvsc.type1.ClassesHere.Constants.USER_TIME_ENTERED

class DataBaseFoodHelperLog(context: Context?) :

    SQLiteOpenHelper(context, "FoodHelperLog", null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val createtable =
            (" CREATE TABLE " + FOOD_HELPER_DB_NAME_LOG.toString() + " ( " + FOOD_HELPER_DB_ID_LOG.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FOOD_DB_NAME.toString() + " TEXT, " + FOOD_DB_TIME_STAMP.toString() + " TEXT, " + FOOD_DB_QUANTITY.toString() + " TEXT, " + USER_DATE_ENTERED.toString() + " TEXT, " + USER_TIME_ENTERED.toString() + " TEXT," + DB_CATEGORY_ENTERED.toString() + " TEXT,  " + FAT.toString() + " TEXT," + CARBS.toString() + " TEXT," + PROTEIN.toString() + " TEXT," + CALORIES.toString() + " TEXT); ")
        sqLiteDatabase.execSQL(createtable)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
}