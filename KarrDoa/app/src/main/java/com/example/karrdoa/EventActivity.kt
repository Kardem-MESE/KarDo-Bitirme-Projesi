package com.example.karrdoa

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_event.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class EventActivity: AppCompatActivity() {

    private lateinit var rvevent : RecyclerView

    private lateinit var tvDatePicker : TextView
    private lateinit var btnDatePicker : Button

    private lateinit var etEventName: EditText
    private lateinit var rbEventshowtype: RadioButton
    private lateinit var rbEventshowtype1: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        rvevent = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        rvevent.layoutManager = linearLayoutManager

        tvDatePicker = findViewById(R.id.tvdate)

        create.setOnClickListener {
            etEventName = findViewById(R.id.plan)
            rbEventshowtype = findViewById(R.id.rbPublic)
            rbEventshowtype1 = findViewById(R.id.rbPrivate)

            var isStatus = "Private"
            if(rbEventshowtype.isChecked && !rbEventshowtype1.isChecked) {
                isStatus = "Public"
            }

            val eventName = etEventName.text.toString().trim()
            val date = tvDatePicker.text.toString()

            val body = mapOf(
                "name" to eventName,
                "showType" to isStatus,
                "eventDate" to date
            )

            val sharedPref = applicationContext.getSharedPreferences(
                "mySharedPreferences",
                Context.MODE_PRIVATE
            )

            val token = sharedPref.getString("token", null)
            if (token != null) {
                createEvents(body = body, token = token)
            } else {
                val intent = Intent(this@EventActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val button = findViewById<Button>(R.id.button13)
        button.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        val button2 = findViewById<Button>(R.id.button12)
        button2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val button3 = findViewById<Button>(R.id.button11)
        button3.setOnClickListener {
            val intent = Intent(this, EventActivity::class.java)
            startActivity(intent)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rbPublic)
                Toast.makeText(this, rbPublic.text.toString(), Toast.LENGTH_SHORT).show()
            if(checkedId == R.id.rbPrivate)
                Toast.makeText(this, rbPrivate.text.toString(), Toast.LENGTH_SHORT).show()
        }
        tvDatePicker = findViewById(R.id.tvdate)
        btnDatePicker = findViewById(R.id.picker)

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR , year)
            myCalendar.set(Calendar.MONTH , month)
            myCalendar.set(Calendar.DAY_OF_MONTH , dayOfMonth)
            updateLable(myCalendar)
        }

        btnDatePicker.setOnClickListener {
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val sharedPref = applicationContext.getSharedPreferences(
            "mySharedPreferences",
            Context.MODE_PRIVATE
        )

        val token = sharedPref.getString("token", null)
        if (token != null) {
            getAllEvents(token = token)
        } else {
            val intent = Intent(this@EventActivity, LoginActivity::class.java)
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

        apiService.getEventsbyId("Bearer $token").enqueue(object : Callback<List<EventsItem>> {
            override fun onResponse(call: Call<List<EventsItem>>, response: Response<List<EventsItem>>) {
                if(response.isSuccessful){
                    val eventsItemMainList = response.body()

                    if(eventsItemMainList != null && eventsItemMainList.isNotEmpty()) {
                        val recAdapter = UserEventsAdapter(context = this@EventActivity, events = eventsItemMainList)
                        rvevent.adapter = recAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<EventsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun createEvents(body: Map<String, Any>, token: String) {
        val okHttpClient = CustomOkHttpClient().create()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://192.168.1.39:7017")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.createEvent(body, "Bearer $token").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    val body = response.body()?.string()
                    println(body)

                    getAllEvents(token)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("HANDLE NOT SUCCESSFUL")
            }
        })
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = sdf.format(myCalendar.time)
    }
}
