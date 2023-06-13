package com.example.karrdoa

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {


    private lateinit var etFirstname: EditText
    private lateinit var etLastname: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPasswordhash: EditText
    private lateinit var etPasswordverify: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        etFirstname = findViewById(R.id.firstname)
        etLastname = findViewById(R.id.lastname)
        etUsername = findViewById(R.id.username)
        etEmail = findViewById(R.id.email)
        etPasswordhash = findViewById(R.id.password)
        etPasswordverify = findViewById(R.id.confirm)

        button.setOnClickListener {
            val firstname = etFirstname.text.toString().trim()
            val lastname = etLastname.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val passwordHash = etPasswordhash.text.toString().trim()
            val passwordVerify = etPasswordverify.text.toString().trim()

            if(firstname.isEmpty()){
                etFirstname.error = "Firstname required"
            }else if(lastname.isEmpty()){
                etLastname.error = "Lastname required"
                return@setOnClickListener
            } else if(username.isEmpty()){
                etUsername.error = "Username required"
                return@setOnClickListener
            }else if(email.isEmpty()){
                etEmail.error = "Email required"
                return@setOnClickListener
            }else if(passwordHash.isEmpty()){
                etPasswordhash.error = "Password required"
                return@setOnClickListener
            }else if(passwordVerify.isEmpty()) {
                etPasswordverify.error = "Confirm password required"
                return@setOnClickListener
            }else if(passwordHash != passwordVerify){
                Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            }else if(passwordHash.length < 6){
                Toast.makeText(this, "Password should be 6 characters at least", Toast.LENGTH_SHORT).show()
            }else{
                //Toast.makeText(this, "Registeration completed", Toast.LENGTH_SHORT).show()
                register(firstname, lastname, username, email, passwordHash, passwordVerify)
            }

        }

        button4.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

        private fun register(firstname: String, lastname: String, username: String, email: String, passwordHash: String, passwordVerify: String){
            val okHttpClient = CustomOkHttpClient().create()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://192.168.1.39:7017")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val body = mapOf(
                "firstname" to firstname,
                "lastname" to lastname,
                "username" to username,
                "email" to email,
                "passwordHash" to passwordHash,
                "passwordVerify" to passwordVerify,
            )

            apiService.registerRequest(body).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    println(response.message())
                    println(response.body()?.string())
                    println(response.code())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    throw t
                }
            })
        }
}