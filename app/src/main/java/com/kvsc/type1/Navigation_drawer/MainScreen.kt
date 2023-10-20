package com.kvsc.type1.Navigation_drawer

import com.kvsc.type1.R
import android.net.Uri
import android.os.Bundle
import android.os.Handler
//import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.kvsc.type1.Activities.Login
import com.kvsc.type1.ClassesHere.Constants.DIRECT_INSULIN_SCREEN
import com.kvsc.type1.Fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser

class MainScreen : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    Predict.OnFragmentInteractionListener,
    Piechart.OnFragmentInteractionListener,
    BloodGlucose.OnFragmentInteractionListener,
    Contact.OnFragmentInteractionListener, Food.OnFragmentInteractionListener,
    Insulin.OnFragmentInteractionListener,
    Exercise.OnFragmentInteractionListener, FoodAddFrag.OnFragmentInteractionListener
{
    var toolbar: Toolbar? = null
    lateinit var bottomNav : BottomNavigationView
    override fun onResume() {
        super.onResume()
        val i = intent
        val check_insulin = i.getStringExtra(DIRECT_INSULIN_SCREEN)
        if (check_insulin != null && check_insulin == "insulin") {
            toolbar!!.setTitle("Insulin")
            val insulinFragment = Insulin()
            bottomNav.selectedItemId=R.id.insulin
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
        } else if (check_insulin != null && check_insulin == "predict") {
            toolbar!!.setTitle("Prediction")
            val predict = Predict()
            bottomNav.selectedItemId=R.id.prediction

            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag).commit()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar!!.setTitle("App name")
        title = "Exercise"
//        val arrowImageView = findViewById<ImageView>(R.id.arrowImageView)
//
//        // Animate the arrow pointer
//        val animator = ObjectAnimator.ofFloat(arrowImageView, "translationY", 0f, 50f).apply {
//            duration = 1000 // Animation duration in milliseconds
//            repeatCount = ObjectAnimator.INFINITE // Repeat animation indefinitely
//            repeatMode = ObjectAnimator.REVERSE // Reverse the animation direction
//            interpolator = AccelerateDecelerateInterpolator() // Apply acceleration and deceleration to the animation
//        }
//
//        // Start the animation
//        animator.start()
//

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        var mAuth=FirebaseAuth.getInstance()
        var header=navigationView.getHeaderView(0)
        var userName=header.findViewById<TextView>(R.id.user_name)
        var userEmail=header.findViewById<TextView>(R.id.user_email)
        val user: FirebaseUser = mAuth!!.getCurrentUser()!!
//        1bLb0BS9LdMqJF69roCMT4WeNYK2
        userName.setText(user.displayName)
        userEmail.setText(user.email)
        println("UDIFDOFDFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"+mAuth.uid)

        navigationView.setNavigationItemSelectedListener(this)
        val i = intent
        val check_insulin = i.getStringExtra(DIRECT_INSULIN_SCREEN)
        if (check_insulin != null && check_insulin == "insulin") {
            toolbar!!.setTitle("Insulin")
            val insulinFragment = Insulin()
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
        } else if (check_insulin != null && check_insulin == "predict") {
            toolbar!!.setTitle("Prediction")
            val predict = Predict()
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag).commit()
        } else {
            drawer.openDrawer(Gravity.LEFT)
        }

        bottomNav=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            val id = it.itemId
            if (id == R.id.insulin) {
                toolbar?.setTitle("Insulin")
                val insulinFragment = Insulin()
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
                true
            }
            if (id == R.id.exercise) {
                toolbar?.setTitle("Exercise")
                val exerciseFragment = Exercise()
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.app_bar, exerciseFragment, exerciseFragment.tag).commit()
                true
            }
            if (id == R.id.bg_value) {
                toolbar?.setTitle("Blood Glucose")
                val bloodGlucose = BloodGlucose()
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.app_bar, bloodGlucose, bloodGlucose.tag)
                    .commit()
                true

            }
            if (id == R.id.daily_log) {
                toolbar?.setTitle("Daily log")
            val pieFragment = Piechart()
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction().replace(R.id.app_bar, pieFragment, pieFragment.tag)
                .commit()
            true
            }
            if (id == R.id.prediction) {
                toolbar?.setTitle("Prediction")
                val predict = Predict()
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag)
                    .commit()

                true
            }
            else{
                true
            }
        }
        bottomNav.selectedItemId=R.id.exercise
        drawer.closeDrawer(GravityCompat.START)

    }

    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
//        if (id == R.id.insulin) {
//            toolbar?.setTitle("Insulin")
//            val insulinFragment = Insulin()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction()
//                .replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
//        } else if (id == R.id.exercise) {
//            toolbar?.setTitle("Exercise")
//            val exerciseFragment = Exercise()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction()
//                .replace(R.id.app_bar, exerciseFragment, exerciseFragment.tag).commit()
//        } else if (id == R.id.bg_value) {
//            toolbar?.setTitle("Blood Glucose")
//            val bloodGlucose = BloodGlucose()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction().replace(R.id.app_bar, bloodGlucose, bloodGlucose.tag)
//                .commit()
//        }
         if (id == R.id.contact_us) {
            toolbar?.setTitle("Contact Us")
            val contactFragment = Contact()
            bottomNav.menu.setGroupCheckable(0, true, false)
             for (i in 0 until bottomNav.menu.size()) {
                 bottomNav.menu.getItem(i).isChecked = false
             }
             bottomNav.menu.setGroupCheckable(0, true, true)
             val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.app_bar, contactFragment, contactFragment.tag).commit()
        }

//         else if (id == R.id.pie_chart) {
//            toolbar?.setTitle("Daily log")
////            val pieFragment = Piechart()
////            val fragmentManager = fragmentManager
////            fragmentManager.beginTransaction().replace(R.id.app_bar, pieFragment, pieFragment.tag)
////                .commit()
//        }
//        else if (id == R.id.prediction) {
//            toolbar?.setTitle("Prediction")
//            val predict = Predict()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag).commit()
//        }
//        else if (id == R.id.log_out) {
//            val b = AlertDialog.Builder(this@MainScreen)
//            b.setTitle("Alert")
//            b.setMessage("Are you sure you want to logout ?")
//            b.setPositiveButton(
//                "Yes"
//            ) { dialogInterface, index ->
//                FirebaseAuth.getInstance().signOut()
//                signOut()
//                startActivity(Intent(this@MainScreen, Login::class.java))
//                dialogInterface.cancel()
//            }
//            b.setNegativeButton(
//                "No"
//            ) { dialogInterface, i -> dialogInterface.cancel() }
//            b.show()
//        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun signOut() {
        Login.mGoogleSignInClient?.signOut()
            ?.addOnCompleteListener(this, object : OnCompleteListener<Void?> {
                override fun onComplete(task: Task<Void?>) {
                    this@MainScreen.finish()
                }
            })
    }

    override fun onFragmentInteraction(uri: Uri?) {}
}