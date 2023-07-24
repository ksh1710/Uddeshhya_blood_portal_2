package com.example.uddeshhyabloodportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.uddeshhyabloodportal.databinding.ActivityMemberPreviewImageBinding

class memberPreviewImage : AppCompatActivity() {
    private lateinit var binding: ActivityMemberPreviewImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberPreviewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val img =intent.getStringExtra("fbimg")
        Glide.with(this).load(img).into(binding.previewReqImg)

    }
}