package com.kvsc.type1.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.kvsc.type1.Activities.DataBaseFoodHelper
import com.kvsc.type1.Activities.DataBaseFoodHelperLog
import com.kvsc.type1.Activities.FoodAdd
import com.kvsc.type1.Activities.PredictionDisplay
import com.kvsc.type1.Adapters.Food_DB_Adapter
import com.kvsc.type1.ClassesHere.Constants.CALORIES
import com.kvsc.type1.ClassesHere.Constants.CARBS
import com.kvsc.type1.ClassesHere.Constants.CURRENT_BG
import com.kvsc.type1.ClassesHere.Constants.CURRENT_DATE
import com.kvsc.type1.ClassesHere.Constants.PREDICT_SCREEN_CATEGORY
import com.kvsc.type1.ClassesHere.Constants.CURRENT_TIME
import com.kvsc.type1.ClassesHere.Constants.DB_CATEGORY_ENTERED
import com.kvsc.type1.ClassesHere.Constants.FAT
import com.kvsc.type1.ClassesHere.Constants.FIXED_BG
import com.kvsc.type1.ClassesHere.Constants.FIXED_EXERCISE_TIME
import com.kvsc.type1.ClassesHere.Constants.FIXED_INSULIN
import com.kvsc.type1.ClassesHere.Constants.FIXED_WORD_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME_STAMP
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_ID
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_BG
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_INSULIN
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.PROTEIN
import com.kvsc.type1.ClassesHere.Constants.STORED_DATE
import com.kvsc.type1.ClassesHere.Constants.TIME_IN_MIN_INSULIN
import com.kvsc.type1.ClassesHere.Constants.TOTAL_CARBS_FOR_PREDICTION
import com.kvsc.type1.ClassesHere.Constants.USER_DATE_ENTERED
import com.kvsc.type1.ClassesHere.Constants.USER_TIME_ENTERED
import com.kvsc.type1.ClassesHere.FoodUser
import com.kvsc.type1.R


import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Predict.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Predict.newInstance] factory method to
 * create an instance of this fragment.
 */
class Predict : Fragment() {
    var date: CardView? = null
    var time: CardView? = null
    var category_button: CardView? = null
    var predict_button: Button? = null
    var date_text: TextView? = null
    var time_text: TextView? = null
    var category_: TextView? = null
    var lv: ListView? = null
    var database = FirebaseDatabase.getInstance()
    var foodref = database.getReference("food")
    var preref = database.getReference("exercise_entry/Prediction")
    var arrayList: ArrayList<FoodUser>? = null
    lateinit var food_name: String
    var time_stamp: String? = null
    var food_carbs: String? = null
    var quantity: String? = null
    var food_fat: String? = null
    var food_protein: String? = null
    var food_calories: String? = null
    var id_ = 0
    var curr_bg: EditText? = null
    var curr_date: String? = null
    var curr_time: String? = null
    var amount = 0f
    var pre_date: String? = null
    var pre_time: String? = null
    var dataBaseFoodHelper: DataBaseFoodHelper? = null
    var food_db_adapter: Food_DB_Adapter? = null
    var addfood: CardView? = null
    var date_cross: ImageButton? = null
    var time_cross: ImageButton? = null
    var time_in_min = 0
    var recyclerView: RecyclerView? = null
    var category = ArrayList<String>()
    var noItem:TextView?=null
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        if(food_db_adapter!!.itemCount==0){
            noItem?.visibility=View.VISIBLE
        }
        else{
            noItem?.visibility=View.INVISIBLE

        }


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_predict, container, false)
        arrayList = ArrayList()
        date = view.findViewById(R.id.date_button)
        time = view.findViewById(R.id.time_button)
        date_text = view.findViewById(R.id.date_text)
        time_text = view.findViewById(R.id.time_text)
        date_cross = view.findViewById(R.id.date_cross)
        time_cross = view.findViewById(R.id.time_cross)
        recyclerView = view.findViewById(R.id.recycle)
        recyclerView!!.setHasFixedSize(true)
        category_button = view.findViewById(R.id.category_button)
        category_ = view.findViewById(R.id.category)
        addfood = view.findViewById(R.id.add_food)
        predict_button = view.findViewById(R.id.predict_button)
        curr_bg = view.findViewById(R.id.current_bg)
        food_db_adapter = Food_DB_Adapter(activity, arrayList!!)
        recyclerView!!.setAdapter(food_db_adapter)
        recyclerView!!.setLayoutManager(LinearLayoutManager(activity))
        dataBaseFoodHelper = DataBaseFoodHelper(activity)
        food_db_adapter!!.notifyDataSetChanged()
        category.add("Breakfast")
        category.add("Lunch")
        category.add("Dinner")
        category.add("Snack")
        noItem=view.findViewById(R.id.no_item)
        if(food_db_adapter!!.itemCount==0){
            noItem?.visibility=View.VISIBLE
        }
        else{
            noItem?.visibility=View.INVISIBLE

        }

        val mcurrenttime = Calendar.getInstance()
        val year = mcurrenttime[Calendar.YEAR]
        val month = mcurrenttime[Calendar.MONTH]
        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
        val default_date = day.toString() + "/" + (month + 1) + "/" + year
        val hour = mcurrenttime[Calendar.HOUR_OF_DAY]
        val minute = mcurrenttime[Calendar.MINUTE]
        val default_time = "$hour:$minute"
        time_in_min = hour * 60 + minute
        val preferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        val pre_category = preferences.getString(PREDICT_SCREEN_CATEGORY, null)
        val pre_bg = preferences.getString(CURRENT_BG, null)
        pre_date = preferences.getString(CURRENT_DATE, null)
        pre_time = preferences.getString(CURRENT_TIME, null)
        if (pre_category != null) {
            category_!!.setText(pre_category)
        }
        if (pre_bg != null) {
            curr_bg!!.setText(pre_bg)
        }
        if (pre_date != null) {
            date_text!!.setText(pre_date)
        } else {
            date_text!!.setText(default_date)
        }
        if (pre_time != null) {
            time_text!!.setText(pre_time)
        } else {
            time_text!!.setText("Time")
        }
        setUpViews()
        category_button!!.setOnClickListener(View.OnClickListener {
            val alertDialog = AlertDialog.Builder(
                activity
            ).create()
            alertDialog.setCancelable(true)
            val layoutInflater = LayoutInflater.from(activity)
            val convertView: View = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
            alertDialog.setTitle("Category")
            lv = convertView.findViewById<View>(R.id.lv) as ListView
            val adapter = ArrayAdapter(activity,android.R.layout.simple_list_item_1, category)
            lv!!.adapter = adapter
            lv!!.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    val selectedFromList = lv!!.getItemAtPosition(i).toString()
                    //                        Log.i("Check2", selectedFromList + "  " + i);
                    category_?.setText(selectedFromList)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(
                        activity
                    )
                    val editor = preferences.edit()
                    editor.putString(PREDICT_SCREEN_CATEGORY, selectedFromList)
                    editor.apply()
                    editor.commit()
                    alertDialog.cancel()
                }
            alertDialog.setView(convertView)
            alertDialog.show()
        })
        //        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, final int position) {
//                ImageButton button = view.findViewById(R.id.button);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
//                        b.setTitle("Alert");
//                        b.setMessage("Are you sure you want to delete this food item?");
//                        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int index) {
//
//                                DataBaseFoodHelper helper = new DataBaseFoodHelper(getActivity());
//                                SQLiteDatabase db = helper.getWritableDatabase();
//
//
//                                db.delete(FOOD_HELPER_DB_NAME, FOOD_HELPER_DB_ID + "=?",
//                                        new String[]{String.valueOf(arrayList.get(position).getId())});
//
//                                db.close();
//                                 .makeText(getActivity(), "Food item deleted!", Toast.LENGTH_SHORT).show();
//                                setUpViews();
//                            }
//                        });
//                        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//                        b.show();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
        predict_button?.setOnClickListener(View.OnClickListener {
            try {
                if (arrayList!!.size == 0) {
                    Toast.makeText(activity, "No food items added", Toast.LENGTH_SHORT).show()
                } else if (curr_bg!!.getText() == null || curr_bg!!.getText().toString() == "") {
                    Toast.makeText(activity, "Enter Current BG", Toast.LENGTH_SHORT).show()
                } else if (!isNetworkAvailable) {
                    Toast.makeText(activity, "No connection", Toast.LENGTH_SHORT).show()
                } else if (category_!!.getText() == null || category_!!.getText() == "" || category_!!.getText()
                        .toString() == "Food Category"
                ) {
                    Toast.makeText(activity, "Enter Category", Toast.LENGTH_SHORT).show()
                } else {

                    //                    try {
                    //
                    //                        AddIntoLog();
                    //                    } catch (Exception e) {
                    //


                    //                        Toast.makeText(getActivity(), "Error in log", Toast.LENGTH_SHORT).show();
                    //
                    //                    }
                    val preferences = PreferenceManager.getDefaultSharedPreferences(
                        activity
                    )
                    val editor = preferences.edit()
                    editor.putString(CURRENT_BG, curr_bg?.getText().toString())
                    editor.putFloat(TOTAL_CARBS_FOR_PREDICTION, amount)
                    editor.putString(CURRENT_DATE, date_text?.getText().toString())
                    editor.putString(CURRENT_TIME, time_text?.getText().toString())
                    val sending_curr = System.currentTimeMillis()
                    editor.putString(TIME_IN_MIN_INSULIN, sending_curr.toString() + "")
                    editor.apply()
                    editor.commit()
                    val i = Intent()
                    i.setClass(activity, PredictionDisplay::class.java)
                    startActivity(i)
                }
            } catch (e: Exception) {
            }
        } //
            //
        )
        //        record_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (arrayList.size() == 0) {
//
//                    Toast.makeText(getActivity(), "No food items added", Toast.LENGTH_SHORT).show();
//
//                } else if (category_.getText() == null || category_.getText().equals("") || category_.getText().toString().equals("Food Category")) {
//                    Toast.makeText(getActivity(), "Enter Category", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    try {
//                        AddDirectIntoLog();
//
//                    } catch (Exception e) {
//
//                        Toast.makeText(getActivity(), "Error in direct log", Toast.LENGTH_SHORT).show();
//
//                    }
//                    category_.setText("Food Category");
//
//                    arrayList.clear();
//                    food_db_adapter.notifyDataSetChanged();
//                    curr_bg.setText("");
//                    date_text.setText("Date");
//                    time_text.setText("Time");
//
//
//                }
//            }
//        });
        date?.setOnClickListener(View.OnClickListener {
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            //                current_date = day + "/" + (month + 1) + "/" + year;
            //                present_date_compare = day + "/" + (month + 1) + "/" + year;
            val dialog = DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog,
                { datePicker, year, month, day ->
                    val final_date = day.toString() + "/" + (month + 1) + "/" + year
                    date_text?.setText(final_date)
                }, year, month, day
            )
            val minDay = day-7
            val minMonth = month
            val minYear = year
            mcurrenttime.set(minYear, minMonth, minDay)
            dialog.datePicker.minDate = mcurrenttime.timeInMillis

            val maxDay = day
            val maxMonth = month
            val maxYear = year
            mcurrenttime.set(maxYear, maxMonth, maxDay)
            dialog.datePicker.maxDate = mcurrenttime.timeInMillis

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        })
        time?.setOnClickListener(object : View.OnClickListener {
            var current_time = Calendar.getInstance()
            var hour = current_time[Calendar.HOUR_OF_DAY]
            var minute = current_time[Calendar.MINUTE]

            //            String current__time = hour + ":" + minute;
            override fun onClick(view: View) {
                val timePickerDialog = TimePickerDialog(activity,android.R.style.Theme_Holo_Light_Dialog,
                    { timePicker, i, i1 ->
                        time_text?.setText("$i:$i1")
                        //                        final_time = i + ":" + i1;
                        time_in_min = i * 60 + i1
                    }, hour, minute, true
                )
                timePickerDialog.show()
            }
        })
        date_cross?.setOnClickListener(View.OnClickListener { date_text?.setText("Date") })
        time_cross?.setOnClickListener(View.OnClickListener { time_text?.setText("Time") })
        addfood?.setOnClickListener(View.OnClickListener {
            if (curr_bg!!.getText() != null || curr_bg!!.getText().toString() != "") {
                val preferences = PreferenceManager.getDefaultSharedPreferences(
                    activity
                )
                val editor = preferences.edit()
                editor.putString(CURRENT_BG, curr_bg!!.getText().toString())
                editor.putString(CURRENT_DATE, date_text!!.getText().toString())
                editor.putString(CURRENT_TIME, time_text!!.getText().toString())
                editor.apply()
                editor.commit()
            }
            val i = Intent()
            i.setClass(activity, FoodAdd::class.java)
            startActivity(i)
        })
        return view
    }

    //    private void AddDirectIntoLog() {
    //        if (curr_bg.getText() != null || !curr_bg.getText().toString().equals("")) {
    //            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    //            SharedPreferences.Editor editor = preferences.edit();
    //            editor.putString(CURRENT_BG, curr_bg.getText().toString());
    //            editor.apply();
    //            editor.commit();
    //
    //        }
    //
    //        SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    //        String storeddate = getpreferences.getString(STORED_DATE, null);
    //        Calendar mcurrenttime = Calendar.getInstance();
    //        int year = mcurrenttime.get(Calendar.YEAR);
    //        int month = mcurrenttime.get(Calendar.MONTH);
    //        int day = mcurrenttime.get(Calendar.DAY_OF_MONTH);
    //        String current_date = day + "/" + (month + 1) + "/" + year;
    //        Log.i("date_issue", storeddate + " in predict " + current_date);
    //        if (storeddate == null || !storeddate.equals(current_date)) {
    //
    //            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    //            SharedPreferences.Editor editor = preferences.edit();
    //
    //            editor.putString(STORED_DATE, current_date);
    //
    //            editor.apply();
    //            editor.commit();
    //
    //
    //            int count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0);
    //            for (int i = 1; i <= count_exercise; i++) {
    //                editor.remove(FIXED_WORD_EXERCISE + i);
    //                editor.remove(FIXED_EXERCISE_TIME + i);
    //            }
    //
    //            int count_insulin = getpreferences.getInt(KEEP_COUNT_INSULIN, 0);
    //            for (int i = 1; i <= count_insulin; i++) {
    //                editor.remove(FIXED_INSULIN + i);
    //            }
    //
    //
    //            DataBaseFoodHelperLog dataBaseFoodHelperLog = new DataBaseFoodHelperLog(getActivity());
    //            SQLiteDatabase db = dataBaseFoodHelperLog.getWritableDatabase();
    //            db.delete(FOOD_HELPER_DB_NAME_LOG, null, null);
    //
    //            int count_bg = getpreferences.getInt(KEEP_COUNT_BG, 0);
    //            for (int i = 1; i <= count_bg; i++) {
    //                editor.remove(FIXED_BG + i);
    //            }
    //            editor.putInt(KEEP_COUNT_EXERCISE, 0);
    //            editor.putInt(KEEP_COUNT_BG, 0);
    //            editor.putInt(KEEP_COUNT_INSULIN, 0);
    //            editor.apply();
    //            editor.commit();
    //
    //
    //        }
    //
    //
    //        SQLiteDatabase dbread = dataBaseFoodHelper.getReadableDatabase();
    //        Cursor cread = dbread.query(FOOD_HELPER_DB_NAME, null, null, null, null, null, null);
    //        while (cread.moveToNext()) {
    //            food_name = cread.getString(cread.getColumnIndex(FOOD_DB_NAME));
    //            time_stamp = cread.getString(cread.getColumnIndex(FOOD_DB_TIME_STAMP));
    //            food_carbs = cread.getString(cread.getColumnIndex(CARBS));
    //            quantity = cread.getString(cread.getColumnIndex(FOOD_DB_QUANTITY));
    //            food_fat = cread.getString(cread.getColumnIndex(FAT));
    //            food_protein = cread.getString(cread.getColumnIndex(PROTEIN));
    //            food_calories = cread.getString(cread.getColumnIndex(CALORIES));
    //
    //
    //            DataBaseFoodHelperLog dataBaseFoodHelperLog = new DataBaseFoodHelperLog(getActivity());
    //            SQLiteDatabase db = dataBaseFoodHelperLog.getWritableDatabase();
    //            ContentValues cv = new ContentValues();
    //            cv.put(FOOD_DB_NAME, food_name);
    //            cv.put(FOOD_DB_TIME_STAMP, time_stamp + "");
    //            cv.put(FOOD_DB_QUANTITY, quantity);
    //            cv.put(USER_DATE_ENTERED, date_text.getText().toString());
    //            cv.put(USER_TIME_ENTERED, time_text.getText().toString());
    //            cv.put(DB_CATEGORY_ENTERED, category_.getText().toString());
    //            cv.put(FAT, food_fat);
    //            cv.put(CARBS, food_carbs);
    //            cv.put(PROTEIN, food_protein);
    //            cv.put(CALORIES, food_calories);
    //
    //            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    //            String key = preferences.getString(KEY, null);
    //
    //            Calendar current_time = Calendar.getInstance();
    //            int hour = current_time.get(Calendar.HOUR_OF_DAY);
    //            int minute = current_time.get(Calendar.MINUTE);
    //            String curr__time = hour + ":" + minute;
    //            long time_as_timestamp = System.currentTimeMillis();
    ////            Log.i("final_testing_firebase", key+" | "+time_as_timestamp+" | "+food_name + " | " + time_stamp + " | " + quantity + " | " + date_text.getText().toString() + " | " + time_text.getText().toString() + " | " + category_.getText().toString());
    //
    //            foodref.child(key).child(time_as_timestamp + "").child("DATE").setValue(date_text.getText().toString());
    //            foodref.child(key).child(time_as_timestamp + "").child("FOOD_CATEGORY").setValue(category_.getText().toString());
    //            foodref.child(key).child(time_as_timestamp + "").child("FOOD_NAME").setValue(food_name);
    //            foodref.child(key).child(time_as_timestamp + "").child("QUANTITY").setValue(quantity);
    //            foodref.child(key).child(time_as_timestamp + "").child("TIME").setValue(time_text.getText().toString());
    //            foodref.child(key).child(time_as_timestamp + "").child("CURRENT_DATE").setValue(current_date);
    //            foodref.child(key).child(time_as_timestamp + "").child("CURRENT_TIME").setValue(curr__time);
    //
    //
    //            db.insert(FOOD_HELPER_DB_NAME_LOG, null, cv);
    //
    //
    //        }
    //
    //        dbread.delete(FOOD_HELPER_DB_NAME, null, null);
    //
    //    }
    @SuppressLint("Range")
    private fun AddIntoLog() {
        if (curr_bg!!.text != null || curr_bg!!.text.toString() != "") {
            val preferences = PreferenceManager.getDefaultSharedPreferences(
                activity
            )
            val editor = preferences.edit()
            editor.putString(CURRENT_BG, curr_bg!!.text.toString())
            editor.apply()
            editor.commit()
        }
        val getpreferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        val storeddate = getpreferences.getString(STORED_DATE, null)
        val mcurrenttime = Calendar.getInstance()
        val year = mcurrenttime[Calendar.YEAR]
        val month = mcurrenttime[Calendar.MONTH]
        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
        val current_date = day.toString() + "/" + (month + 1) + "/" + year
        Log.i("date_issue", "$storeddate in predict $current_date")
        if (storeddate == null || storeddate != current_date) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(
                activity
            )
            val editor = preferences.edit()
            editor.putString(STORED_DATE, current_date)
            editor.apply()
            editor.commit()
            val count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
            for (i in 1..count_exercise) {
                editor.remove(FIXED_WORD_EXERCISE + i)
                editor.remove(FIXED_EXERCISE_TIME + i)
            }
            val count_insulin = getpreferences.getInt(KEEP_COUNT_INSULIN, 0)
            for (i in 1..count_insulin) {
                editor.remove(FIXED_INSULIN + i)
            }
            val dataBaseFoodHelperLog = DataBaseFoodHelperLog(activity)
            val db = dataBaseFoodHelperLog.writableDatabase
            db.delete(FOOD_HELPER_DB_NAME_LOG, null, null)
            val count_bg = getpreferences.getInt(KEEP_COUNT_BG, 0)
            for (i in 1..count_bg) {
                editor.remove(FIXED_BG + i)
            }
            editor.putInt(KEEP_COUNT_EXERCISE, 0)
            editor.putInt(KEEP_COUNT_BG, 0)
            editor.putInt(KEEP_COUNT_INSULIN, 0)
            editor.apply()
            editor.commit()
        }
        //
//
        val dbread = dataBaseFoodHelper!!.readableDatabase
        val cread = dbread.query(FOOD_HELPER_DB_NAME, null, null, null, null, null, null)
        while (cread.moveToNext()) {
            food_name = cread.getString(cread.getColumnIndex(FOOD_DB_NAME))
            time_stamp = cread.getString(cread.getColumnIndex(FOOD_DB_TIME_STAMP))
            food_carbs = cread.getString(cread.getColumnIndex(CARBS))
            quantity = cread.getString(cread.getColumnIndex(FOOD_DB_QUANTITY))
            food_fat = cread.getString(cread.getColumnIndex(FAT))
            food_protein = cread.getString(cread.getColumnIndex(PROTEIN))
            food_calories = cread.getString(cread.getColumnIndex(CALORIES))
            val dataBaseFoodHelperLog = DataBaseFoodHelperLog(activity)
            val db = dataBaseFoodHelperLog.writableDatabase
            val cv = ContentValues()
            cv.put(FOOD_DB_NAME, food_name)
            cv.put(FOOD_DB_TIME_STAMP, time_stamp + "")
            cv.put(FOOD_DB_QUANTITY, quantity)
            cv.put(USER_DATE_ENTERED, date_text!!.text.toString())
            cv.put(USER_TIME_ENTERED, time_text!!.text.toString())
            cv.put(DB_CATEGORY_ENTERED, category_!!.text.toString())
            cv.put(FAT, food_fat)
            cv.put(CARBS, food_carbs)
            cv.put(PROTEIN, food_protein)
            cv.put(CALORIES, food_calories)
            val preferences = PreferenceManager.getDefaultSharedPreferences(
                activity
            )
            val key = preferences.getString(KEY, null)
            val current_time = Calendar.getInstance()
            val hour = current_time[Calendar.HOUR_OF_DAY]
            val minute = current_time[Calendar.MINUTE]
            val curr__time = "$hour:$minute"
            //            long time_as_timestamp = System.currentTimeMillis();
//            Log.i("final_testing_firebase", key+" | "+time_as_timestamp+" | "+food_name + " | " + time_stamp + " | " + quantity + " | " + date_text.getText().toString() + " | " + time_text.getText().toString() + " | " + category_.getText().toString());

//            foodref.child(key).child(time_stamp + "").child("DATE").setValue(date_text.getText().toString());
//            foodref.child(key).child(time_stamp + "").child("FOOD_CATEGORY").setValue(category_.getText().toString());
//            foodref.child(key).child(time_stamp + "").child("FOOD_NAME").setValue(food_name);
//            foodref.child(key).child(time_stamp + "").child("QUANTITY").setValue(quantity);
//            foodref.child(key).child(time_stamp + "").child("TIME").setValue(time_text.getText().toString());
//            foodref.child(key).child(time_stamp + "").child("CURRENT_DATE").setValue(current_date);
//            foodref.child(key).child(time_stamp + "").child("CURRENT_TIME").setValue(curr__time);
//
//            preref.child(key).child(time_stamp + "").child("DATE").setValue(date_text.getText().toString());
//            preref.child(key).child(time_stamp + "").child("FOOD_CATEGORY").setValue(category_.getText().toString());
//            preref.child(key).child(time_stamp + "").child("FOOD_NAME").setValue(food_name);
//            preref.child(key).child(time_stamp + "").child("QUANTITY").setValue(quantity);
//            preref.child(key).child(time_stamp + "").child("TIME").setValue(time_text.getText().toString());
//            preref.child(key).child(time_stamp + "").child("CURRENT_DATE").setValue(current_date);
//            preref.child(key).child(time_stamp + "").child("CURRENT_TIME").setValue(curr__time);
//            preref.child(key).child(time_stamp + "").child("CURRENT_BG").setValue(curr_bg.getText().toString());
            db.insert(FOOD_HELPER_DB_NAME_LOG, null, cv)
        }

//        dbread.delete(FOOD_HELPER_DB_NAME, null, null);
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    @SuppressLint("Range")
    fun setUpViews() {
        amount = 0f
        Log.i("TAG4", "setUpViews: ")
        try {
            val dp = dataBaseFoodHelper!!.readableDatabase
            arrayList!!.clear()
            val c = dp.query(FOOD_HELPER_DB_NAME, null, null, null, null, null, null)
            while (c.moveToNext()) {
                Log.i("TAG5", "setUpViews: ")
                food_name = c.getString(c.getColumnIndex(FOOD_DB_NAME))
                food_carbs = c.getString(c.getColumnIndex(CARBS))
                //            String food_category="lets see";
                quantity = c.getString(c.getColumnIndex(FOOD_DB_QUANTITY))
                val tempcal =
                    java.lang.Float.valueOf(quantity) * java.lang.Float.valueOf(food_carbs)
                val amountroundOff =
                    (Math.round(java.lang.Float.valueOf(tempcal) * 100.0) / 100.0).toFloat()
                amount += tempcal
                id_ = c.getInt(c.getColumnIndex(FOOD_HELPER_DB_ID))
                val foodUser = FoodUser(food_name, quantity!!, amountroundOff.toString() + "", id_)

                //            Reminder reminder2 = new Reminder(id,title, description, date, time,boottime);
//            reminder.add(reminder2);
                arrayList!!.add(foodUser)
            }
            food_db_adapter!!.notifyDataSetChanged()
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in setting up food items", Toast.LENGTH_SHORT).show()
        }
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Predict.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Predict {
            val fragment = Predict()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}