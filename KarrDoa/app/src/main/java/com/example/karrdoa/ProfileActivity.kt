package com.example.karrdoa

import android.app.usage.UsageEvents
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity :AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var profilusername: TextView
    private lateinit var profilemail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name = findViewById(R.id.name)
        profilusername = findViewById(R.id.profilusername)
        profilemail = findViewById(R.id.profilemail)

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

        val sharedPref = applicationContext.getSharedPreferences(
            "mySharedPreferences",
            Context.MODE_PRIVATE
        )

        val button10 = findViewById<Button>(R.id.button10)
        button10.setOnClickListener {
            val editor = sharedPref.edit()
            editor.putString("token", null)
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val token = sharedPref.getString("token", null)
        if (token != null) {
            getUser(token = token)
            // GET USER INFO
            // https://localhost:7017/api/User
        } else {
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getUser(token: String){
        val okHttpClient = CustomOkHttpClient().create()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://192.168.1.39:7017")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getUser("Bearer $token").enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if(response.isSuccessful) {
                    val getUserItem = response.body()
                    if(getUserItem != null){
                        name.text = "Name: ${getUserItem.fullName}"
                        profilusername.text = "Username: ${getUserItem.userName}"
                        profilemail.text = "Email: ${getUserItem.email}"
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun showProfileData(profileResponse: ProfileResponse) {
        name.text = profileResponse.fullName
        profilusername.text = profileResponse.userName
        profilemail.text = profileResponse.email
    }

}