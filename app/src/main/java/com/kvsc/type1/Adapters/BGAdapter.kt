package com.kvsc.type1.Adapters


import com.kvsc.type1.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kvsc.type1.ClassesHere.UserBG


class BGAdapter : RecyclerView.Adapter<BGAdapter.ViewHolder> {
    var mContext: Context? = null
    var users: java.util.ArrayList<UserBG> = ArrayList<UserBG>()

    constructor(users: ArrayList<UserBG>) {
        this.users = users
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val contactView: View =
            inflater.inflate(R.layout.my_item_look_bg, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.type.setText(users[position].bLOOD_GLUCOSE_TYPE)
        holder.amount.setText(users[position].vALUE)
        holder.date.setText(users[position].dATE)
        holder.time.setText(users[position].tIME)
    }

    constructor(context: Context?, user: java.util.ArrayList<UserBG>) {
        mContext = context
        users = user
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        ImageView imageView ;
        var type: TextView
        var amount: TextView
        var date: TextView
        var time: TextView

        init {
            type = itemView.findViewById(R.id.type_here)
            amount = itemView.findViewById<View>(R.id.amount_here) as TextView
            date = itemView.findViewById<View>(R.id.date_here) as TextView
            time = itemView.findViewById(R.id.time_here)
        }
    }
}