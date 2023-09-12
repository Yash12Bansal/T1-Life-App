package com.kvsc.type1.Adapters

import com.kvsc.type1.R
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kvsc.type1.Activities.DataBaseFoodHelper
import com.kvsc.type1.ClassesHere.Constants.DIRECT_INSULIN_SCREEN
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_ID
import com.kvsc.type1.ClassesHere.Constants.FOOD_HELPER_DB_NAME
import com.kvsc.type1.ClassesHere.FoodUser
import com.kvsc.type1.Navigation_drawer.MainScreen
import java.lang.String
import kotlin.Int
import kotlin.arrayOf


class Food_DB_Adapter : RecyclerView.Adapter<Food_DB_Adapter.ViewHolder> {
    var mContext: Context? = null
    var users: ArrayList<FoodUser> = ArrayList<FoodUser>()

    constructor(users: ArrayList<FoodUser>) {
        this.users = users
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val contactView: View = inflater.inflate(R.layout.fooditemshow, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    constructor(context: Context?, user: ArrayList<FoodUser>) {
        mContext = context
        users = user
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.food.setText(users[position].food)
        holder.quantity.setText(users[position].quantity)
        holder.carbs.setText(users[position].carbs)
        holder.button.setOnClickListener {
            val b = AlertDialog.Builder(mContext)
            b.setTitle("Alert")
            b.setMessage("Are you sure you want to delete this food item?")
            b.setPositiveButton(
                "Yes"
            ) { dialogInterface, index ->
                val helper = DataBaseFoodHelper(mContext)
                val db = helper.writableDatabase
                db.delete(
                    FOOD_HELPER_DB_NAME,
                    FOOD_HELPER_DB_ID.toString() + "=?",
                    arrayOf(
                        String.valueOf(
                            users[position].id
                        )
                    )
                )
                db.close()
                Toast.makeText(mContext, "Food item deleted!", Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, MainScreen::class.java)
                intent.putExtra(DIRECT_INSULIN_SCREEN, "predict")
                mContext!!.startActivity(intent)
            }
            b.setNegativeButton(
                "No"
            ) { dialogInterface, i -> dialogInterface.cancel() }
            b.show()
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        ImageView imageView ;
        var food: TextView
        var quantity: TextView
        var carbs: TextView
        var button: ImageButton

        init {
            quantity = itemView.findViewById(R.id.quantity_here)
            food = itemView.findViewById<View>(R.id.food_here) as TextView
            carbs = itemView.findViewById<View>(R.id.carbs_here) as TextView
            button = itemView.findViewById(R.id.button)
        }
    }
}