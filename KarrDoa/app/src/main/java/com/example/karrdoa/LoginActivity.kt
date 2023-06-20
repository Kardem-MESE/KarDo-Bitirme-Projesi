package com.example.karrdoa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.editTextUsername)
        etPassword = findViewById(R.id.editTextPassword)

        buttonLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty()) {
                etUsername.error = "Username required"
                return@setOnClickListener
            } else if (password.isEmpty()) {
                etPassword.error = "Password required"
                return@setOnClickListener
            }else {
                login(username, password)
            }
        }

        button3.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login(username: String, password: String){
        val okHttpClient = CustomOkHttpClient().create()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://192.168.1.39:7017")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val body = mapOf(
            "username" to username,
            "password" to password,
        )

        apiService.loginRequest(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.string()

                    val jsonObject = jsonResponse?.let { JSONObject(it) }
                    val token = jsonObject?.optString("token")

                    if(token != null){
                        if(token.isEmpty()){
                            handleLoginError()
                        } else {
                            saveToken(token)
                            navigateToMainPage()
                        }
                    }
                }else {
                    handleLoginError()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                handleLoginError()
            }
            private fun handleLoginError() {
                Toast.makeText(applicationContext, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()

                clearLoginInputs()
            }
            fun clearLoginInputs() {
                val etUsername = findViewById<EditText>(R.id.editTextUsername)
                val etPassword = findViewById<EditText>(R.id.editTextPassword)
                etUsername.text.clear()
                etPassword.text.clear()
            }
            fun saveToken(token: String) {
                    val sharedPref = applicationContext.getSharedPreferences(
                        "mySharedPreferences",
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPref.edit()
                    editor.putString("token", token)
                    editor.apply()
            }
            fun navigateToMainPage() {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
            }
        })

    }
}






