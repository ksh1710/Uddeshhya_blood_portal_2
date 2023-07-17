package com.example.uddeshhyabloodportal

import android.R
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.uddeshhyabloodportal.databinding.ActivityImagePreviewBinding


class imagePreview : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val img = this.intent.getStringExtra("previewImg")
//
//        binding.imgPV.setImageURI(img?.toUri())


        val extras = intent.extras
        val byteArray = extras!!.getByteArray("picture")

        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        val image = binding.imgPV

        image.setImageBitmap(bmp)
    }
}