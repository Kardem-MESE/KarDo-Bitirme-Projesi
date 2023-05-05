package com.example.karrdoa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.karrdoa.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.button4.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val username = binding.username.text.toString()
            val mail = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmpassword = binding.confirm.text.toString()

            if(username.isNotEmpty() && mail.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()){
                if(password == confirmpassword){
                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            //Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            Toast.makeText(this, "Password should be 6 characters at least", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}