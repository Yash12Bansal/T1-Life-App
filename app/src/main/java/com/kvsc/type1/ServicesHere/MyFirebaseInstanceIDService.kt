package com.kvsc.type1.ServicesHere

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {

        //For registration of token
        val refreshedToken: String? = FirebaseInstanceId.getInstance().getToken()

        //To displaying token on logcat
        if (refreshedToken != null) {
            Log.d("TOKEN: ", refreshedToken)
        }
    }
}