package com.kvsc.type1.Fragments

import android.annotation.SuppressLint
import com.kvsc.type1.R
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
//import com.developer.jatin.diabetes.ClassesHere.Constants.CARBS
//import com.developer.jatin.diabetes.ClassesHere.Constants.FAT
//import com.developer.jatin.diabetes.ClassesHere.Constants.FIXED_EXERCISE_TIME
//import com.developer.jatin.diabetes.ClassesHere.Constants.FIXED_WORD_EXERCISE
//import com.developer.jatin.diabetes.ClassesHere.Constants.FOOD_DB_QUANTITY
//import com.developer.jatin.diabetes.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
//import com.developer.jatin.diabetes.ClassesHere.Constants.KEEP_COUNT_EXERCISE
//import com.developer.jatin.diabetes.ClassesHere.Constants.PROTEIN
//import com.github.mikephil.charting.charts.PieChart
//import com.github.mikephil.charting.data.PieData
//import com.github.mikephil.charting.data.PieDataSet
//import com.github.mikephil.charting.formatter.PercentFormatter
//import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.FirebaseDatabase
import com.kvsc.type1.Activities.DataBaseFoodHelperLog
import com.kvsc.type1.Activities.FoodLog
import com.kvsc.type1.Activities.ShowBG
import com.kvsc.type1.Activities.ShowInsulin

import com.kvsc.type1.ClassesHere.Constants.CARBS
import com.kvsc.type1.ClassesHere.Constants.FAT
import com.kvsc.type1.ClassesHere.Constants.FIXED_EXERCISE_TIME
import com.kvsc.type1.ClassesHere.Constants.FIXED_WORD_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME_LOG
import com.kvsc.type1.ClassesHere.Constants.KEEP_COUNT_EXERCISE
import com.kvsc.type1.ClassesHere.Constants.PROTEIN

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Piechart.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Piechart.newInstance] factory method to
 * create an instance of this fragment.
 */
class Piechart() : Fragment() {
    var pieChart: PieChart? = null

    // TODO: Rename and change types of parameters
    var carbs = 0f
    var fat = 0f
    var proteins = 0f
    var quantity = 0f
    var dataBaseFoodHelper: DataBaseFoodHelperLog? = null
    var database = FirebaseDatabase.getInstance()
    var main_food_db_ref = database.getReference("food")
    var pred_food_db_ref = database.getReference("prediction")
    var exercise_list = ArrayList<String>()
    var bg_list = ArrayList<String>()
    var insulin_list = ArrayList<String>()
    var lv_exercise: ListView? = null
    var lv_bg: ListView? = null
    var lv_insulin: ListView? = null
    private var mParam1: String? = null
    private var mParam2: String? = null
    var full_carbs: TextView? = null
    var food_log: ImageButton? = null
    var insulin_log: ImageButton? = null
    var bg_log: ImageButton? = null
    var exercise_log: ImageButton? = null

    //    ArrayList<String> food_items = new ArrayList<>();
    //    ListView listView;
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
        val view: View = inflater.inflate(R.layout.fragment_piechart, container, false)
        pieChart = view.findViewById<View>(R.id.piechart) as PieChart
        food_log = view.findViewById(R.id.food_log)
        exercise_log = view.findViewById(R.id.exercise_log)
        bg_log = view.findViewById(R.id.bg_log)
        carbs = 0f
        insulin_log = view.findViewById(R.id.insulin_log)
        //        listView=view.findViewById(R.id.list_view);
        full_carbs = view.findViewById(R.id.total_carbs)
        pieChart!!.setUsePercentValues(true)
        dataBaseFoodHelper = DataBaseFoodHelperLog(activity)
        setUpViews()
        food_log!!.setOnClickListener(View.OnClickListener {
            if (carbs == 0f) {
                Toast.makeText(activity, "No Food Item Added", Toast.LENGTH_SHORT).show()
            } else {
                val i = Intent()
                i.setClass(activity, FoodLog::class.java)
                startActivity(i)
            }
        })
        exercise_log!!.setOnClickListener(View.OnClickListener {
            val alertDialog = AlertDialog.Builder(
                activity
            ).create()
            alertDialog.setCancelable(true)
            if (exercise_list.isEmpty()) {
                Toast.makeText(activity, "No Exercise added", Toast.LENGTH_SHORT).show()
            } else {
                val layoutInflater = LayoutInflater.from(activity)
                val convertView: View =
                    layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
                alertDialog.setTitle("Exercise Entries")
                lv_exercise = convertView.findViewById<View>(R.id.lv) as ListView
                val adapter = ArrayAdapter(
                    activity, android.R.layout.simple_list_item_1, exercise_list
                )
                lv_exercise!!.adapter = adapter
                lv_exercise!!.onItemClickListener = object : AdapterView.OnItemClickListener {
                    override fun onItemClick(
                        adapterView: AdapterView<*>?,
                        view: View,
                        i: Int,
                        l: Long
                    ) {
                        alertDialog.cancel()
                    }
                }
                alertDialog.setView(convertView)
                alertDialog.show()
            }
        })
        bg_log!!.setOnClickListener(View.OnClickListener {
            val i = Intent()
            i.setClass(activity, ShowBG::class.java)
            startActivity(i)

            //                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            //                alertDialog.setCancelable(true);
            //                if (bg_list.isEmpty()) {
            //                    Toast.makeText(getActivity(), "No BG value added", Toast.LENGTH_SHORT).show();
            //                } else {
            //
            //                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //                    View convertView = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true);
            //
            //                    alertDialog.setTitle("BG Entries");
            //                    lv_bg = (ListView) convertView.findViewById(R.id.lv);
            //                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, bg_list);
            //                    lv_bg.setAdapter(adapter);
            //                    lv_bg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //
            //
            //                        @Override
            //                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //                            alertDialog.cancel();
            //
            //                        }
            //
            //                    });
            //
            //
            //                    alertDialog.setView(convertView);
            //
            //                    alertDialog.show();
            //
            //                }
        })
        insulin_log!!.setOnClickListener(View.OnClickListener {
            val i = Intent()
            i.setClass(activity, ShowInsulin::class.java)
            startActivity(i)

            //                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //                    View convertView = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true);
            //
            //                    alertDialog.setTitle("Insulin Entries");
            //                    lv_insulin = (ListView) convertView.findViewById(R.id.lv);
            //                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, insulin_list);
            //                    lv_insulin.setAdapter(adapter);
            //                    lv_insulin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //
            //
            //                        @Override
            //                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //                            alertDialog.cancel();
            //
            //                        }
            //
            //                    });
            //
            //
            //                    alertDialog.setView(convertView);
            //
            //                    alertDialog.show();
        })
        val getpreferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        //        Float carbs_initial = getpreferences.getFloat(CARBS, 0f);
//        Float fat_initial = getpreferences.getFloat(FAT, 0f);
//        Float protein_initial = getpreferences.getFloat(PROTEIN, 0f);
////        Log.i("checking21", carbs_initial+" "+fat_initial+" "+protein_initial);
//        int count = getpreferences.getInt(KEEP_COUNT, 0);
//        String arr_of_category[]={"Breakfast","Lunch","Dinner","Snack"};
//        for(int j=0;j<arr_of_category.length;j++){
//
//            String type = arr_of_category[j].toUpperCase();
//
//
//            food_items.add(type.toString());
//            int numbering=1;
//            for(int i=1;i<=count;i++){
//                String category=getpreferences.getString(FOOD_CATEGORY+i,null);
//                if(arr_of_category[j].equals(category)){
//                    String food_item = getpreferences.getString(FIXED_WORD+i, null);
//                    String food_amount = getpreferences.getString(FIXED_AMOUNT+i, null);
//                    Float calories=getpreferences.getFloat(CALORIES+i,0f);
//                    Float carbs_total=getpreferences.getFloat(FIXED_CARBS+i,0f);
//                    food_items.add(numbering+") "+"food - "+food_item+"\nQuantity - "+food_amount+" , Carbs - "+carbs_total+" , Calories - "+calories);
//                    numbering++;
//                }
//            }
//            if(numbering==1){
//                food_items.remove(type.toString());
//            }
//        }
//        float total_calories=0f;
//      for(int i=1;i<=count;i++){
//          Float calories=getpreferences.getFloat(CALORIES+i,0f);
//          total_calories+=calories;
//      }
//        full_carbs.setText("Total Carbs : "+carbs_initial.toString()+", Total Calories : "+total_calories);
        val count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
        for (i in 1..count_exercise) {
            val exercise_item = getpreferences.getString(FIXED_WORD_EXERCISE + i, null)
            val duration = getpreferences.getFloat(FIXED_EXERCISE_TIME + i, 0f)
            exercise_list.add("$i) EXERCISE - $exercise_item\n    DURATION - $duration min")
        }
        //        if(insulin_count>0)
//            food_items.add("INSULIN");
//        for (int i = 1; i <= insulin_count; i++) {
//            String insulin_item = getpreferences.getString(FIXED_INSULIN + i, null);
//            insulin_list.add(i + ") " + insulin_item + " units");
//        }
//
//        int bg_count = getpreferences.getInt(KEEP_COUNT_BG, 0);
//        if(bg_count>0)
//            food_items.add("BLOOD GLUCOSE");
//        for (int i = 1; i <= bg_count; i++) {
//            String bg_item = getpreferences.getString(FIXED_BG + i, null);
//            bg_list.add(i + ") " + bg_item + " mg/dl");
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, food_items);
//        listView.setAdapter(adapter);
        full_carbs?.setText((Math.round(carbs * 100.0) / 100.0).toString() + "" + " g")
        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()
        labels.add("carbs")
        labels.add("fats")
        labels.add("proteins")
        entries.add(Entry (carbs, 0))
        entries.add(Entry(fat, 1))
        entries.add(Entry(proteins, 2))
        val dataSet = PieDataSet(entries, "")
        val data = PieData(labels, dataSet)
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS)
        data.setValueTextSize(13f)
        data.setValueFormatter(PercentFormatter())
        pieChart!!.setDescription("")
        pieChart!!.animateXY(1000, 1000)
        pieChart!!.animate()
        pieChart!!.setData(data)
        pieChart!!.setHoleRadius(0f)
        pieChart!!.invalidate()
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
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    @SuppressLint("Range")
    private fun setUpViews() {

//        Log.i("TAG4", "setUpViews: ");
        val dp = dataBaseFoodHelper!!.readableDatabase
        val c = dp.query(FOOD_HELPER_DB_NAME_LOG, null, null, null, null, null, null)
        while (c.moveToNext()) {
            quantity = java.lang.Float.valueOf(c.getString(c.getColumnIndex(FOOD_DB_QUANTITY)))
            fat += (java.lang.Float.valueOf(c.getString(c.getColumnIndex(FAT))) * java.lang.Float.valueOf(
                quantity
            ))
            carbs += (java.lang.Float.valueOf(c.getString(c.getColumnIndex(CARBS))) * java.lang.Float.valueOf(
                quantity
            ))
            proteins += (java.lang.Float.valueOf(c.getString(c.getColumnIndex(PROTEIN))) * java.lang.Float.valueOf(
                quantity
            ))
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
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Piechart.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Piechart {
            val fragment = Piechart()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}