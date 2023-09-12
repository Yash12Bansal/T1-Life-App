package com.kvsc.type1.Activities

import android.animation.ValueAnimator
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.kvsc.type1.ClassesHere.Constants.GENDER
import com.kvsc.type1.ClassesHere.Constants.KEY
import com.kvsc.type1.ClassesHere.Constants.PERSON_NAME
import com.kvsc.type1.Navigation_drawer.MainScreen
import com.kvsc.type1.R


//import com.sunfusheng.marqueeview.MarqueeView
//import com.victor.loading.rotate.RotateLoading


class Login : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    var result = false
    var New_login: String? = null
    var left: ImageView? = null
    var right: ImageView? = null
    private var mAuth: FirebaseAuth? = null
    var signInButton: View? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
//    var marqueeView: MarqueeView? = null
    var rotateLoading: ProgressBar? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var myRef: DatabaseReference = database.getReference("login")
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        Log.i("Check", "onStart: called");
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        //        Log.i("Check", "onCreate: called");
        mAuth = FirebaseAuth.getInstance()
        signInButton = findViewById<View>(R.id.sign_in_button)
//        left = findViewById<View>(R.id.left) as ImageView
//        right = findViewById<View>(R.id.right) as ImageView
        rotateLoading = findViewById<View>(R.id.rotateloading) as ProgressBar
        rotateLoading?.visibility=View.INVISIBLE


//        marqueeView=(MarqueeView)findViewById(R.id.marquee);
//        ArrayList<String> arr=new ArrayList<>();
//        arr.add("Welcome to");
//        arr.add("Kalpavriksh Labs");
//        arr.add("Login Below..");
//        marqueeView.startWithList(arr);
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.duration = 10000L
//        animator.addUpdateListener { animation ->
//            val progress = animation.animatedValue as Float
//            val width = left!!.width.toFloat()
//            val translationX = width * progress
//            right!!.translationX = translationX
//            left!!.translationX = translationX - width
//        }
//        animator.start()
        signInButton!!.setOnClickListener(View.OnClickListener {
            if (isNetworkAvailable) {
                rotateLoading?.visibility=View.VISIBLE
                signIn()
            } else {
                Toast.makeText(this@Login, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        })
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Login)
                    val gender = preferences.getString(GENDER, null)
                    Log.i("Checkii", KEY.toString() + " hbjbj")
                    val user: FirebaseUser = mAuth!!.getCurrentUser()!!
                    val email: String = user.getEmail()!!
                    var my_key = ""
                    for (i in 0 until email.length) {
                        if (email[i] != '.') my_key += email[i]
                    }

                    myRef.child(my_key)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
//                                    // user exists in the database
//                                    val userName = dataSnapshot.child("userName").getValue(
//                                        String::class.java
//                                    )
//                                    val intent = Intent(this@MainActivity, Chat::class.java)
//                                    startActivity(intent)
                                    startActivity(Intent(this@Login, MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                                    this@Login.finish()

                                } else {
                                    // user does not exist in the database
//                                    val intent =
//                                        Intent(this@MainActivity, UploadUserInfo::class.java)
//                                    startActivity(intent)
                                    startActivity(Intent(this@Login, EnterDetails::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                                    this@Login.finish()

                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
//                    if (gender != null) {
//                        startActivity(Intent(this@Login, MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                        this@Login.finish()
//                        //                        Log.i("Check4", preferences.getString(KEY,null));
//                    } else {
//                        startActivity(Intent(this@Login, EnterDetails::class.java))
//                        //                        Log.i("Check4", preferences.getString(KEY,null));
//                    }
                    rotateLoading?.visibility=View.INVISIBLE
                }
            }
        }

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
//        var mGoogleApiClient = GoogleApiClient.Builder(this)
//            .enableAutoManage(
//                this /* FragmentActivity */,
//                this /* OnConnectionFailedListener */
//            )
//            .addApi(Drive.API)
//            .addScope(Drive.SCOPE_FILE)
//            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//            .addApi(com.sun.org.apache.xpath.internal.operations.Plus.API)
            .build()
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private fun signIn() {
//        Log.i("Check", "signin");
        val signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.i("Check", "onActivityResult: called");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
                rotateLoading?.visibility=View.INVISIBLE
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("login", "Google sign in failed", e)
                // ...
                Toast.makeText(this@Login, "Google sign in failed", Toast.LENGTH_SHORT).show()
                rotateLoading?.visibility=View.INVISIBLE
            }
        }
    }

//    var doubleBackToExitPressedOnce = false
//    override fun onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show()
//        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
//    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

//        Log.i("Check", "firebaseAuthWithGoogle");
        val credential: AuthCredential =
            GoogleAuthProvider.getCredential(account.getIdToken(), null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        Log.d("login", "signInWithCredential:success")
//                        val person: Person =
//                            com.sun.org.apache.xpath.internal.operations.Plus.PeopleApi.getCurrentPerson(
//                                mGoogleApiClient
//                            )
                        val user: FirebaseUser = mAuth!!.getCurrentUser()!!
                        val email: String = user.getEmail()!!
                        var my_key = ""
                        for (i in 0 until email.length) {
                            if (email[i] != '.') my_key += email[i]
                        }
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@Login)
                        val editor = preferences.edit()
                        editor.putString(KEY, my_key)
//                        Toast.makeText(
//                            this@Login,
//                            "11111111 done went wrong! $my_key",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        editor.putString(PERSON_NAME, user.getDisplayName())
                        editor.apply()
                        editor.commit()
                        //                            Log.i("Check3", preferences.getString(KEY, null));
                        New_login = my_key
                        Toast.makeText(
                            this@Login,
                            user.getDisplayName().toString() + " Authenticated",
                            Toast.LENGTH_SHORT
                        ).show()
                        rotateLoading?.visibility=View.INVISIBLE
                    } else {
                        Log.w("login", "signInWithCredential:failure", task.getException())
                        Toast.makeText(this@Login, "Authentication Failed", Toast.LENGTH_SHORT)
                            .show()
                        rotateLoading?.visibility=View.INVISIBLE
                    }
                }
            })
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        rotateLoading?.visibility=View.INVISIBLE
        //        Log.i("Check", "onConnectionFailed: ");
        Toast.makeText(this@Login, "Authentication Failed", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val RC_SIGN_IN = 2
        var mGoogleSignInClient: GoogleSignInClient? = null
    }
}