package com.kvsc.type1.Fragments
import com.kvsc.type1.R
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.kvsc.type1.Activities.DataBaseFoodHelper
import com.kvsc.type1.Activities.FoodAdd
import com.kvsc.type1.Adapters.Food_DB_Adapter
import com.kvsc.type1.ClassesHere.FoodUser
import java.util.*
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_CATEGORY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_DATE
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_QUANTITY
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_TIME_STAMP
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_ID
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.STORE_CATEGORY_TEMP


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FoodAddFrag.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FoodAddFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoodAddFrag : Fragment() {
    var lv: ListView? = null
    var food_name: String? = null
    var food_category: String? = null
    var quantity: String? = null
    var date: String? = null
    var time: String? = null
    var time_stamp: String? = null
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("food")
    var items = ArrayList<String>()
    var recyclerView: RecyclerView? = null
    var floatingActionButton: FloatingActionButton? = null
    var dataBaseFoodHelper: DataBaseFoodHelper? = null
    var food_db_adapter: Food_DB_Adapter? = null
    var arrayList: ArrayList<FoodUser>? = null
    var category_to_be_passed: String? = ""

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_food_add, container, false)
        arrayList = ArrayList()
        floatingActionButton = view.findViewById(R.id.floatbutton)
        recyclerView = view.findViewById(R.id.recycle)
        recyclerView?.setHasFixedSize(true)
        items.add("Breakfast")
        items.add("Lunch")
        items.add("Dinner")
        items.add("Snack")
        val alertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setCancelable(true)
        val layoutInflater = LayoutInflater.from(activity)
        val convertView: View = layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
        alertDialog.setTitle("Food Category")
        lv = convertView.findViewById<View>(R.id.lv) as ListView
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        lv!!.adapter = adapter
        food_db_adapter = Food_DB_Adapter(activity, arrayList!!)
        recyclerView?.setAdapter(food_db_adapter)
        recyclerView?.setLayoutManager(LinearLayoutManager(activity))
        dataBaseFoodHelper = DataBaseFoodHelper(activity)
        lv!!.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val selectedFromList = lv!!.getItemAtPosition(i).toString()
                //                        Log.i("Check2", selectedFromList + "  " + i);
                category_to_be_passed = selectedFromList
                alertDialog.cancel()
            }
        alertDialog.setView(convertView)
        val preferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        val cate = preferences.getString(STORE_CATEGORY_TEMP, null)
        if (cate == null || cate.length == 0) {
            alertDialog.show()
        } else {
            category_to_be_passed = cate
        }
        setUpViews()
        floatingActionButton?.setOnClickListener(View.OnClickListener {
            if (category_to_be_passed == null || category_to_be_passed!!.length == 0) {
                Toast.makeText(activity, "Enter category", Toast.LENGTH_SHORT).show()
            } else {
                val preferences_contents = PreferenceManager.getDefaultSharedPreferences(
                    activity
                )
                val editor = preferences_contents.edit()
                editor.putString(STORE_CATEGORY_TEMP, category_to_be_passed)
                editor.apply()
                editor.commit()
                val i = Intent()
                i.setClass(activity, FoodAdd::class.java)
                startActivity(i)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.submit_menu, menu)
        val button = menu.findItem(R.id.submit_entries)
        button.setOnMenuItemClickListener { //                Log.i("gotithere", "onMenuItemClick: ");
            val b = AlertDialog.Builder(activity)
            b.setTitle("Alert")
            b.setMessage("Are you sure you want to submit ?")
            b.setPositiveButton(
                "Yes"
            ) { dialogInterface, index ->
                if (time_stamp != null && time_stamp!!.length > 0) {
                    send_data()
                    Toast.makeText(activity, "Data Recorded", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Invalid entry", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.cancel()
            }
            b.setNegativeButton(
                "No"
            ) { dialogInterface, i -> dialogInterface.cancel() }
            b.show()
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
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

    private fun setUpViews() {
        Log.i("TAG4", "setUpViews: ")
        val dp = dataBaseFoodHelper!!.readableDatabase
        arrayList!!.clear()
        val c = dp.query(FOOD_HELPER_DB_NAME, null, null, null, null, null, null)
        while (c.moveToNext()) {
            Log.i("TAG5", "setUpViews: ")
            val id = c.getInt(c.getColumnIndex(FOOD_HELPER_DB_ID))
            food_name = c.getString(c.getColumnIndex(FOOD_DB_NAME))
            food_category = c.getString(c.getColumnIndex(FOOD_DB_CATEGORY))
            //            String food_category="lets see";
            quantity = c.getString(c.getColumnIndex(FOOD_DB_QUANTITY))
            date = c.getString(c.getColumnIndex(FOOD_DB_DATE))
            time = c.getString(c.getColumnIndex(FOOD_DB_TIME))
            time_stamp = c.getString(c.getColumnIndex(FOOD_DB_TIME_STAMP))
            //            Log.i("TAG3",title);
//            Log.i("TAG3",description);
//            Log.i("TAG3",date);
//            FoodUser foodUser = new FoodUser(food_category, food_name, quantity, date, time, id, time_stamp);
//            Reminder reminder2 = new Reminder(id,title, description, date, time,boottime);
//            reminder.add(reminder2);
//            arrayList.add(foodUser);
        }
        food_db_adapter!!.notifyDataSetChanged()
    }

    private fun send_data() {
        val preferences_contents = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        val editor = preferences_contents.edit()
        editor.remove(STORE_CATEGORY_TEMP)
        editor.apply()
        editor.commit()
        val dp = dataBaseFoodHelper!!.readableDatabase
        arrayList!!.clear()
        val c = dp.query(FOOD_HELPER_DB_NAME, null, null, null, null, null, null)
        while (c.moveToNext()) {
            val id = c.getInt(c.getColumnIndex(FOOD_HELPER_DB_ID))
            food_name = c.getString(c.getColumnIndex(FOOD_DB_NAME))
            food_category = c.getString(c.getColumnIndex(FOOD_DB_CATEGORY))
            quantity = c.getString(c.getColumnIndex(FOOD_DB_QUANTITY))
            date = c.getString(c.getColumnIndex(FOOD_DB_DATE))
            time = c.getString(c.getColumnIndex(FOOD_DB_TIME))
            time_stamp = c.getString(c.getColumnIndex(FOOD_DB_TIME_STAMP))
            val preferences = PreferenceManager.getDefaultSharedPreferences(
                activity
            )
            val key = preferences.getString(KEY, null)
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            val current_date = day.toString() + "/" + (month + 1) + "/" + year
            val current_time = Calendar.getInstance()
            val hour = current_time[Calendar.HOUR_OF_DAY]
            val minute = current_time[Calendar.MINUTE]
            val current__time = "$hour:$minute"
            try {
                Log.i("final_testing", "$key s $time_stamp s ")


//               myRef.child(key).child(time_stamp + "").child("DATE").setValue(date);
//
//               myRef.child(key).child(time_stamp + "").child("FOOD_CATEGORY").setValue(food_category);
//               myRef.child(key).child(time_stamp + "").child("FOOD_NAME").setValue(food_name);
//               myRef.child(key).child(time_stamp + "").child("QUANTITY").setValue(quantity);
//               myRef.child(key).child(time_stamp + "").child("TIME").setValue(time);
//               myRef.child(key).child(time_stamp + "").child("CURRENT_DATE").setValue(current_date);
//               myRef.child(key).child(time_stamp + "").child("CURRENT_TIME").setValue(current__time);
            } catch (e: Exception) {
                Log.i("Tagexc", e.toString() + "")
            }
        }

//        Helperfamily helperfamily = new Helperfamily(getActivity());
//        SQLiteDatabase db =helperfamily.getWritableDatabase();
        dp.delete(FOOD_HELPER_DB_NAME, null, null)
        food_db_adapter!!.notifyDataSetChanged()
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
         * @return A new instance of fragment FoodAddFrag.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): FoodAddFrag {
            val fragment = FoodAddFrag()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}