package com.kvsc.type1.ServicesHere
import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kvsc.type1.ClassesHere.Constants.FOOD_CONTENT
import com.kvsc.type1.ClassesHere.Constants.FOOD_DB_SIZE
import com.kvsc.type1.ClassesHere.Constants.FOOD_NAME
import java.util.regex.Pattern


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var database2: FirebaseDatabase = FirebaseDatabase.getInstance()
    var myRef2: DatabaseReference = database2.getReference("food_database")
    var food_name_list = ArrayList<String>()
    var mapping = HashMap<String, String>()
    var food_formulations = ArrayList<String>()
    //    @Override
    //    public void onCreate() {
    //
    //        Log.i("food_items_added", " on create h");
    //        AsyncTask.execute(new Runnable() {
    //            @Override
    //            public void run() {
    //                //TODO your background code
    //                Log.i("food_items_added", " on create h");
    //
    //                CheckExist();
    //            }
    //        });
    //        super.onCreate();
    //    }
    override fun onMessageReceived(message: RemoteMessage) {

//        Log.i("food_items_added", message+" h");
//        if(!isNetworkAvailable()){
//
//        }else{
//         //sync work here
//            onCreate();
//        }
//        sendMyNotification(message.getNotification().getBody());
    }

    private fun CheckExist() {
        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.getChildren()) {
                    val temp_string: String = d.getKey().toString()
                    var ans = ""
                    val matcher = Pattern.compile("Serving(.*?),").matcher(d.getValue().toString())
                    while (matcher.find()) {
//                            Log.i("check21", matcher.group(1));
                        ans = matcher.group(1)
                    }
                    val content: String = d.getValue().toString()
                    val food = "$temp_string[ Serving $ans ]"
                    food_formulations.add(content)
                    food_name_list.add(food)
                }
                updatelist()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updatelist() {
        val preferences_contents = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences_contents.edit()
        val getpreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val size = getpreferences.getInt(FOOD_DB_SIZE, 0)
        for (i in 0 until size) {
            try {
                editor.remove(FOOD_NAME + i)
                editor.remove(FOOD_CONTENT + i)
            } catch (E: Exception) {
            }
        }
        if (size != 0) {
            try {
                editor.remove(FOOD_DB_SIZE)
            } catch (e: Exception) {
            }
        }
        var i = 0
        while (i < food_name_list.size && i < food_formulations.size) {
            editor.putString(FOOD_NAME + i, food_name_list[i])
            editor.putString(FOOD_CONTENT + i, food_formulations[i])
            Log.i("food_items_added", food_name_list[i] + " h")
            i++
        }
        editor.putInt(FOOD_DB_SIZE, Math.min(food_formulations.size, food_name_list.size))
        editor.commit()
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager =
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private fun sendMyNotification(message: String) {

//        //On click of notification it redirect to this Activity
//        Intent intent = new Intent(this, Login.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.icon)
//                .setContentTitle("Kalpvriksh Carbometer")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(soundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
    }
}