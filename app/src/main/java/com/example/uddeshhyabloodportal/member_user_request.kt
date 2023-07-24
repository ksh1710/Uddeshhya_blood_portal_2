package com.example.uddeshhyabloodportal

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.uddeshhyabloodportal.databinding.ActivityMemberUserRequestBinding
import com.google.firebase.auth.FirebaseAuth

class member_user_request : AppCompatActivity() {
    private lateinit var binding: ActivityMemberUserRequestBinding


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberUserRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bloodReqBtn = binding.bloodReqBtn
        val createReqBtn = binding.createRequest

        bloodReqBtn.setOnClickListener {
            val intent = Intent(this, membersList::class.java)
            startActivity(intent)
        }

        createReqBtn.setOnClickListener {
            val intent = Intent(this,bloodRequestWritten::class.java)
            startActivity(intent)
            finish()
        }

    }
}
