package com.kvsc.type1.Activities
import com.kvsc.type1.R
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import com.victor.loading.rotate.RotateLoading
import java.util.regex.Pattern

import com.kvsc.type1.ClassesHere.Constants.FOOD_CONTENT
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_SIZE
import com.kvsc.type1.ClassesHere.Constants.FOOD_NAME
import com.kvsc.type1.ClassesHere.Constants.STORE_CATEGORY_TEMP

class Search : AppCompatActivity() {
    var myDB: DatabaseHelper? = null
    private val items = ArrayList<String>()
    var adapter: ArrayAdapter<String?>? = null
    var adaptertest: ArrayAdapter<String>? = null
    var rotateLoading: ProgressBar? = null
    var send_food_contents: String? = null
    var send_food: String? = null
    var database2: FirebaseDatabase = FirebaseDatabase.getInstance()
    var myRef2: DatabaseReference = database2.getReference("food_entry/food_db")
    val arrayList = ArrayList<String>()
    var food_formulations = ArrayList<String?>()
    var food_name_list = ArrayList<String?>()
    var mapping = HashMap<String?, String?>()
    var listView: ListView? = null
    var suggest: ListView? = null
    var theList: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        listView = findViewById(R.id.list_of_food)
        theList = ArrayList()
        myDB = DatabaseHelper(this@Search)
        suggest = findViewById(R.id.suggestlist)
        val data = myDB!!.listContent
        val getpreferences = PreferenceManager.getDefaultSharedPreferences(this@Search)
        if (data.count == 0) {
            suggest?.setVisibility(View.GONE)
        } else {
            suggest?.setVisibility(View.GONE)
            while (data.moveToNext()) {
                theList!!.add(data.getString(1))
            }
        }
        rotateLoading = findViewById<View>(R.id.rotateloading) as ProgressBar
        rotateLoading?.visibility=View.INVISIBLE
        //        SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(Search.this);
        val size = getpreferences.getInt(FOOD_DB_SIZE, 0)
        //        Set<String> set_food_name = getpreferences.getStringSet(FOOD_NAME, null);
//        Set<String> set_food_content = getpreferences.getStringSet(FOOD_CONTENT, null);
        if (size > 0) {
            for (i in 0 until size) {
                val getfood = getpreferences.getString(FOOD_NAME + i, null)
                food_name_list.add(getfood)
                val getcontent = getpreferences.getString(FOOD_CONTENT + i, null)
                food_formulations.add(getcontent)
                mapping[getfood] = getcontent
            }
            if (!theList!!.isEmpty()) {
                adaptertest = ArrayAdapter(
                    this@Search, android.R.layout.simple_list_item_1,
                    theList!!
                )
                suggest?.setAdapter(adaptertest)
                suggest?.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
                    val selectedFromList = suggest?.getItemAtPosition(i).toString()
                    //                    Log.i("Check2", selectedFromList + "  " + i);
                    send_food_contents = mapping[selectedFromList]
                    send_food = selectedFromList
                    //                    Toast.makeText(Search.this,send_food_contents,Toast.LENGTH_SHORT).show();
                    foodName = selectedFromList
                    content = send_food_contents
                    val intent = Intent(this@Search, FoodAdd::class.java)
                    intent.putExtra("message", "Food")
                    startActivity(intent)
                })
            }
            adapter = ArrayAdapter(this@Search, android.R.layout.simple_list_item_1, food_name_list)
            listView?.setAdapter(adapter)
            listView?.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
                val selectedFromList = listView?.getItemAtPosition(i).toString()
                //                    Log.i("Check2", selectedFromList + "  " + i);
                send_food_contents = mapping[selectedFromList]
                send_food = selectedFromList
                //                    Toast.makeText(Search.this,send_food_contents,Toast.LENGTH_SHORT).show();
                foodName = selectedFromList
                content = send_food_contents
                var wentin = false
                for (m in theList!!.indices) {
                    if (theList!![m] == foodName) {
                        wentin = true
                    }
                }
                if (!wentin) {
                    myDB!!.adddata(foodName)
                }
                val intent = Intent(this@Search, FoodAdd::class.java)
                intent.putExtra("message", "Food")
                startActivity(intent)
            })
        } else {
            Toast.makeText(this@Search, "Sync list", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.menuSearch)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
//                materialSearchView.openSearch();
                if (suggest!!.visibility == View.GONE && !theList!!.isEmpty()) {
                    suggest!!.visibility = View.VISIBLE
                }
                if (adaptertest != null && adaptertest!!.isEmpty) {
                    suggest!!.visibility = View.GONE
                }
                if (!food_name_list.isEmpty() && !theList!!.isEmpty()) {
                    adapter!!.filter.filter(s)
                    adaptertest!!.filter.filter(s)
                }
                if (food_name_list.isEmpty()) {
                    Toast.makeText(this@Search, "Sync list", Toast.LENGTH_SHORT).show()
                } else {
                    adapter!!.filter.filter(s)
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager =
                this@Search.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sync_food -> {
                food_name_list.clear()
                food_formulations.clear()
                if (!isNetworkAvailable) {
                    Toast.makeText(this@Search, "No connection", Toast.LENGTH_SHORT).show()
                } else {
                    rotateLoading?.visibility=View.VISIBLE
                    CheckExist()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun CheckExist() {
        myRef2.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.getChildren()) {
                    val temp_string: String = d.getKey().toString()
                    var ans = ""
                    val matcher = Pattern.compile("Serving(.*?),").matcher(d.getValue().toString())
                    while (matcher.find()) {
                        Log.i("check2177", d.getValue().toString())
                        ans = matcher.group(1)
                    }
                    val content: String = d.getValue().toString()
                    val food = "$temp_string[ Serving $ans ]"
                    food_formulations.add(content)
                    food_name_list.add(food)
                    mapping[food] = content
                }
                rotateLoading?.visibility=View.INVISIBLE
                updatelist()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

//            fun onCancelled(databaseError: DatabaseError?) {
//                print("hello"+databaseError.toString())
//            }
        })
    }

    //    private void saveToFile(){
    //        try{
    //            FileOutputStream fileOutputStream=openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
    //            ObjectOutputStream oout = new ObjectOutputStream(fileOutputStream);
    //
    //            // write something in the file
    //            oout.writeObject(food_name_list);
    ////            for(int i=0;i<food_name_list.size();i++)
    ////            fileOutputStream.write(food_name_list.get(i).getBytes());
    //            oout.flush();
    //
    //            fileOutputStream.close();
    //        }catch (Exception e){
    //            Toast.makeText(Search.this,"Error saving data",Toast.LENGTH_SHORT).show();
    //        }
    //    }
    //    private void readFile(){
    //        try{
    ////            FileInputStream fileInputStream=openFileInput(FILE_NAME);
    //            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
    //            byte[] read = (byte[]) ois.readObject();
    //            String s2 = new String(read);
    //            Log.i("testingstorage", s2);
    ////            int size=fileInputStream.available();
    ////            byte[] buffer=new byte[size];
    ////            fileInputStream.read(buffer);
    ////            fileInputStream.close();
    //        }catch (Exception e){
    //            Toast.makeText(Search.this,"Error reading data",Toast.LENGTH_SHORT).show();
    //        }
    //    }
    private fun updatelist() {
        val preferences_contents = PreferenceManager.getDefaultSharedPreferences(this@Search)
        val editor = preferences_contents.edit()
        //        SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(Search.this);
//        Set<String> set_food_name = getpreferences.getStringSet(FOOD_NAME, null);
//        Set<String> set_food_content = getpreferences.getStringSet(FOOD_CONTENT, null);
//        if(set_food_name!=null)
//        editor.remove(FOOD_NAME);
//        if(set_food_content!=null)
//        editor.remove(FOOD_CONTENT);
//        Set<String> food_list = new HashSet<String>();
//        food_list.addAll(food_name_list);
//        Set<String> food_content = new HashSet<String>();
//        food_content.addAll(food_formulations);
        run {
            var i = 0
            while (i < food_name_list.size && i < food_formulations.size) {
                try {
                    editor.remove(FOOD_NAME + i)
                    editor.remove(FOOD_CONTENT + i)
                } catch (E: Exception) {
//             Log.i("Nofood", "NO FOOD");
                }
                i++
            }
        }
        try {
            editor.remove(FOOD_DB_SIZE)
        } catch (e: Exception) {
        }
        var i = 0
        while (i < food_name_list.size && i < food_formulations.size) {
            editor.putString(FOOD_NAME + i, food_name_list[i])
            editor.putString(FOOD_CONTENT + i, food_formulations[i])
            i++
        }
        editor.putInt(FOOD_DB_SIZE, Math.min(food_formulations.size, food_name_list.size))
        editor.commit()
        adapter = ArrayAdapter(this@Search,     android.R.layout.simple_list_item_1, food_name_list)
        listView!!.adapter = adapter
        //        saveToFile();
//        readFile();
        listView!!.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                val selectedFromList = listView!!.getItemAtPosition(i).toString()
                //                Log.i("Check2", selectedFromList + "  " + i);
                send_food_contents = mapping[selectedFromList]
                send_food = selectedFromList
                //                Toast.makeText(Search.this,send_food_contents,Toast.LENGTH_SHORT).show();
                foodName = selectedFromList
                content = send_food_contents
                //                Log.i("log21", content_of_selectedFood+" goj");
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@Search)
                val cattemp = preferences.getString(STORE_CATEGORY_TEMP, null)
                val intent = Intent(this@Search, FoodAdd::class.java)
                intent.putExtra("message", "Food")
                intent.putExtra("categorysend", cattemp + "")
                startActivity(intent)
            }
        rotateLoading?.visibility=View.INVISIBLE
    }

    companion object {
        var foodName = ""

        //    String food_selected="";
        var content: String? = ""
        var foodCAT = ""

        fun settFoodName(updatefood: String) {
            foodName = updatefood
        }

        fun setFoodContent(updatecontent: String?) {
            content = updatecontent
        }
    }
}