package com.example.uddeshhyabloodportal

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class register_or_donor_search : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_or_donor_search)
        val registerBtn: Button = findViewById(R.id.registerBtn)
        val donorSearchBtn: Button = findViewById(R.id.donorSearchBtn)
        val aboutUs: TextView = findViewById(R.id.aboutTv)
        val Faqs: TextView = findViewById(R.id.faqTV)
        val member:TextView=findViewById(R.id.userTV)
//        val uddeshhyaString:TextView = findViewById(R.id.uddeshhyaText)


        registerBtn.setOnClickListener {
            val intent: Intent = Intent(this, donorRegistration::class.java)
            startActivity(intent)
        }
        donorSearchBtn.setOnClickListener {
//            val intent: Intent = Intent(this, membersList::class.java)
            val intent: Intent = Intent(this, verify_urself_first::class.java)
            startActivity(intent)
        }
        aboutUs.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        aboutUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.uddeshhya.org/"))
            startActivity(intent)
        }
        Faqs.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        Faqs.setOnClickListener {
            val intent: Intent = Intent(this, faqs::class.java)
            startActivity(intent)
        }

        member.paintFlags=Paint.UNDERLINE_TEXT_FLAG
        member.setOnClickListener {
            val intent = Intent(this,memberLogin::class.java)
            startActivity(intent)
        }


    }
}