package com.example.karrdoa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventsAdapter(val context: Context, private val events: List<EventsItem>, private val token: String) : RecyclerView.Adapter<EventViewHolder>(){

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
            val isJoined = true
            val eventId = events[position].id
            println(eventId)

            val body = mapOf(
                "eventId" to eventId.toString(),
                "isJoined" to isJoined,
            )

            eventsJoin(body = body, token = token, holder = holder)
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

private fun eventsJoin(body: Map<String, Any>, token: String, holder: EventViewHolder) {
    val okHttpClient = CustomOkHttpClient().create()

    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://192.168.1.39:7017")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    apiService.eventJoin(body, "Bearer $token").enqueue(object : Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            if(response.isSuccessful) {
                holder.joinNames.text = response.body()?.joinToString(prefix = "", postfix = "", separator = ",")
            }
        }

        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            TODO("Not yet implemented")
        }
    })
}


