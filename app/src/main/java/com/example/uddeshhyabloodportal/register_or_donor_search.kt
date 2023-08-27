package com.example.uddeshhyabloodportal

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.media.tv.AdRequest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

class register_or_donor_search : AppCompatActivity() {
    lateinit var mAdView : AdView

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
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
            super.onAdClicked()
            // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                super.onAdFailedToLoad(adError)
                mAdView.loadAd(adRequest)
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                super.onAdOpened()
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }



        registerBtn.setOnClickListener {
            val intent: Intent = Intent(this, donorRegistration::class.java)
            startActivity(intent)
        }
        donorSearchBtn.setOnClickListener {
            val intent: Intent = Intent(this, verify_urself_first::class.java)
            startActivity(intent)
        }
        aboutUs.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        aboutUs.setOnClickListener {
            val intent: Intent = Intent(this,AboutUs::class.java)
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