package com.example.karrdoa

import android.app.usage.UsageEvents
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val button = findViewById<Button>(R.id.button7)
        button.setOnClickListener {
            val intent = Intent(this, EventActivity::class.java)
            startActivity(intent)
        }
        val button2 = findViewById<Button>(R.id.button8)
        button2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val button3 = findViewById<Button>(R.id.button9)
        button3.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        val button10 = findViewById<Button>(R.id.button10)
        button10.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        val sharedPref = applicationContext.getSharedPreferences(
            "mySharedPreferences",
            Context.MODE_PRIVATE
        )
        val token = sharedPref.getString("token", null)
        if (token != null) {
            eventbyId(token = token)
        } else {
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun eventbyId(token: String): ApiService {
        val okHttpClient = CustomOkHttpClient().create()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://192.168.1.39:7017")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getEventsbyId("Bearer $token").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.string()
                    println("jsonResponse = " + jsonResponse)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                throw t
            }

        }
        )
        return retrofit.create(ApiService::class.java)
    }

    suspend fun getEventsbyId(token: String): List<UsageEvents.Event> {
        return withContext(Dispatchers.IO) {
            try {
                val apiService = eventbyId(token) // ApiService'yi oluştur
                val response = apiService.getEvents("Bearer $token")
                return@withContext emptyList()
            } catch (e: Exception) {
                // Hata durumunda yapılacak işlemleri buraya ekleyebilirsiniz
                return@withContext emptyList()
            }
        }
    }
}