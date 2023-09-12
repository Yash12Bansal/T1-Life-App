package com.kvsc.type1.Activities

import com.kvsc.type1.R
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import com.victor.loading.rotate.RotateLoading
import com.kvsc.type1.Adapters.BGAdapter
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.UserBG
import java.util.*


class ShowBG : AppCompatActivity() {
    var rotateLoading: ProgressBar? = null
    var database2: FirebaseDatabase = FirebaseDatabase.getInstance()
    var userBG: ArrayList<UserBG> =  ArrayList<UserBG>()
    var bgAdapter: BGAdapter? = null
    var recyclerView: RecyclerView? = null
    var path: String? = null
    var myRef2: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_bg)
        val preferences2 = PreferenceManager.getDefaultSharedPreferences(this@ShowBG)
        userBG = ArrayList<UserBG>()
        supportActionBar!!.setTitle("Blood Glucose Log")
        val key = preferences2.getString(KEY, null)
        path = "blood_glucose/$key"
        myRef2 = database2.getReference(path!!)
        rotateLoading = findViewById<View>(R.id.rotateloading) as ProgressBar
        recyclerView = findViewById(R.id.recycle)
        recyclerView?.setHasFixedSize(true)
        bgAdapter = BGAdapter(this@ShowBG, userBG)
        recyclerView?.setAdapter(bgAdapter)
        recyclerView?.setLayoutManager(LinearLayoutManager(this@ShowBG))
        if (!isNetworkAvailable) {
            Toast.makeText(this@ShowBG, "No connection", Toast.LENGTH_SHORT).show()
        } else {
            try {
                CheckExist()
                rotateLoading?.visibility=View.VISIBLE
            } catch (e: Exception) {
                Toast.makeText(this@ShowBG, "Error", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private fun CheckExist() {
        try {
            Log.i("see_data", "check")
            myRef2?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    Log.i("see_data", dataSnapshot.getKey().toString());

                    for (d in dataSnapshot.children) {
//                        val user: UserBG = d.getValue(UserBG::class.java)!!
                        var data=d.getValue() as HashMap<*,*>
                        val user = UserBG(
                            data["BLOOD_GLUCOSE_TYPE"].toString() ,
                            data["VALUE"].toString(),
                            data["DATE"].toString(),
                            data["TIME"].toString(),
                        )

                        if (user != null) {
                            Log.i("see_data", user.dATE.toString() + "")
                            val date: String = user.dATE.toString() + ""
                            val time: String = user.tIME.toString() + ""
                            val amount: String = user.vALUE.toString() + ""
                            val type: String = user.bLOOD_GLUCOSE_TYPE.toString() + ""
//                            val user = UserBG(type, amount, date, time)
                            userBG!!.add(user)
                        }
                    }
                    rotateLoading?.visibility=View.INVISIBLE
                    if ((userBG?.size?:0) == 0) {
                        Toast.makeText(this@ShowBG, "No BG entries", Toast.LENGTH_LONG).show()
                    } else {
                        Collections.reverse(userBG)
                        bgAdapter!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } catch (e: Exception) {
        }
    }
}