package com.example.uddeshhyabloodportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class register_or_donor_search : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_or_donor_search)
        val registerBtn:Button = findViewById(R.id.registerBtn)
        val donorSearchBtn:Button = findViewById(R.id.donorSearchBtn)


        registerBtn.setOnClickListener {
            val intent: Intent = Intent(this, donorRegistration::class.java)
            startActivity(intent)
        }
            donorSearchBtn.setOnClickListener {
                val intent: Intent = Intent(this, searchDonor::class.java)
                startActivity(intent)

            }
    }
}