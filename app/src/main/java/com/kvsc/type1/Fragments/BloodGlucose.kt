package com.kvsc.type1.Fragments
import android.app.AlertDialog
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.firebase.database.FirebaseDatabase
import com.kvsc.type1.Activities.DataBaseFoodHelperLog
import com.kvsc.type1.R
import java.util.*

import com.kvsc.type1.ClassesHere.Constants.FIXED_EXERCISE_TIME
import com.kvsc.type1.ClassesHere.Constants.FIXED_INSULIN
import com.kvsc.type1.ClassesHere.Constants.FIXED_WORD_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_INSULIN
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.STORED_DATE

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BloodGlucose.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BloodGlucose.newInstance] factory method to
 * create an instance of this fragment.
 */
class BloodGlucose : Fragment() {
    var blood_glucose: CardView? = null
    var present_date_compare: String? = null
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("blood_glucose")
    var submit: Button? = null
    var date_cross: ImageButton? = null
    var time_cross: ImageButton? = null
    var dataBaseFoodHelperlog: DataBaseFoodHelperLog? = null
    var date: CardView? = null
    var time: CardView? = null
    var current_date: String? = null
    var current_time2: String? = null
    var date_text: TextView? = null
    var time_text: TextView? = null
    var blood_glucose_text: TextView? = null
    var value: EditText? = null
    var final_date = ""
    var final_time = ""
    var final_glucose: String? = ""
    var current_time = Calendar.getInstance()
    var datePickerDialog: DatePickerDialog? = null
    var timepicker: TimePickerDialog? = null
    var lv: ListView? = null
    var items = ArrayList<String>()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_blood_glucose, container, false)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        value = view.findViewById(R.id.value)
        date = view.findViewById(R.id.date_button)
        time = view.findViewById(R.id.time_button)
        date_text = view.findViewById(R.id.date_text)
        blood_glucose_text = view.findViewById(R.id.blood_glucose_text)
        time_text = view.findViewById(R.id.time_text)
        submit = view.findViewById(R.id.submit_button)
        date_cross = view.findViewById(R.id.date_cross)
        time_cross = view.findViewById(R.id.time_cross)
        blood_glucose = view.findViewById(R.id.blood_glucose)
        val mcurrenttime = Calendar.getInstance()
        val year = mcurrenttime[Calendar.YEAR]
        val month = mcurrenttime[Calendar.MONTH]
        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
        present_date_compare = day.toString() + "/" + (month + 1) + "/" + year
        val default_date = day.toString() + "/" + (month + 1) + "/" + year
        val hour = mcurrenttime[Calendar.HOUR_OF_DAY]
        val minute = mcurrenttime[Calendar.MINUTE]
        val default_time = "$hour:$minute"
        date_text!!.setText(default_date)
        time_text!!.setText("Time")
        dataBaseFoodHelperlog = DataBaseFoodHelperLog(activity)
        items.add("Fasting")
        items.add("2 hrs after breakfast")
        items.add("Before lunch")
        items.add("2 hrs after lunch")
        items.add("Before dinner")
        items.add("2 hrs after dinner")
        items.add("Random")
        items.add("3:00 am")
        date_cross?.setOnClickListener(View.OnClickListener {
            date_text?.setText("Date")
            final_date = ""
        })
        time_cross?.setOnClickListener(View.OnClickListener {
            time_text?.setText("Time")
            final_time = ""
        })
        date?.setOnClickListener(View.OnClickListener {
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            current_date = day.toString() + "/" + (month + 1) + "/" + year
            present_date_compare = day.toString() + "/" + (month + 1) + "/" + year
            val dialog = DatePickerDialog(activity,android.R.style.Theme_Holo_Light_Dialog,
                { datePicker, year, month, day ->
                    final_date = day.toString() + "/" + (month + 1) + "/" + year
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
        time!!.setOnClickListener(object : View.OnClickListener {
            var hour = current_time[Calendar.HOUR_OF_DAY]
            var minute = current_time[Calendar.MINUTE]
            override fun onClick(view: View) {
                current_time2 = "$hour:$minute"
                val timePickerDialog = TimePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog,
                    { timePicker, i, i1 ->
                        time_text?.setText("$i:$i1")
                        final_time = "$i:$i1"
                    }, hour, minute, true
                )
                timePickerDialog.show()
            }
        })
        blood_glucose!!.setOnClickListener(View.OnClickListener {
            val alertDialog = AlertDialog.Builder(
                activity
            ).create()
            alertDialog.setCancelable(true)
            val layoutInflater = LayoutInflater.from(activity)
            val convertView: View = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
            alertDialog.setTitle("Blood Glucose Type")
            lv = convertView.findViewById<View>(R.id.lv) as ListView
            val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
            lv!!.adapter = adapter
            lv!!.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    val selectedFromList = lv!!.getItemAtPosition(i).toString()
                    //                        Log.i("Check2", selectedFromList + "  " + i);
                    blood_glucose_text?.setText(selectedFromList)
                    alertDialog.cancel()
                }
            alertDialog.setView(convertView)
            alertDialog.show()
        })
        submit?.setOnClickListener(View.OnClickListener {
            try {
                final_glucose = value?.getText().toString()
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
                    } else if ("Blood glucose type" == blood_glucose_text?.getText()
                            .toString()
                    ) {
                        Toast.makeText(activity, "Enter BG type", Toast.LENGTH_SHORT).show()
                    } else if (final_glucose == null || final_glucose!!.isEmpty()) {
                        Toast.makeText(activity, "Enter BG Value", Toast.LENGTH_SHORT).show()
                    } else if ("" == date_text?.getText().toString()) {
                        Toast.makeText(activity, "Enter date", Toast.LENGTH_SHORT).show()
                    } else if ("" == time_text?.getText().toString()) {
                        Toast.makeText(activity, "Enter time", Toast.LENGTH_SHORT).show()
                    } else {
                        val preferences = PreferenceManager.getDefaultSharedPreferences(
                            activity
                        )
                        val key = preferences.getString(KEY, null)
                        val time = System.currentTimeMillis()
                        if (key == null) {
                            Toast.makeText(
                                activity,
                                "Something went wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            myRef.child(key).child(time.toString() + "")
                                .child("BLOOD_GLUCOSE_TYPE")
                                .setValue(blood_glucose_text!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("VALUE")
                                .setValue(value!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("DATE")
                                .setValue(date_text!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("TIME")
                                .setValue(time_text!!.getText().toString())
                            myRef.child(key).child(time.toString() + "").child("CURRENT_TIME")
                                .setValue(default_time)
                            myRef.child(key).child(time.toString() + "").child("CURRENT_DATE")
                                .setValue(default_date)
                            val getpreferences = PreferenceManager.getDefaultSharedPreferences(
                                activity
                            )
                            val preferences_contents =
                                PreferenceManager.getDefaultSharedPreferences(
                                    activity
                                )
                            val editor = preferences_contents.edit()
                            var storeddate = getpreferences.getString(STORED_DATE, null)
                            val carb_count = 0.0f
                            val fat_count = 0.0f
                            val protein_count = 0.0f
                            if (storeddate == null || storeddate != present_date_compare) {
                                //                                Log.i("checking21", storeddate + " " + present_date_compare);
                                //                                        carb_count = 0.0f;
                                //                                        fat_count = 0.0f;
                                //                                        protein_count = 0.0f;
                                val dbread = dataBaseFoodHelperlog!!.readableDatabase
                                dbread.delete(FOOD_HELPER_DB_NAME_LOG, null, null)


                                //                                        editor.putFloat(FAT, fat_count);
                                //                                        editor.putFloat(CARBS, carb_count);
                                //                                        editor.putFloat(PROTEIN, protein_count);
                                storeddate = present_date_compare
                                editor.putString(STORED_DATE, storeddate)
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
                                val count_insulin = getpreferences.getInt(KEEP_COUNT_INSULIN, 0)
                                for (i in 1..count_insulin) {
                                    editor.remove(FIXED_INSULIN + i)
                                }
                                editor.putInt(KEEP_COUNT_EXERCISE, 0)
                                //                                        editor.putInt(KEEP_COUNT, 0);
                                editor.putInt(KEEP_COUNT_INSULIN, 0)
                            }
                            editor.apply()
                            editor.commit()
                            final_date = ""
                            final_time = ""
                            date_text!!.setText("Date")
                            time_text!!.setText("Time")
                            value!!.setText("")
                            final_time = ""
                            final_date = ""
                            blood_glucose_text!!.setText("Blood glucose type")
                            Toast.makeText(activity, "Data submitted", Toast.LENGTH_SHORT)
                                .show()
                            dialogInterface.cancel()
                        }
                    }
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
         * @return A new instance of fragment BloodGlucose.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): BloodGlucose {
            val fragment = BloodGlucose()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}