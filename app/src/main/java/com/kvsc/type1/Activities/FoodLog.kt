package com.kvsc.type1.Activities

import android.annotation.SuppressLint
import com.kvsc.type1.R
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kvsc.type1.Adapters.Food_DB_AdapterLog
import com.kvsc.type1.ClassesHere.FoodUserLog
import com.kvsc.type1.ClassesHere.RecyclerItemClickListener

import com.kvsc.type1.ClassesHere.Constants.CARBS
import com.kvsc.type1.ClassesHere.Constants.DB_CATEGORY_ENTERED
import com.kvsc.type1.ClassesHere.Constants.EDIT_CARBS
import com.kvsc.type1.ClassesHere.Constants.EDIT_CATEGORY
import com.kvsc.type1.ClassesHere.Constants.EDIT_DATE
import com.kvsc.type1.ClassesHere.Constants.EDIT_FOOD
import com.kvsc.type1.ClassesHere.Constants.EDIT_ID
import com.kvsc.type1.ClassesHere.Constants.EDIT_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.EDIT_TIME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME_STAMP
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_ID_LOG
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
import com.kvsc.type1.ClassesHere.Constants.USER_DATE_ENTERED
import com.kvsc.type1.ClassesHere.Constants.USER_TIME_ENTERED
import com.kvsc.type1.Navigation_drawer.MainScreen


class FoodLog : AppCompatActivity() {
    var arrayList: ArrayList<FoodUserLog>? = null
    var food_name: String? = null
    var date: String? = null
    var food_carbs: String? = null
    var quantity: String? = null
    var time: String? = null
    var category: String? = null
    var id = 0
    var timestamp: Long = 0
    var dataBaseFoodHelper: DataBaseFoodHelperLog? = null
    var food_db_adapter: Food_DB_AdapterLog? = null
    var recyclerView: RecyclerView? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var foodref: DatabaseReference = database.getReference("food")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_log)
        supportActionBar!!.title = "Food Log"
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        arrayList = ArrayList<FoodUserLog>()
        recyclerView = findViewById(R.id.recycle)
        recyclerView?.setHasFixedSize(true)
        //        hideSoftKeyboard();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        food_db_adapter = Food_DB_AdapterLog(this@FoodLog, arrayList!!)
        recyclerView!!.setAdapter(food_db_adapter)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this@FoodLog))
        dataBaseFoodHelper = DataBaseFoodHelperLog(this@FoodLog)
        recyclerView!!.addOnItemTouchListener(
            RecyclerItemClickListener(
                this@FoodLog,
                recyclerView!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        var options: ActivityOptions? = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            options = ActivityOptions.makeSceneTransitionAnimation(this@FoodLog)
                        }
                        val ii = Intent()
                        ii.setClass(this@FoodLog, UpdateFood::class.java)
                        ii.putExtra(FOOD_DB_TIME_STAMP, arrayList!![position].timestamp)
                        ii.putExtra(EDIT_CATEGORY, arrayList!![position].category)
                        ii.putExtra(EDIT_FOOD, arrayList!![position].food)
                        ii.putExtra(EDIT_QUANTITY, arrayList!![position].quantity)
                        ii.putExtra(EDIT_CARBS, arrayList!![position].carbs)
                        ii.putExtra(EDIT_DATE, arrayList!![position].date)
                        ii.putExtra(EDIT_TIME, arrayList!![position].time)
                        ii.putExtra(EDIT_ID, arrayList!![position].id)
                        startActivity(ii, options!!.toBundle())
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )
        setUpViews()
    }

    @SuppressLint("Range")
    private fun setUpViews() {
        try {
            Log.i("TAG4", "setUpViews: ")
            val dp = dataBaseFoodHelper!!.readableDatabase
            arrayList!!.clear()
            val c = dp.query(FOOD_HELPER_DB_NAME_LOG, null, null, null, null, null, null)
            while (c.moveToNext()) {
                Log.i("TAG5", "setUpViews: ")
                food_name = c.getString(c.getColumnIndex(FOOD_DB_NAME))
                food_carbs = c.getString(c.getColumnIndex(CARBS))
                date = c.getString(c.getColumnIndex(USER_DATE_ENTERED))
                time = c.getString(c.getColumnIndex(USER_TIME_ENTERED))
                category = c.getString(c.getColumnIndex(DB_CATEGORY_ENTERED))
                quantity = c.getString(c.getColumnIndex(FOOD_DB_QUANTITY))
                id = c.getInt(c.getColumnIndex(FOOD_HELPER_DB_ID_LOG))
                timestamp = c.getLong(c.getColumnIndex(FOOD_DB_TIME_STAMP))
                val tempcal =
                    java.lang.Float.valueOf(quantity) * java.lang.Float.valueOf(food_carbs)
                val amountroundOff =
                    (Math.round(java.lang.Float.valueOf(tempcal) * 100.0) / 100.0).toFloat()

//            float amountroundOff = (float) (Math.round(Float.valueOf(food_carbs) * 100.0) / 100.0);
                val foodUser = FoodUserLog(
                    food_name!!,
                    quantity!!,
                    amountroundOff.toString() + "",
                    date!!,
                    time!!,
                    category!!,
                    id,
                    timestamp
                )
                //            Reminder reminder2 = new Reminder(id,title, description, date, time,boottime);
//            reminder.add(reminder2);
                arrayList!!.add(foodUser)
            }
            food_db_adapter?.notifyDataSetChanged()
            if (arrayList!!.size == 0) {
                newscreen()
                Toast.makeText(this@FoodLog, "No food items added!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this@FoodLog, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun newscreen() {
        var options: ActivityOptions? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(this@FoodLog)
        }
        val ii = Intent()
        ii.setClass(this@FoodLog, MainScreen::class.java)
        //        ii.putExtra(DIRECT_INSULIN_SCREEN, "predict");
//        startActivity(ii, options.toBundle());
        startActivity(ii)
    }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}