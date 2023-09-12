package com.kvsc.type1.Adapters
import com.kvsc.type1.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kvsc.type1.ClassesHere.FoodUserLog


class Food_DB_AdapterLog : RecyclerView.Adapter<Food_DB_AdapterLog.ViewHolder> {
    var mContext: Context? = null
    var users: ArrayList<FoodUserLog> = ArrayList<FoodUserLog>()

    constructor(users: ArrayList<FoodUserLog>) {
        this.users = users
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val contactView: View = inflater.inflate(R.layout.myitemlook, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    constructor(context: Context?, user: ArrayList<FoodUserLog>) {
        mContext = context
        users = user
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.food.setText(users[position].food)
        holder.quantity.setText(users[position].quantity)
        holder.carbs.setText(users[position].carbs)
        holder.category.setText(users[position].category)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        ImageView imageView ;
        var food: TextView
        var quantity: TextView
        var carbs: TextView
        var date: TextView? = null
        var time: TextView? = null
        var category: TextView

        init {
            quantity = itemView.findViewById(R.id.quantity_here)
            food = itemView.findViewById<View>(R.id.food_here) as TextView
            carbs = itemView.findViewById<View>(R.id.carbs_here) as TextView
            category = itemView.findViewById(R.id.category_here)
        }
    }
}