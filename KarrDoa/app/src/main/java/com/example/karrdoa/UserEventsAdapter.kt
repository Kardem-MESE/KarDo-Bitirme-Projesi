package com.example.karrdoa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserEventsAdapter(val context: Context, private val events: List<EventsItem>) : RecyclerView.Adapter<UserEventViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_item_layout, parent, false)
        return UserEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserEventViewHolder, position: Int) {
        holder.eventName.text = events[position].name
        holder.eventDate.text = events[position].eventDate
        holder.eventShowtype.text = events[position].showType
    }

    override fun getItemCount(): Int {
        return events.size
    }
}

class UserEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var eventName: TextView = itemView.findViewById(R.id.eventName)
    var eventDate: TextView = itemView.findViewById(R.id.eventDate)
    var eventShowtype: TextView = itemView.findViewById(R.id.showType)
}