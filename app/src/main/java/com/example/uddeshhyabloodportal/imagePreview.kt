package com.example.uddeshhyabloodportal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uddeshhyabloodportal.databinding.ActivityImagePreviewBinding


class imagePreview : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageUri = intent.data
        binding.previewImg.setImageURI(imageUri)

    }
}