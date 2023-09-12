package com.kvsc.type1.Activities
import com.kvsc.type1.R
import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.*
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import com.victor.loading.rotate.RotateLoading
//import com.victor.loading.rotate.RotateLoading

import java.util.*
import java.util.regex.Pattern

import com.kvsc.type1.ClassesHere.Constants.CALORIES
import com.kvsc.type1.ClassesHere.Constants.CARBS
import com.kvsc.type1.ClassesHere.Constants.DIRECT_INSULIN_SCREEN
import com.kvsc.type1.ClassesHere.Constants.FAT
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME_STAMP
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.PROTEIN
import com.kvsc.type1.CustomDialog.CDialogue
import com.kvsc.type1.Fragments.Food
import com.kvsc.type1.Navigation_drawer.MainScreen
import java.lang.Float

class FoodAdd : AppCompatActivity() {
    var rotateLoading:ProgressBar?=null
    var food_category: TextView? = null
    var quantity: EditText? = null
    var search: CardView? = null
    var items = ArrayList<String>()
    var date: CardView? = null
    var searchFood:TextView?=null
    var time: CardView? = null
    var food_index = 0
    var datePickerDialog: DatePickerDialog? = null
    var current_time = Calendar.getInstance()
    var present_date_compare: String? = null
    var timepicker: TimePickerDialog? = null
    var date_text: TextView? = null
    var time_text: TextView? = null
    var fixed_category = ""
    var mainLayout: LinearLayout? = null
    var add_food: CardView? = null
    var lv: ListView? = null

    //    Matcher pattern;
    var food_here: String? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var myRef: DatabaseReference = database.getReference("food")
    var submit: Button? = null
    var date_cross: ImageButton? = null
    var time_cross: ImageButton? = null
    var final_date = ""
    var final_time = ""
    var current_date: String? = null
    var current__time: String? = null
    var database2: FirebaseDatabase = FirebaseDatabase.getInstance()
    var food_name_list = ArrayList<String>()
    var food_formulations = ArrayList<String>()
    var myRef2: DatabaseReference = database2.getReference("food_database")
    private val mListener: Food.OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_food)
        supportActionBar!!.title = "Food Items"
        //        hideSoftKeyboard();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        quantity = findViewById(R.id.quantity)
        add_food = findViewById(R.id.add_food)
        rotateLoading = findViewById<View>(R.id.rotateloading) as ProgressBar
        submit = findViewById(R.id.submit_button)
        supportActionBar!!.setTitle("Food")
        search = findViewById(R.id.search)
        mainLayout = findViewById<View>(R.id.mainlayout) as LinearLayout
        food_context = this@FoodAdd
        searchFood=findViewById(R.id.search_food)
        rotateLoading?.visibility=View.INVISIBLE


//        Bundle bundle = getIntent().getExtras();
////        String getfromsearch=Bundle bundle = getIntent().getExtras();
//        if(bundle!=null){
//
//            final String getfromsearch = bundle.getString("categorysend");
//            if(getfromsearch==null || getfromsearch.length()==0){
//                food_category.setText(getfromsearch);
//            }
//        }
        val checkfood: String = Search.foodName
        if (checkfood != "") searchFood?.setText(checkfood)

        add_food?.setOnClickListener(View.OnClickListener {
            val cdd = CDialogue(this@FoodAdd)
            cdd.show()
        })
        search?.setOnClickListener(View.OnClickListener {
            val i = Intent()
            i.setClass(this@FoodAdd, Search::class.java)
            startActivity(i)
        })
        submit?.setOnClickListener(View.OnClickListener {
            val b = AlertDialog.Builder(this@FoodAdd)
            b.setTitle("Alert")
            b.setMessage("Are you sure you want to submit ?")
            b.setPositiveButton(
                "Yes"
            ) { dialogInterface, index ->
                food_here = Search.foodName
                if (!isNetworkAvailable) {
                    Toast.makeText(this@FoodAdd, "No connection", Toast.LENGTH_SHORT).show()
                } else if ("" == food_here || "search food" == food_here!!.lowercase(Locale.getDefault())) {
                    Toast.makeText(this@FoodAdd, "Enter food name", Toast.LENGTH_SHORT).show()
                } else if ("" == quantity!!.getText().toString()) {
                    Toast.makeText(this@FoodAdd, "Enter quantity", Toast.LENGTH_SHORT).show()
                } else if (searchFood!!.getText().toString().lowercase(Locale.getDefault())
                        .contains("none")
                ) {
                    Toast.makeText(
                        this@FoodAdd,
                        "Add the new food item in database",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    try {
                        val preferences =
                            PreferenceManager.getDefaultSharedPreferences(this@FoodAdd)
                        val key = preferences.getString(KEY, null)
                        val time = System.currentTimeMillis()
                        if (key == null) {
                            Toast.makeText(
                                this@FoodAdd,
                                "Something went wrong!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            //                                    SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(FoodAdd.this);
                            //                                    int count_here = getpreferences.getInt(KEEP_COUNT, 0);
                            //                                    float carbs_initial = getpreferences.getFloat(CARBS, 0f);
                            //                                    float fat_initial = getpreferences.getFloat(FAT, 0f);
                            //                                    float protein_initial = getpreferences.getFloat(PROTEIN, 0f);
                            //                                    String storeddate = getpreferences.getString(STORED_DATE, null);
                            var carb_count = 0.0f
                            var fat_count = 0.0f
                            var protein_count = 0.0f
                            var calories_count = 0f
                            //                            if (carbs_initial != 0 && fat_initial != 0 && protein_initial != 0) {
                            //                                    carb_count = carbs_initial;
                            //                                    fat_count = fat_initial;
                            //                                    protein_count = protein_initial;

                            //                            }
                            val food_contents: String = Search.content!!
                            Log.i("checking21 ", food_contents)
                            var carbs = "0"
                            val matchercarbs = Pattern.compile("Carbs=(.*?)g,")
                                .matcher(food_contents)
                            while (matchercarbs.find()) {
                                //                                Log.i("check21", matchercarbs.group(1));
                                carbs = matchercarbs.group(1)
                            }
                            var fat = "0"
                            val matcherfat = Pattern.compile("Fat=(.*?)g")
                                .matcher(food_contents)
                            //                                Log.i("checking21fat", fat + " fatcheck " + matcherfat.toString() + " " + food_contents);
                            while (matcherfat.find()) {
                                fat = matcherfat.group(1)
                                //                                    Log.i("checking21fat", fat + " fatcheck inside" + matcherfat.toString());
                            }
                            var calories = "0"
                            val matchercalories =
                                Pattern.compile("Calories=(.*?),")
                                    .matcher(food_contents)
                            while (matchercalories.find()) {
                                //                                Log.i("check21", matcherfat.group(1));
                                calories = matchercalories.group(1)
                            }
                            //                                Log.i("checking21", calories + " " + matchercalories);
                            var protein = "0"
                            val matcherprotein =
                                Pattern.compile("Protein=(.*?)g")
                                    .matcher(food_contents)
                            while (matcherprotein.find()) {
                                //                                Log.i("check21", matcherprotein.group(1));
                                protein = matcherprotein.group(1)
                            }

                            //                                    SharedPreferences preferences_contents = PreferenceManager.getDefaultSharedPreferences(FoodAdd.this);
                            //                                    SharedPreferences.Editor editor = preferences_contents.edit();
                            //                                Log.i("checking21", storeddate + " " + present_date_compare);
                            //                                    if (storeddate == null || !storeddate.equals(present_date_compare)) {
                            ////                                    Log.i("checking21", storeddate + " " + present_date_compare);
                            //                                        carb_count = 0.0f;
                            //                                        fat_count = 0.0f;
                            //                                        protein_count = 0.0f;
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
                            //                                        count_here = 0;
                            //                                        int count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0);
                            //                                        for (int i = 1; i <= count_exercise; i++) {
                            //                                            editor.remove(FIXED_WORD_EXERCISE + i);
                            //                                            editor.remove(FIXED_EXERCISE_TIME + i);
                            //                                        }
                            //
                            //                                        int count_insulin = getpreferences.getInt(KEEP_COUNT_INSULIN, 0);
                            //                                        for (int i = 1; i <= count_insulin; i++) {
                            //                                            editor.remove(FIXED_INSULIN + i);
                            //                                        }
                            //                                        int count_bg = getpreferences.getInt(KEEP_COUNT_BG, 0);
                            //                                        for (int i = 1; i <= count_bg; i++) {
                            //                                            editor.remove(FIXED_BG + i);
                            //                                        }
                            //                                        editor.putInt(KEEP_COUNT_EXERCISE, 0);
                            //                                        editor.putInt(KEEP_COUNT, 0);
                            //                                        editor.putInt(KEEP_COUNT_BG, 0);
                            //                                        editor.putInt(KEEP_COUNT_INSULIN, 0);
                            //
                            //                                    }

                            // checking if myfitnesspal has null in any value,otherwise app crash
                            if (carbs.length == 0) carbs = "0"
                            if (fat.length == 0) fat = "0"
                            if (protein.length == 0) protein = "0"
                            if (calories.length == 0) calories = "0"

                            //                                Log.i("checking21fat", fat + " fatcheck");
                            carb_count += (Float.valueOf(carbs) + 0.0).toFloat()
                            fat_count += (Float.valueOf(fat) + 0.0).toFloat()
                            protein_count += (Float.valueOf(protein) + 0.0).toFloat()
                            calories_count += (Float.valueOf(calories) + 0.0).toFloat()

                            // increasing count here
                            //                                    count_here++;
                            //                                    editor.putInt(KEEP_COUNT, count_here);
                            //                                    editor.putString(FIXED_WORD + count_here, food_here);
                            //                                    editor.putString(FIXED_AMOUNT + count_here, quantity.getText().toString());
                            //
                            //                                    editor.putString(FOOD_CATEGORY+count_here,food_category.getText().toString());

                            //     editor.putString(FOOD_CATEGORY + count_here, food_category.getSelectedItem().toString());


                            //                                    editor.putFloat(FIXED_CARBS + count_here, Float.valueOf(carbs) * Float.parseFloat(quantity.getText().toString()));
                            ////                                Log.i("checking21", "quantity here " + quantity.getText().toString());
                            //                                    editor.putFloat(FAT, fat_count);
                            //                                    editor.putFloat(CARBS, carb_count);
                            //                                    editor.putFloat(PROTEIN, protein_count);
                            //                                    editor.putFloat(CALORIES + count_here, calories_count);
                            //                                    editor.apply();
                            //                                    editor.commit();
                            val dataBaseFoodHelper = DataBaseFoodHelper(this@FoodAdd)
                            val db = dataBaseFoodHelper.writableDatabase
                            val cv = ContentValues()
                            val time_stamp = System.currentTimeMillis()
                            cv.put(FOOD_DB_NAME, food_here)
                            cv.put(FOOD_DB_TIME_STAMP, time_stamp.toString() + "")
                            cv.put(FOOD_DB_QUANTITY, quantity!!.getText().toString())
                            cv.put(FAT, fat_count)
                            cv.put(CARBS, carb_count)
                            cv.put(PROTEIN, protein_count)
                            cv.put(CALORIES, calories_count)
                            db.insert(FOOD_HELPER_DB_NAME, null, cv)


                            //                                    date_text.setText("Date");
                            //                                    time_text.setText("Time");
                            quantity!!.setText("")
                            searchFood!!.setText("Search food")
                            Search.settFoodName("")
                            //                                food_category.setSelection(0);
                            var options: ActivityOptions? = null
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                options =
                                    ActivityOptions.makeSceneTransitionAnimation(this@FoodAdd)
                            }
                            val ii = Intent()
                            ii.setClass(this@FoodAdd, MainScreen::class.java)
                            ii.putExtra(DIRECT_INSULIN_SCREEN, "predict")
                            startActivity(ii, options!!.toBundle())
                            //                                Intent i = new Intent();
                            //                                i.putExtra("message", "Food");
                            //                                i.setClass(FoodAdd.this, MainScreen.class);
                            //                                startActivity(i);
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@FoodAdd, "Error in submission", Toast.LENGTH_SHORT)
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
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    companion object {
        var food_context: Context? = null
    }
}