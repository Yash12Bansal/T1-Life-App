package com.kvsc.type1.Fragments

import android.app.AlertDialog
import com.kvsc.type1.R
import android.app.DatePickerDialog
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.Context
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
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.firebase.database.*
//import com.victor.loading.rotate.RotateLoading
import com.kvsc.type1.Activities.DataBaseFoodHelperLog
import com.kvsc.type1.ClassesHere.Constants.CURRENT_BG
import com.kvsc.type1.ClassesHere.Constants.CURRENT_DATE
import com.kvsc.type1.ClassesHere.Constants.PREDICT_SCREEN_CATEGORY
import com.kvsc.type1.ClassesHere.Constants.CURRENT_TIME
import com.kvsc.type1.ClassesHere.Constants.FIXED_EXERCISE_TIME
import com.kvsc.type1.ClassesHere.Constants.FIXED_WORD_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.STORED_DATE
import com.kvsc.type1.ClassesHere.Constants.TIME_IN_MIN_INSULIN
import com.kvsc.type1.ClassesHere.Constants.TOTAL_CARBS_FOR_PREDICTION
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Insulin.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Insulin.newInstance] factory method to
 * create an instance of this fragment.
 */
class Insulin : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    var time1: Long = 0
    var time2: Long = 0
    var time3: Long = 0
    var category_button: CardView? = null
    var time_in_min: String? = null
    var prev_date: String? = null
    var prev_time: String? = null
    var dataBaseFoodHelperlog: DataBaseFoodHelperLog? = null
    var present_date_compare: String? = null
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("insulin")
    var type: Spinner? = null
    var database2 = FirebaseDatabase.getInstance()
    var myRef2 = database2.getReference("exercise_entry/Prediction_values")
    var submit: Button? = null
    var date_cross: ImageButton? = null
    var time_cross: ImageButton? = null
    var went_in = false
    var date: CardView? = null
    var time: CardView? = null
    var rotateLoading: ProgressBar? = null
    var date_text: TextView? = null
    var time_text: TextView? = null
    var pre_meal_text: TextView? = null
    var pre_exercise_text: TextView? = null
    var category_text: TextView? = null
    var final_date = ""
    var final_time = ""
    var current_date: String? = null
    var current__time: String? = null
    var current_time = Calendar.getInstance()
    var datePickerDialog: DatePickerDialog? = null
    var timepicker: TimePickerDialog? = null
    var lv: ListView? = null
    var preref = database.getReference("exercise_entry/Prediction_values")
    var insulin_amount: EditText? = null
    var correction_dose: EditText? = null
    var category = ArrayList<String>()
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_insulin, container, false)
        type = view.findViewById(R.id.insulin_type_spinner)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        date = view.findViewById(R.id.date_button)
        time = view.findViewById(R.id.time_button)
        date_text = view.findViewById(R.id.date_text)
        time_text = view.findViewById(R.id.time_text)
        submit = view.findViewById(R.id.submit_button)
        insulin_amount = view.findViewById(R.id.amount_insulin)
        correction_dose = view.findViewById(R.id.correction_dose)
        category_text = view.findViewById(R.id.category)
        category_button = view.findViewById(R.id.category_button)
        date_cross = view.findViewById(R.id.date_cross)
        time_cross = view.findViewById(R.id.time_cross)
        rotateLoading = view.findViewById<View>(R.id.rotateloading) as ProgressBar
        rotateLoading?.visibility=View.INVISIBLE
        dataBaseFoodHelperlog = DataBaseFoodHelperLog(activity)
        val preferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        time_in_min = preferences.getString(TIME_IN_MIN_INSULIN, null)
        prev_date = preferences.getString(CURRENT_DATE, null)
        prev_time = preferences.getString(CURRENT_TIME, null)
        category.add("Pre-exercise")
        category.add("Pre-breakfast")
        category.add("Pre-lunch")
        category.add("Pre-dinner")
        category.add("Bed-time")
        category.add("Pre-snack")
        val spinnerAdapter =
            ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, android.R.id.text1)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type!!.setAdapter(spinnerAdapter)
        spinnerAdapter.add("Select")
        spinnerAdapter.add("Long acting")
        spinnerAdapter.add("Short acting")
        spinnerAdapter.add("Pre-mix")
        spinnerAdapter.add("Rapid acting")
        spinnerAdapter.add("Regular")
        spinnerAdapter.notifyDataSetChanged()
        val mcurrenttime = Calendar.getInstance()
        val year = mcurrenttime[Calendar.YEAR]
        val month = mcurrenttime[Calendar.MONTH]
        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
        val default_date = day.toString() + "/" + (month + 1) + "/" + year
        present_date_compare = day.toString() + "/" + (month + 1) + "/" + year
        val hour = mcurrenttime[Calendar.HOUR_OF_DAY]
        val minute = mcurrenttime[Calendar.MINUTE]
        val default_time = "$hour:$minute"
        date_text!!.setText(default_date)
        time_text!!.setText("Time")
        date!!.setOnClickListener(View.OnClickListener {
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            current_date = day.toString() + "/" + (month + 1) + "/" + year
            present_date_compare = day.toString() + "/" + (month + 1) + "/" + year
            val dialog = DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog,
                { datePicker, year, month, day ->
                    final_date = day.toString() + "/" + (month + 1) + "/" + year
                    date_text!!.setText(final_date)
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
        time!!.setOnClickListener(object : View.OnClickListener {
            var hour = current_time[Calendar.HOUR_OF_DAY]
            var minute = current_time[Calendar.MINUTE]
            override fun onClick(view: View) {
                current__time = "$hour:$minute"
                val timePickerDialog = TimePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog,
                    { timePicker, i, i1 ->
                        time_text?.setText("$i:$i1")
                        final_time = "$i:$i1"
                    }, hour, minute, true
                )
                timePickerDialog.show()
            }
        })
        date_cross?.setOnClickListener(View.OnClickListener {
            date_text!!.setText("Date")
            final_date = ""
        })
        time_cross!!.setOnClickListener(View.OnClickListener {
            time_text!!.setText("Time")
            final_time = ""
        })
        category_button!!.setOnClickListener(View.OnClickListener {
            val alertDialog = AlertDialog.Builder(
                activity
            ).create()
            alertDialog.setCancelable(true)
            val layoutInflater = LayoutInflater.from(activity)
            val convertView: View = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
            alertDialog.setTitle("Category")
            lv = convertView.findViewById<View>(R.id.lv) as ListView
            val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, category)
            lv!!.adapter = adapter
            lv!!.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    val selectedFromList = lv!!.getItemAtPosition(i).toString()
                    //                        Log.i("Check2", selectedFromList + "  " + i);
                    category_text!!.setText(selectedFromList)
                    alertDialog.cancel()
                }
            alertDialog.setView(convertView)
            alertDialog.show()
        })
        submit!!.setOnClickListener(View.OnClickListener {
            try {
                val b = AlertDialog.Builder(
                    activity
                )
                b.setTitle("Alert")
                b.setMessage("Are you sure you want to submit ?")
                b.setPositiveButton(
                    "Yes"
                ) { dialogInterface, index ->
                    if (!isNetworkAvailable) {
                        Toast.makeText(activity, "No connection", Toast.LENGTH_SHORT).show()
                    } else if ("Category" == type?.getSelectedItem().toString()) {
                        Toast.makeText(activity, "Select Category", Toast.LENGTH_SHORT).show()
                    } else if ("Select" == type?.getSelectedItem()) {
                        Toast.makeText(activity, "Select Insulin Type", Toast.LENGTH_SHORT)
                            .show()
                    } else if (insulin_amount!!.getText()
                            .toString() == null || insulin_amount!!.getText().toString().isEmpty()
                    ) {
                        Toast.makeText(activity, "Enter Insulin Amount", Toast.LENGTH_SHORT)
                            .show()
                    } else if (correction_dose!!.getText()
                            .toString() == null || correction_dose!!.getText().toString()
                            .isEmpty()
                    ) {
                        Toast.makeText(activity, "Enter Correction Dose", Toast.LENGTH_SHORT)
                            .show()
                    } else if ("Category" == category_text!!.getText().toString()) {
                        Toast.makeText(activity, "Enter Category", Toast.LENGTH_SHORT).show()
                    } else {
                        val preferences = PreferenceManager.getDefaultSharedPreferences(
                            activity
                        )
                        val key = preferences.getString(KEY, null)
                        if (key == null) {
                            Toast.makeText(
                                activity,
                                "Something went wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val time = System.currentTimeMillis()
                            val hour_current = current_time[Calendar.HOUR_OF_DAY]
                            val minute_current = current_time[Calendar.MINUTE]
                            val current_date_send = Calendar.getInstance()
                            val year_current = current_date_send[Calendar.YEAR]
                            val month_current = current_date_send[Calendar.MONTH]
                            val day_current = current_date_send[Calendar.DAY_OF_MONTH]
                            myRef.child(key).child(time.toString() + "").child("AMOUNT")
                                .setValue(insulin_amount!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("CATEGORY")
                                .setValue(category_text!!.getText().toString())
                            myRef.child(key).child(time.toString() + "")
                                .child("CORRECTION_DOSE")
                                .setValue(correction_dose!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("DATE")
                                .setValue(date_text!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("TYPE")
                                .setValue(type!!.getSelectedItem().toString())
                            myRef.child(key).child(time.toString() + "").child("TIME")
                                .setValue(time_text!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("CURRENT_TIME")
                                .setValue(
                                    "$hour_current:$minute_current"
                                )
                            myRef.child(key).child(time.toString() + "").child("CURRENT_DATE")
                                .setValue(day_current.toString() + "/" + (month_current + 1) + "/" + year_current)
                            Toast.makeText(activity, "Data submitted22222222222222222222..!!", Toast.LENGTH_SHORT)

                            Log.i(
                                "testing_insulin1.0",
                                insulin_amount!!.getText().toString() + insulin_amount!!.getText()
                                    .toString().length + type!!.getSelectedItem().toString()
                            )
                            CheckExist()
                            val preferences_contents =
                                PreferenceManager.getDefaultSharedPreferences(
                                    activity
                                )
                            val editor = preferences_contents.edit()
                            editor.remove(PREDICT_SCREEN_CATEGORY)
                            editor.remove(CURRENT_BG)
                            editor.remove(TIME_IN_MIN_INSULIN)
                            editor.remove(TOTAL_CARBS_FOR_PREDICTION)
                            editor.remove(CURRENT_DATE)
                            editor.remove(CURRENT_TIME)
                            editor.apply()
                            editor.commit()
                            val getpreferences = PreferenceManager.getDefaultSharedPreferences(
                                activity
                            )
                            val storeddate = getpreferences.getString(STORED_DATE, null)
                            val carb_count = 0.0f
                            val fat_count = 0.0f
                            val protein_count = 0.0f
                            Log.i(
                                "date_issue",
                                "$storeddate in insulin $present_date_compare"
                            )
                            if (storeddate == null || storeddate != present_date_compare) {
                                //                                Log.i("checking21", storeddate + " " + present_date_compare);
                                //                                        carb_count = 0.0f;
                                //                                        fat_count = 0.0f;
                                //                                        protein_count = 0.0f;
                                try {
                                    val dbread = dataBaseFoodHelperlog!!.readableDatabase
                                    dbread?.delete(FOOD_HELPER_DB_NAME_LOG, null, null)
                                } catch (e: Exception) {
                                    Log.i("exception_here", "onClick: ")
                                }
                                //                                        editor.putFloat(FAT, fat_count);
                                //                                        editor.putFloat(CARBS, carb_count);
                                //                                        editor.putFloat(PROTEIN, protein_count);
                                //                                        storeddate = present_date_compare;
                                //                                        editor.putString(STORED_DATE, storeddate);
                                //                                        int count = getpreferences.getInt(KEEP_COUNT, 0);
                                //                                        for (int i = 1; i <= count; i++) {
                                //                                            editor.remove(FIXED_WORD + i);
                                //                                            editor.remove(FIXED_AMOUNT + i);
                                //                                            editor.remove(FOOD_CATEGORY + i);
                                //                                            editor.remove(FIXED_CARBS + i);
                                //                                            editor.remove(CALORIES + i);
                                //                                        }
                                val count_exercise =
                                    getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
                                for (i in 1..count_exercise) {
                                    editor.remove(FIXED_WORD_EXERCISE + i)
                                    editor.remove(FIXED_EXERCISE_TIME + i)
                                }
                                editor.putInt(KEEP_COUNT_EXERCISE, 0)
                                editor.putInt(KEEP_COUNT, 0)
                                editor.putInt(KEEP_COUNT_EXERCISE, 0)
                                //                                        editor.putInt(KEEP_COUNT, 0);
                            }
                            editor.apply()
                            editor.commit()
                            final_date = ""
                            final_time = ""
                        }
                    }
                    dialogInterface.cancel()
                }
                b.setNegativeButton(
                    "No"
                ) { dialogInterface, i -> dialogInterface.cancel() }
                b.show()
            } catch (e: Exception) {
                Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    private fun after_check_exist() {
        Log.i(
            "testing_insulin2,0",
            insulin_amount!!.text.toString() + insulin_amount!!.text.toString().length + type!!.selectedItem.toString()
        )
        date_text!!.text = "Date"
        time_text!!.text = "Time"
        category_text!!.text = "Category"
        insulin_amount!!.setText("")
        type!!.setSelection(0)
        correction_dose!!.setText("")
        insulin_amount!!.hint = "amount of insulin"
        correction_dose!!.hint = "correction-dose"
        rotateLoading?.visibility=View.INVISIBLE
    }

    private fun CheckExist() {
        try {
            Log.i("see_data", "check")
            rotateLoading?.visibility=View.VISIBLE
            val preferences = PreferenceManager.getDefaultSharedPreferences(
                activity
            )
            val key = preferences.getString(KEY, null)
            preref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.i("see_data", dataSnapshot.key.toString())
                    for (d in dataSnapshot.children) {
                        Log.i("see_data", d.key.toString())
                        if (d.key.toString() == key) {
//                            FirebasePredictionUser firebasePredictionUser = d.getValue(FirebasePredictionUser.class);
                            went_in = true
                            break
                        }
                    }
                    Log.i(
                        "testing_insulin",
                        insulin_amount!!.text.toString() + insulin_amount!!.text.toString().length + type!!.selectedItem.toString()
                    )
                    if (went_in) {
                        val time_current = System.currentTimeMillis()
                        if (insulin_amount!!.text.toString() != null && insulin_amount!!.text.toString().length > 0 && type!!.selectedItem.toString() == "Short acting") {
                            preref.child(key!!).child("INSULIN_DOSE")
                                .setValue(insulin_amount!!.text.toString())
                            preref.child(key).child("PREV_INSULIN_TIME")
                                .setValue(time_current.toString() + "")
                            preref.child(key).child("DIVISION_BY").setValue(4.toString() + "")
                        } else if (insulin_amount!!.text.toString() != null && insulin_amount!!.text.toString().length > 0 && type!!.selectedItem.toString() == "Regular") {
                            preref.child(key!!).child("INSULIN_DOSE")
                                .setValue(insulin_amount!!.text.toString())
                            preref.child(key).child("PREV_INSULIN_TIME")
                                .setValue(time_current.toString() + "")
                            preref.child(key).child("DIVISION_BY").setValue(6.toString() + "")
                        }
                    }
                    if (activity != null) {
                        try {
                            Toast.makeText(activity, "Data submitted..!!", Toast.LENGTH_SHORT)
                                .show()
                            after_check_exist()
                        } catch (e: Exception) {
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (e: Exception) {
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
         * @return A new instance of fragment Insulin.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Insulin {
            val fragment = Insulin()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}