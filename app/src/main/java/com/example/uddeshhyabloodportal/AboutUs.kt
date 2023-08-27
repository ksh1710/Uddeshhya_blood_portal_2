package com.example.uddeshhyabloodportal

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.uddeshhyabloodportal.databinding.ActivityAboutUsBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

class AboutUs : AppCompatActivity() {

    lateinit var aboutUsAdView : AdView
    private lateinit var binding:ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        MobileAds.initialize(this) {}

        aboutUsAdView = findViewById(R.id.aboutUsAd)
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        aboutUsAdView.loadAd(adRequest)

        aboutUsAdView.adListener = object: AdListener() {
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
                aboutUsAdView.loadAd(adRequest)
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



        binding.website.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.uddeshhya.org/"))
            startActivity(intent)
        }
        binding.instagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/uddeshhya_/"))
            startActivity(intent)
        }
        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/blooxconnect-privacy-policy/home"))
            startActivity(intent)
        }
    }
}