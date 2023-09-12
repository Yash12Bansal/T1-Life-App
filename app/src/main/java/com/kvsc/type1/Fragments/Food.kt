package com.kvsc.type1.Fragments

import com.kvsc.type1.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.*
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
import com.google.firebase.database.FirebaseDatabase
//import com.victor.loading.rotate.RotateLoading
import com.kvsc.type1.Activities.DataBaseFoodHelper
import com.kvsc.type1.Activities.Search
import com.kvsc.type1.CustomDialog.CDialogue
import java.util.*
import java.util.regex.Pattern
import com.kvsc.type1.ClassesHere.Constants.CALORIES
import com.kvsc.type1.ClassesHere.Constants.CARBS
import com.kvsc.type1.ClassesHere.Constants.FAT
import com.kvsc.type1.ClassesHere.Constants.FIXED_AMOUNT
import com.kvsc.type1.ClassesHere.Constants.FIXED_BG
import com.kvsc.type1.ClassesHere.Constants.FIXED_CARBS
import com.kvsc.type1.ClassesHere.Constants.FIXED_EXERCISE_TIME
import com.kvsc.type1.ClassesHere.Constants.FIXED_INSULIN
import com.kvsc.type1.ClassesHere.Constants.FIXED_WORD
import com.kvsc.type1.ClassesHere.Constants.FIXED_WORD_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.FOOD_CATEGORY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_CATEGORY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_DATE
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_BG
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_INSULIN
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.PROTEIN
import com.kvsc.type1.ClassesHere.Constants.STORED_DATE


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Food.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Food.newInstance] factory method to
 * create an instance of this fragment.
 */
class Food : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    var rotateLoading: ProgressBar? = null
    var food_category: TextView? = null
    var quantity: EditText? = null
    var search: Button? = null
    var items = ArrayList<String>()
    var date: CardView? = null
    var time: CardView? = null
    var food_index = 0
    var datePickerDialog: DatePickerDialog? = null
    var current_time = Calendar.getInstance()
    var present_date_compare: String? = null
    var timepicker: TimePickerDialog? = null
    var date_text: TextView? = null
    var time_text: TextView? = null
    var mainLayout: LinearLayout? = null
    var add_food: CardView? = null
    var lv: ListView? = null

    //    Matcher pattern;
    var food_here: String? = null
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("food")
    var submit: ImageButton? = null
    var date_cross: ImageButton? = null
    var time_cross: ImageButton? = null
    var final_date = ""
    var final_time = ""
    var current_date: String? = null
    var current__time: String? = null
    var database2 = FirebaseDatabase.getInstance()
    var food_name_list = ArrayList<String>()
    var food_formulations = ArrayList<String>()
    var myRef2 = database2.getReference("food_database")
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
        savedInstanceState: Bundle
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_food, container, false)
        date = view.findViewById(R.id.date_button)
        time = view.findViewById(R.id.time_button)
        quantity = view.findViewById(R.id.quantity)
        add_food = view.findViewById(R.id.add_food)
        date_text = view.findViewById(R.id.date_text)
        time_text = view.findViewById(R.id.time_text)
        rotateLoading = view.findViewById<View>(R.id.rotateloading) as ProgressBar
        rotateLoading?.visibility=View.INVISIBLE
        submit = view.findViewById(R.id.submit_button)
        activity.title = "Food"
        date_cross = view.findViewById(R.id.date_cross)
        time_cross = view.findViewById(R.id.time_cross)
        search = view.findViewById(R.id.search)
        mainLayout = view.findViewById<View>(R.id.mainlayout) as LinearLayout
        food_context = activity


//        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
////        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        food_category.setAdapter(spinnerAdapter2);
//        spinnerAdapter2.add("select");
//        spinnerAdapter2.add("Breakfast");
//        spinnerAdapter2.add("Lunch");
//        spinnerAdapter2.add("Dinner");
//        spinnerAdapter2.add("Snack");
//        spinnerAdapter2.notifyDataSetChanged();
//        items.add("Breakfast");
//        items.add("Lunch");
//        items.add("Dinner");
//        items.add("Snack");
//
//
//
//
//
//
//        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//
//        alertDialog.setCancelable(true);
//
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        View convertView = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true);
//        alertDialog.setTitle("Blood Glucose Type");
//        lv = (ListView) convertView.findViewById(R.id.lv);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
//        lv.setAdapter(adapter);
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String selectedFromList = (lv.getItemAtPosition(i).toString());
////                        Log.i("Check2", selectedFromList + "  " + i);
//
//
//
//                alertDialog.cancel();
//
//            }
//
//        });
//
//        alertDialog.setView(convertView);
//        alertDialog.show();
//


//  food_edittext = view.findViewById(R.id.food_edittext);

//        Search act = (Search) getActivity();
        val checkfood: String = Search.foodName
        if (checkfood != "") search!!.setText(checkfood)
        date_cross!!.setOnClickListener(View.OnClickListener {
            date_text!!.setText("Date")
            final_date = ""
        })
        time_cross!!.setOnClickListener(View.OnClickListener {
            time_text!!.setText("Time")
            final_time = ""
        })
        add_food!!.setOnClickListener(View.OnClickListener {
            val cdd = CDialogue(activity)
            cdd.show()
        })
        date!!.setOnClickListener(View.OnClickListener {
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            current_date = day.toString() + "/" + (month + 1) + "/" + year
            present_date_compare = day.toString() + "/" + (month + 1) + "/" + year

            //                String food_contents = Search.getContent();
            //                Log.i("checking21 ", food_contents);
            val dialog = DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog,
                { datePicker, year, month, day ->
                    final_date = day.toString() + "/" + (month + 1) + "/" + year
                    date_text!!.setText(final_date)
                }, year, month, day
            )
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
                        time_text!!.setText("$i:$i1")
                        final_time = "$i:$i1"
                    }, hour, minute, true
                )
                timePickerDialog.show()
            }
        })
        search!!.setOnClickListener(View.OnClickListener {
            val i = Intent()
            i.setClass(activity, Search::class.java)
            startActivity(i)
            //                if (!food_name_list.isEmpty()) {
            //                    food_name_list.clear();
            //                }
            //                try {
            //                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            //                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            //                } catch (Exception e) {
            //                    // TODO: handle exception
            //                }
            //                if (!isNetworkAvailable()) {
            //                    Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
            //                } else {
            //
            //                    rotateLoading.start();
            //                    CheckExist();
            //                }
        })
        submit!!.setOnClickListener(View.OnClickListener {
            val b = AlertDialog.Builder(activity)
            b.setTitle("Alert")
            b.setMessage("Are you sure you want to submit ?")
            b.setPositiveButton(
                "Yes"
            ) { dialogInterface, index ->
                food_here = Search.foodName
                if (!isNetworkAvailable) {
                    Toast.makeText(activity, "No connection", Toast.LENGTH_SHORT).show()
                } else if ("" == food_here || "search food" == food_here!!.lowercase(Locale.getDefault())) {
                    Toast.makeText(activity, "Enter food name", Toast.LENGTH_SHORT).show()
                    //                        } else
                    //                        if ("select".equals(food_category.getSelectedItem().toString())) {
                    //                            Toast.makeText(getActivity(), "Enter food category", Toast.LENGTH_SHORT).show();
                    //                        } else if ("".equals(quantity.getText().toString())) {
                    Toast.makeText(activity, "Enter quantity", Toast.LENGTH_SHORT).show()
                } else if (search!!.getText().toString().lowercase(Locale.getDefault())
                        .contains("none")
                ) {
                    Toast.makeText(
                        activity,
                        "Add the new food item in database",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    try {
                        val preferences = PreferenceManager.getDefaultSharedPreferences(
                            activity
                        )
                        val key = preferences.getString(KEY, null)
                        val time = System.currentTimeMillis()
                        if (key == null) {
                            Toast.makeText(
                                activity,
                                "Something went wrong!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val dataBaseFoodHelper = DataBaseFoodHelper(
                                activity
                            )
                            val db = dataBaseFoodHelper.writableDatabase
                            val cv = ContentValues()
                            cv.put(FOOD_DB_NAME, food_here)
                            cv.put(FOOD_DB_CATEGORY, "already")
                            cv.put(FOOD_DB_DATE, final_date)
                            cv.put(FOOD_DB_TIME, final_time)
                            cv.put(FOOD_DB_QUANTITY, quantity!!.getText().toString())
                            db.insert(FOOD_HELPER_DB_NAME, null, cv)
                            //                                myRef.child(key).child(time + "").child("DATE").setValue(final_date);
                            ////                                Search activity = (Search) getActivity();
                            //
                            //
                            //                                myRef.child(key).child(time + "").child("FOOD_CATEGORY").setValue(food_category.getSelectedItem().toString());
                            //                                myRef.child(key).child(time + "").child("FOOD_NAME").setValue(food_here);
                            //                                myRef.child(key).child(time + "").child("QUANTITY").setValue(quantity.getText().toString());
                            //                                myRef.child(key).child(time + "").child("TIME").setValue(final_time);
                            //                                myRef.child(key).child(time + "").child("CURRENT_DATE").setValue(current_date);
                            //                                myRef.child(key).child(time + "").child("CURRENT_TIME").setValue(current__time);

                            //                                Toast.makeText(getActivity(), "Data submitted..!!", Toast.LENGTH_SHORT).show();
                            val getpreferences = PreferenceManager.getDefaultSharedPreferences(
                                activity
                            )
                            var count_here = getpreferences.getInt(KEEP_COUNT, 0)
                            val carbs_initial = getpreferences.getFloat(CARBS, 0f)
                            val fat_initial = getpreferences.getFloat(FAT, 0f)
                            val protein_initial = getpreferences.getFloat(PROTEIN, 0f)
                            var storeddate = getpreferences.getString(STORED_DATE, null)
                            var carb_count = 0.0f
                            var fat_count = 0.0f
                            var protein_count = 0.0f
                            var calories_count = 0f
                            //                            if (carbs_initial != 0 && fat_initial != 0 && protein_initial != 0) {
                            carb_count = carbs_initial
                            fat_count = fat_initial
                            protein_count = protein_initial

                            //                            }
                            val food_contents: String? = Search.content
                            if (food_contents != null) {
                                Log.i("checking21 ", food_contents)
                            }
                            var carbs = "0"
                            val matchercarbs = Pattern.compile("Carbs= (.*?)g,")
                                .matcher(food_contents)
                            while (matchercarbs.find()) {
                                //                                Log.i("check21", matchercarbs.group(1));
                                carbs = matchercarbs.group(1)
                            }
                            var fat = "0"
                            val matcherfat = Pattern.compile("Fat= (.*?)g")
                                .matcher(food_contents)
                            //                                Log.i("checking21fat", fat + " fatcheck " + matcherfat.toString() + " " + food_contents);
                            while (matcherfat.find()) {
                                fat = matcherfat.group(1)
                                //                                    Log.i("checking21fat", fat + " fatcheck inside" + matcherfat.toString());
                            }
                            var calories = "0"
                            val matchercalories =
                                Pattern.compile("Calories= (.*?),")
                                    .matcher(food_contents)
                            while (matchercalories.find()) {
                                //                                Log.i("check21", matcherfat.group(1));
                                calories = matchercalories.group(1)
                            }
                            //                                Log.i("checking21", calories + " " + matchercalories);
                            var protein = "0"
                            val matcherprotein =
                                Pattern.compile("Protein= (.*?)g")
                                    .matcher(food_contents)
                            while (matcherprotein.find()) {
                                //                                Log.i("check21", matcherprotein.group(1));
                                protein = matcherprotein.group(1)
                            }
                            val preferences_contents =
                                PreferenceManager.getDefaultSharedPreferences(
                                    activity
                                )
                            val editor = preferences_contents.edit()
                            //                                Log.i("checking21", storeddate + " " + present_date_compare);
                            if (storeddate == null || storeddate != present_date_compare) {
                                //                                    Log.i("checking21", storeddate + " " + present_date_compare);
                                carb_count = 0.0f
                                fat_count = 0.0f
                                protein_count = 0.0f
                                storeddate = present_date_compare
                                editor.putString(STORED_DATE, storeddate)
                                val count = getpreferences.getInt(KEEP_COUNT, 0)
                                for (i in 1..count) {
                                    editor.remove(FIXED_WORD + i)
                                    editor.remove(FIXED_AMOUNT + i)
                                    editor.remove(FOOD_CATEGORY + i)
                                    editor.remove(FIXED_CARBS + i)
                                    editor.remove(CALORIES + i)
                                }
                                count_here = 0
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
                                val count_bg = getpreferences.getInt(KEEP_COUNT_BG, 0)
                                for (i in 1..count_bg) {
                                    editor.remove(FIXED_BG + i)
                                }
                                editor.putInt(KEEP_COUNT_EXERCISE, 0)
                                editor.putInt(KEEP_COUNT, 0)
                                editor.putInt(KEEP_COUNT_BG, 0)
                                editor.putInt(KEEP_COUNT_INSULIN, 0)
                            }
                            // checking if myfitnesspal has null in any value,otherwise app crash
                            if (carbs.length == 0) carbs = "0"
                            if (fat.length == 0) fat = "0"
                            if (protein.length == 0) protein = "0"
                            if (calories.length == 0) calories = "0"

                            //                                Log.i("checking21fat", fat + " fatcheck");
                            carb_count += (java.lang.Float.valueOf(carbs) * quantity!!.getText()
                                .toString().toFloat() + 0.0).toFloat()
                            fat_count += (java.lang.Float.valueOf(fat) * quantity!!.getText()
                                .toString().toFloat() + 0.0).toFloat()
                            protein_count += (java.lang.Float.valueOf(protein) * quantity!!.getText()
                                .toString().toFloat() + 0.0).toFloat()
                            calories_count += (java.lang.Float.valueOf(calories) * quantity!!.getText()
                                .toString().toFloat() + 0.0).toFloat()

                            // increasing count here
                            count_here++
                            editor.putInt(KEEP_COUNT, count_here)
                            editor.putString(FIXED_WORD + count_here, food_here)
                            editor.putString(
                                FIXED_AMOUNT + count_here,
                                quantity!!.getText().toString()
                            )
                            //


                            //     editor.putString(FOOD_CATEGORY + count_here, food_category.getSelectedItem().toString());
                            editor.putFloat(
                                FIXED_CARBS + count_here,
                                java.lang.Float.valueOf(carbs) * quantity!!.getText().toString()
                                    .toFloat()
                            )
                            //                                Log.i("checking21", "quantity here " + quantity.getText().toString());
                            editor.putFloat(FAT, fat_count)
                            editor.putFloat(CARBS, carb_count)
                            editor.putFloat(PROTEIN, protein_count)
                            editor.putFloat(CALORIES + count_here, calories_count)
                            editor.apply()
                            editor.commit()
                            date_text!!.setText("Date")
                            time_text!!.setText("Time")
                            quantity!!.setText("")
                            search!!.setText("Search food")
                            Search.settFoodName("")
                            //                                food_category.setSelection(0);
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity, "Error in submission", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                dialogInterface.cancel()
            }
            b.setNegativeButton(
                "No"
            ) { dialogInterface, i -> dialogInterface.cancel() }
            b.show()
        })
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    //    private void CheckExist() {
    //
    //        myRef2.addValueEventListener(new ValueEventListener() {
    //            @Override
    //            public void onDataChange(DataSnapshot dataSnapshot) {
    //                for (DataSnapshot d : dataSnapshot.getChildren()) {
    //                    String temp_string = d.getKey().toString();
    //                    if (temp_string.toLowerCase().contains(food_edittext.getText().toString().toLowerCase())) {
    //                        String ans = "";
    //                        Matcher matcher = Pattern.compile("Serving(.*?),").matcher(d.getValue().toString());
    //                        while (matcher.find()) {
    //                            Log.i("check21", matcher.group(1));
    //                            ans = matcher.group(1);
    //                        }
    //                        food_formulations.add(d.getValue().toString());
    //                        food_name_list.add(temp_string + "[ Serving " + ans + " ]");
    //                    }
    //                }
    //                listhere();
    //            }
    //
    //            @Override
    //            public void onCancelled(DatabaseError databaseError) {
    //
    //            }
    //
    //        });
    //        if (!food_name_list.isEmpty())
    //            rotateLoading.stop();
    //        Log.i("Check21", "function end");
    //
    //    }
    //    public void listhere() {
    //        rotateLoading.stop();
    //
    //        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
    //        alertDialog.setCancelable(true);
    //        if (!isNetworkAvailable()) {
    //            Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
    //        } else {
    ////        CheckExist();
    //            Log.i("Check21", "function called");
    //
    //            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    //            View convertView = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true);
    //
    //            alertDialog.setTitle("Select food item");
    //            lv = (ListView) convertView.findViewById(R.id.lv);
    //            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, food_name_list);
    //            lv.setAdapter(adapter);
    //            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //
    //
    //                @Override
    //                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    //                    String selectedFromList = (lv.getItemAtPosition(i).toString());
    //                    Log.i("Check2", selectedFromList + "  " + i);
    //
    //                    food_here = selectedFromList;
    //                    food_edittext.setText(food_here);
    //                    food_index = i;
    //                    alertDialog.cancel();
    //
    //                }
    //
    //            });
    //
    //
    //            alertDialog.setView(convertView);
    //
    //            if (!food_name_list.isEmpty()) {
    //
    //                alertDialog.show();
    //
    //            } else {
    //                Toast.makeText(getActivity(), "Food not found!", Toast.LENGTH_SHORT).show();
    //
    //            }
    //        }
    //
    //    }
    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
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
        var food_context: Context? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Food.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Food {
            val fragment = Food()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}