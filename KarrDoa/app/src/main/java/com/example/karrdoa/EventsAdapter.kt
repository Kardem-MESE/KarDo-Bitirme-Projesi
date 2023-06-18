package com.example.karrdoa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventsAdapter(val context: Context, private val events: List<EventsItem>) : RecyclerView.Adapter<EventViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.eventName.text = events[position].name
        holder.eventDate.text = events[position].eventDate
        holder.eventShowtype.text = events[position].showType
        val uNameJoin = events[position].userJoinNames
        if(uNameJoin != null && uNameJoin.isNotEmpty()){
            holder.joinNames.text = uNameJoin.joinToString(prefix = "", postfix = "", separator = ",")
        } else {
            holder.joinNames.text = ""
        }
        holder.join.setOnClickListener{
            val joinedUser = events[position].userJoinNames
            print(joinedUser)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var eventName: TextView = itemView.findViewById(R.id.eventName)
    var eventDate: TextView = itemView.findViewById(R.id.eventDate)
    var eventShowtype: TextView = itemView.findViewById(R.id.showType)
    var join: ImageView = itemView.findViewById(R.id.join)
    var joinNames: TextView = itemView.findViewById(R.id.userJoinNames)
}