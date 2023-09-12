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
import com.kvsc.type1.Adapters.InsulinAdapter
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.UserInsulin
import java.util.*


class ShowInsulin : AppCompatActivity() {
    var rotateLoading: ProgressBar? = null
    var database2: FirebaseDatabase = FirebaseDatabase.getInstance()
    var userInsulin: ArrayList<UserInsulin> = ArrayList<UserInsulin>()
    var insulinAdapter: InsulinAdapter? = null
    var recyclerView: RecyclerView? = null
    var path: String? = null
    var myRef2: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_insulin)
        val preferences2 = PreferenceManager.getDefaultSharedPreferences(this@ShowInsulin)
        userInsulin = ArrayList<UserInsulin>()
        supportActionBar!!.setTitle("Insulin Log")
        val key = preferences2.getString(KEY, null)
        path = "insulin/$key"
        myRef2 = database2.getReference(path!!)
        rotateLoading = findViewById<View>(R.id.rotateloading) as ProgressBar
        recyclerView = findViewById(R.id.recycle)
        recyclerView!!.setHasFixedSize(true)
        insulinAdapter = InsulinAdapter(this@ShowInsulin, userInsulin)
        recyclerView?.setAdapter(insulinAdapter)
        recyclerView?.setLayoutManager(LinearLayoutManager(this@ShowInsulin))
        if (!isNetworkAvailable) {
            Toast.makeText(this@ShowInsulin, "No connection", Toast.LENGTH_SHORT).show()
        } else {
            try {
                CheckExist()
                rotateLoading?.visibility=View.VISIBLE
            } catch (e: Exception) {
                Toast.makeText(this@ShowInsulin, "Error", Toast.LENGTH_SHORT)
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
                        var data=d.getValue() as HashMap<*,*>
                        val user = UserInsulin(
                            data["AMOUNT"].toString() ,
                            data["CATEGORY"].toString(),
                            data["DATE"].toString(),
                            data["TIME"].toString(),
                            data["TYPE"].toString()
                            )

//                        val firebaseUser: UserInsulin = d.getValue(UserInsulin::class.java)!!

//                        Log.i("see_data", firebaseUser.getDATE().toString() + "");
                        val date: String = user.dATE.toString() + ""
                        val time: String = user.tIME .toString() + ""
                        val amount: String = user.aMOUNT .toString() + ""
                        val category: String = user.cATEGORY .toString() + ""
                        val type: String = user.tYPE.toString() + ""
//                        val user = UserInsulin(amount, category, date, time, type)
                        userInsulin!!.add(user)
                        Log.i(
                            "see_data",
                            user.dATE .toString().toString() + "dcc " + userInsulin!!.size
                        )
                    }
                    rotateLoading?.visibility=View.INVISIBLE
                    if (userInsulin!!.size == 0) {
                        Toast.makeText(this@ShowInsulin, "No insulin entries", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Collections.reverse(userInsulin)
                        insulinAdapter!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } catch (e: Exception) {
        }
    }
}