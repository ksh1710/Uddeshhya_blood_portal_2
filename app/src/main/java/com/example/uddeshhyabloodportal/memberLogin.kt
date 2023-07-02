package com.example.uddeshhyabloodportal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uddeshhyabloodportal.databinding.ActivityMemberLoginBinding
import com.google.firebase.auth.FirebaseAuth

class memberLogin : AppCompatActivity() {
    lateinit var binding: ActivityMemberLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val userEmail = binding.memberEmail.text.toString().trim()
//        val userPassword = binding.memberPassword.text.toString()
//
//
        val loginBtn = binding.memberLoginBtn
        val sharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE)
        val memberEmail = sharedPref.getString("Email","")
        val memberPass = sharedPref.getString("Password","")

        if (memberEmail != "" && memberPass!=""){
            val intent =Intent(this,searchDonor::class.java)
            startActivity(intent)
            finish()
        }
//        with(sharedPref.edit()){
//            putString("Email",userEmail)
//            putString("Password",userPassword)
//            apply()
//        }

        loginBtn.setOnClickListener {
            val userEmail = binding.memberEmail.text.toString().trim()
            val userPassword = binding.memberPassword.text.toString().trim()

            if (userEmail.isBlank() || userPassword.isBlank()) {
                Toast.makeText(this, "please enter your credentials", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedPref.edit()
                editor.putString("Email",userEmail)
                editor.putString("Password",userPassword)
                editor.apply()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val intent = Intent(this, searchDonor::class.java)
//                            intent.putExtra("email",userEmail)
//                            intent.putExtra("password",userPassword)
                            startActivity(intent)
                            Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show()
//                            finish()
                        } else {
                            Toast.makeText(this, "invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}