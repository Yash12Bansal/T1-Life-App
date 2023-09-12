package com.kvsc.type1.Activities
import com.kvsc.type1.R
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kvsc.type1.ClassesHere.Constants.GENDER
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.PERSON_NAME
import com.kvsc.type1.Navigation_drawer.MainScreen


class EnterDetails : AppCompatActivity() {
    var name: EditText? = null
    var age: EditText? = null
    var mobile: EditText? = null
    var spinner: Spinner? = null
    var submit: CardView? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var myRef: DatabaseReference = database.getReference("login")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)
        supportActionBar!!.hide()
        name = findViewById<View>(R.id.name) as EditText
        age = findViewById(R.id.age)
        mobile = findViewById(R.id.mobile_no)
        spinner = findViewById(R.id.gender_spinner)
        submit = findViewById(R.id.submit)
        val spinnerAdapter =
            ArrayAdapter<String>(this@EnterDetails,android. R.layout.simple_spinner_item, android.R.id.text1)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.setAdapter(spinnerAdapter)
        spinnerAdapter.add("Male")
        spinnerAdapter.add("Female")
        spinnerAdapter.add("Other")
        spinnerAdapter.notifyDataSetChanged()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@EnterDetails)
        name!!.setText(preferences.getString(PERSON_NAME, null))
        submit?.setOnClickListener(View.OnClickListener {
            val b = AlertDialog.Builder(this@EnterDetails)
            b.setTitle("Alert")
            b.setMessage("Are you sure you want to submit ?")
            b.setPositiveButton(
                "Yes"
            ) { dialogInterface, index ->
                if (name!!.text.toString().isEmpty()) {
                    Toast.makeText(this@EnterDetails, "Enter name", Toast.LENGTH_SHORT).show()
                } else if (spinner?.getSelectedItem().toString()
                        .isEmpty() || spinner?.getSelectedItem().toString() == "select"
                ) {
                    Toast.makeText(this@EnterDetails, "Enter gender", Toast.LENGTH_SHORT).show()
                } else if (mobile?.getText().toString().isEmpty()) {
                    Toast.makeText(this@EnterDetails, "Enter mobile number", Toast.LENGTH_SHORT)
                        .show()
                } else if (age?.getText().toString().isEmpty()) {
                    Toast.makeText(this@EnterDetails, "Enter age", Toast.LENGTH_SHORT).show()
                } else {
                    val preferences =
                        PreferenceManager.getDefaultSharedPreferences(this@EnterDetails)
                    val key = preferences.getString(KEY, null)
                    if (key == null) {
                        Toast.makeText(
                            this@EnterDetails,
                            "Something went wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val time = System.currentTimeMillis()
                        val editor = preferences.edit()
//                        Toast.makeText(
//                            this@EnterDetails,
//                            "done dona done went wrong! $key",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        editor.putString(PERSON_NAME, name!!.text.toString())
                        editor.putString(GENDER, spinner?.getSelectedItem().toString())
                        editor.apply()
                        //                      Log.i("Check", key +" "+time);
                        myRef.child(key).child("NAME").setValue(name!!.text.toString())
                        myRef.child(key).child("AGE").setValue(age?.getText()?.toString())
                        myRef.child(key).child("GENDER").setValue(spinner?.getSelectedItem())
                        myRef.child(key).child("MOBILENO").setValue(mobile?.getText()?.toString())
                        startActivity(Intent(this@EnterDetails, MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                        this@EnterDetails.finish()
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
    private fun signOut() {
        Login.mGoogleSignInClient?.signOut()
            ?.addOnCompleteListener(this, object : OnCompleteListener<Void?> {
                override fun onComplete(task: Task<Void?>) {
                    this@EnterDetails.finish()
                }
            })
    }

    override fun onBackPressed() {
        val b = AlertDialog.Builder(this@EnterDetails)
        b.setTitle("Alert")
        b.setMessage("Do you want to leave without signing up?")
        b.setPositiveButton(
            "Yes"
        ) { dialogInterface, index ->
            FirebaseAuth.getInstance().signOut()
            signOut()
            startActivity(Intent(this@EnterDetails , Login::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            dialogInterface.cancel()
//            this@EnterDetails.finish()

            dialogInterface.cancel()
        }
        b.setNegativeButton(
            "No"
        ) { dialogInterface, i -> dialogInterface.cancel() }
        b.show()

    }
}