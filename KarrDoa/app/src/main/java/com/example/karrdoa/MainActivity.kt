package com.example.karrdoa

import android.app.usage.UsageEvents
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karrdoa.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView

    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        rvMain = findViewById(R.id.rvMain)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        rvMain.layoutManager = linearLayoutManager


        val button = findViewById<Button>(R.id.button6)
        button.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this, EventActivity::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<Button>(R.id.button5)
        button3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val sharedPref = applicationContext.getSharedPreferences(
            "mySharedPreferences",
            Context.MODE_PRIVATE
        )

        val token = sharedPref.getString("token", null)
        if (token != null) {
            getAllEvents(token = token)
        } else {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAllEvents(token: String) {
        val okHttpClient = CustomOkHttpClient().create()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://192.168.1.39:7017")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getEvents("Bearer $token").enqueue(object : Callback<List<EventsItem>> {
            override fun onResponse(call: Call<List<EventsItem>>, response: Response<List<EventsItem>>) {
                if(response.isSuccessful){
                    val eventsItemMainList = response.body()
//                    eventsItemList.stream().map()
//                    eventsItemList?.forEach()

                    if(eventsItemMainList != null && eventsItemMainList.isNotEmpty()) {
                        val recAdapter = EventsAdapter(context = this@MainActivity, events = eventsItemMainList, token = token)
                        rvMain.adapter = recAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<EventsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun event(token: String): ApiService {
        val okHttpClient = CustomOkHttpClient().create()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://192.168.1.39:7017")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getEvents("Bearer $token").enqueue(object : Callback<List<EventsItem>> {
            override fun onResponse(call: Call<List<EventsItem>>, response: Response<List<EventsItem>>) {
                if(response.isSuccessful){
                    val body = response.body()
                }
            }

            override fun onFailure(call: Call<List<EventsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        return retrofit.create(ApiService::class.java)
}

    suspend fun getEvents(token: String): List<UsageEvents.Event> {
        return withContext(Dispatchers.IO) {
            try {
                //val response = apiService.getEvents("Bearer $token")
                val apiService = event(token) // ApiService'yi oluştur
                val response = apiService.getEvents("Bearer $token")
//                if (response.isSuccessful) {
//                    return@withContext response.body() ?: emptyList()
//                } else {
//
//                    return@withContext emptyList()
//                }
                return@withContext emptyList()
            } catch (e: Exception) {
                // Hata durumunda yapılacak işlemleri buraya ekleyebilirsiniz
                return@withContext emptyList()
            }
        }
    }


}

