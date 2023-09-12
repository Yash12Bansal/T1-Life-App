package com.kvsc.type1.Activities

import com.kvsc.type1.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kvsc.type1.ClassesHere.Constants.DB_CATEGORY_ENTERED
import com.kvsc.type1.ClassesHere.Constants.EDIT_CARBS
import com.kvsc.type1.ClassesHere.Constants.EDIT_CATEGORY
import com.kvsc.type1.ClassesHere.Constants.EDIT_DATE
import com.kvsc.type1.ClassesHere.Constants.EDIT_FOOD
import com.kvsc.type1.ClassesHere.Constants.EDIT_ID
import com.kvsc.type1.ClassesHere.Constants.EDIT_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.EDIT_TIME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME_STAMP
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_ID_LOG
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.USER_DATE_ENTERED
import com.kvsc.type1.ClassesHere.Constants.USER_TIME_ENTERED
import java.util.*


class UpdateFood : AppCompatActivity() {
    var category: String? = null
    var food: String? = null
    var quantity: String? = null
    var carbs: String? = null
    var date: String? = null
    var time: String? = null
    var food_name: TextView? = null
    var carbs_here: TextView? = null
    var date_text: TextView? = null
    var time_text: TextView? = null
    var food_category: Button? = null
    var delete_record: Button? = null
    var date_cross: ImageButton? = null
    var time_cross: ImageButton? = null
    var submit: ImageButton? = null
    var quantity_here: EditText? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var foodref: DatabaseReference = database.getReference("food")
    var date_b: CardView? = null
    var time_b: CardView? = null
    var lv: ListView? = null
    var id = 0
    var timestamp: Long = 0
    var categorylist = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_food)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Update Entry"
        food_name = findViewById(R.id.food_name)
        food_category = findViewById(R.id.food_category)
        quantity_here = findViewById(R.id.quantity_here)
        carbs_here = findViewById(R.id.carbs_here)
        date_text = findViewById(R.id.date_text)
        time_text = findViewById(R.id.time_text)
        date_cross = findViewById(R.id.date_cross)
        time_cross = findViewById(R.id.time_cross)
        submit = findViewById(R.id.submit_button)
        date_b = findViewById(R.id.date_button)
        time_b = findViewById(R.id.time_button)
        delete_record = findViewById(R.id.delete_record)
        val i = intent
        category = i.getStringExtra(EDIT_CATEGORY)
        food = i.getStringExtra(EDIT_FOOD)
        quantity = i.getStringExtra(EDIT_QUANTITY)
        carbs = i.getStringExtra(EDIT_CARBS)
        date = i.getStringExtra(EDIT_DATE)
        time = i.getStringExtra(EDIT_TIME)
        id = i.getIntExtra(EDIT_ID, 0)
        timestamp = i.getLongExtra(FOOD_DB_TIME_STAMP, 0)
        food_name!!.setText(food + "")
        food_category!!.setText(category)
        quantity_here!!.setText(quantity)
        carbs_here!!.setText((Math.round(carbs!!.toDouble() * 100.0) / 100.0).toString() + "")
        date_text!!.setText(date)
        time_text!!.setText(time)
        categorylist.add("Breakfast")
        categorylist.add("Lunch")
        categorylist.add("Dinner")
        categorylist.add("Snack")
        food_category!!.setOnClickListener(View.OnClickListener {
            val alertDialog = AlertDialog.Builder(this@UpdateFood).create()
            alertDialog.setCancelable(true)
            val layoutInflater = LayoutInflater.from(this@UpdateFood)
            val convertView: View = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
            alertDialog.setTitle("Category")
            lv = convertView.findViewById<View>(R.id.lv) as ListView
            val adapter = ArrayAdapter(this@UpdateFood, android.R.layout.simple_list_item_1, categorylist)
            lv!!.adapter = adapter
            lv!!.onItemClickListener =
                OnItemClickListener { adapterView, view, i, l ->
                    val selectedFromList = lv!!.getItemAtPosition(i).toString()
                    //                        Log.i("Check2", selectedFromList + "  " + i);
                    food_category!!.setText(selectedFromList)
                    alertDialog.cancel()
                }
            alertDialog.setView(convertView)
            alertDialog.show()
        })
        date_b!!.setOnClickListener(object : View.OnClickListener {
            var mcurrenttime = Calendar.getInstance()
            var year = mcurrenttime[Calendar.YEAR]
            var month = mcurrenttime[Calendar.MONTH]
            var day = mcurrenttime[Calendar.DAY_OF_MONTH]
            override fun onClick(view: View) {
                val dialog = DatePickerDialog(this@UpdateFood, android.R.style.Theme_Holo_Light_Dialog,
                    { datePicker, year, month, day ->
                        val final_date = day.toString() + "/" + (month + 1) + "/" + year
                        date_text!!.setText(final_date)
                    }, year, month, day
                )
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
        })
        time_b!!.setOnClickListener(object : View.OnClickListener {
            var current_time = Calendar.getInstance()
            var hour = current_time[Calendar.HOUR_OF_DAY]
            var minute = current_time[Calendar.MINUTE]
            override fun onClick(view: View) {
                val timePickerDialog =
                    TimePickerDialog(this@UpdateFood, android.R.style.Theme_Holo_Light_Dialog,
                        { timePicker, i, i1 -> time_text!!.setText("$i:$i1") }, hour, minute, true
                    )
                timePickerDialog.show()
            }
        })
        delete_record!!.setOnClickListener(View.OnClickListener {
            try {
                val b = AlertDialog.Builder(this@UpdateFood)
                b.setTitle("Alert")
                b.setMessage("Are you sure you want to delete this food item?")
                b.setPositiveButton(
                    "Yes"
                ) { dialogInterface, index ->
                    val helper = DataBaseFoodHelperLog(this@UpdateFood)
                    val db = helper.writableDatabase
                    db.delete(
                        FOOD_HELPER_DB_NAME_LOG,
                        FOOD_HELPER_DB_ID_LOG.toString() + "=?",
                        arrayOf(id.toString())
                    )
                    db.close()
                    Toast.makeText(this@UpdateFood, "Food item deleted!", Toast.LENGTH_SHORT)
                        .show()
                    val preferences =
                        PreferenceManager.getDefaultSharedPreferences(this@UpdateFood)
                    val key = preferences.getString(KEY, null)
                    foodref.child(key!!).child(timestamp.toString() + "").child("FOOD_NAME")
                        .setValue("This item has been deleted")
                    val i = Intent()
                    i.setClass(this@UpdateFood, FoodLog::class.java)
                    startActivity(i)
                }
                b.setNegativeButton(
                    "No"
                ) { dialogInterface, i -> dialogInterface.cancel() }
                b.show()
            } catch (e: Exception) {
            }
        })
        submit!!.setOnClickListener(View.OnClickListener {
            if (!isNetworkAvailable) {
                Toast.makeText(this@UpdateFood, "No connection", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val b = AlertDialog.Builder(this@UpdateFood)
                    b.setTitle("Alert")
                    b.setMessage("Are you sure you want to update this food item?")
                    b.setPositiveButton(
                        "Yes"
                    ) { dialogInterface, index ->
                        val helper = DataBaseFoodHelperLog(this@UpdateFood)
                        val db = helper.writableDatabase
                        val cv = ContentValues()
                        cv.put(DB_CATEGORY_ENTERED, food_category!!.getText().toString())
                        cv.put(FOOD_DB_QUANTITY, quantity_here!!.getText().toString())
                        cv.put(USER_DATE_ENTERED, date_text!!.getText().toString())
                        cv.put(USER_TIME_ENTERED, time_text!!.getText().toString())
                        val preferences =
                            PreferenceManager.getDefaultSharedPreferences(this@UpdateFood)
                        val key = preferences.getString(KEY, null)
                        foodref.child(key!!).child(timestamp.toString() + "").child("QUANTITY")
                            .setValue(quantity_here!!.getText().toString())
                        foodref.child(key!!).child(timestamp.toString() + "")
                            .child("FOOD_CATEGORY").setValue(food_category!!.getText().toString())
                        foodref.child(key!!).child(timestamp.toString() + "").child("DATE")
                            .setValue(date_text!!.getText().toString())
                        foodref.child(key!!).child(timestamp.toString() + "").child("TIME")
                            .setValue(time_text!!.getText().toString())
                        Toast.makeText(
                            this@UpdateFood,
                            "Food item updated!",
                            Toast.LENGTH_SHORT
                        ).show()
                        db.update(FOOD_HELPER_DB_NAME_LOG, cv, "food_db_id_log= $id", null)
                        db.close()
                        val i = Intent()
                        i.setClass(this@UpdateFood, FoodLog::class.java)
                        startActivity(i)
                    }
                    b.setNegativeButton(
                        "No"
                    ) { dialogInterface, i -> dialogInterface.cancel() }
                    b.show()
                } catch (e: Exception) {
                }
            }
        })
        date_cross!!.setOnClickListener(View.OnClickListener { date_text!!.setText("Date") })
        time_cross!!.setOnClickListener(View.OnClickListener { time_text!!.setText("Time") })
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
}