package com.example.uddeshhyabloodportal

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.uddeshhyabloodportal.databinding.ActivityVerifyUrselfFirstBinding
import com.example.uddeshhyabloodportal.models.bloodReqUser
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class verify_urself_first : AppCompatActivity() {
    lateinit var binding: ActivityVerifyUrselfFirstBinding
    lateinit var database: DatabaseReference
    lateinit var OTP: String
    lateinit var verifyUrselfAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyUrselfFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}

        verifyUrselfAdView = findViewById(R.id.verifyUrselfAd)
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        verifyUrselfAdView.loadAd(adRequest)

        verifyUrselfAdView.adListener = object: AdListener() {
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
                verifyUrselfAdView.loadAd(adRequest)
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



        val sharedPref = getSharedPreferences("userPref", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("name", "")
        val userPhoneNo = sharedPref.getString("phoneNo", "")

        if (userName != "" && userPhoneNo != "") {
            val intent = Intent(this, member_user_request::class.java)
            startActivity(intent)
            finish()
        }




        fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    binding.sendotpProgressbar.isVisible = false
                    binding.userSendOtp.isVisible = true
                    binding.userVerifyOtp.isVisible= true
                    binding.verifyotpProgressBar.isVisible = false

                    if (task.isSuccessful) {
                        binding.userVerifyOtp.text = "Verified"
                        binding.userVerifyOtp.setTextColor(Color.parseColor("#00FF00"))
                        Toast.makeText(
                            applicationContext,
                            "otp verified successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        FirebaseAuth.getInstance().currentUser?.delete()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "an error occured",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    binding.userVerifyOtp.isVisible= true
                    binding.verifyotpProgressBar.isVisible = false
                }
        }


        binding.userSendOtp.setOnClickListener {
            val phone = binding.userPhoneNumber.text.toString()
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                    Toast.makeText(applicationContext, "idfc", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onVerificationFailed(e: FirebaseException) {

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            applicationContext,
                            "invalid mobile number",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.userVerifyOtp.isVisible= true
                        binding.verifyotpProgressBar.isVisible = false
                        binding.sendotpProgressbar.isVisible = false
                        binding.userSendOtp.isVisible = true
                    } else if (e is FirebaseTooManyRequestsException) {
                        Toast.makeText(
                            applicationContext,
                            "too many requests...try later!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        binding.sendotpProgressbar.isVisible = false
                        binding.userSendOtp.isVisible = true
                    } else {
                        Toast.makeText(applicationContext, "an error occured", Toast.LENGTH_SHORT)
                            .show()
                        binding.sendotpProgressbar.isVisible = false
                        binding.userSendOtp.isVisible = true
                    }

                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {

                    OTP = verificationId
                    Toast.makeText(
                        applicationContext,
                        "code sent successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.sendotpProgressbar.isVisible = false
                    binding.userSendOtp.isVisible = true
                }

            }
            if (phone.trim().isNotEmpty()) {
                if (phone.trim().length == 10) {
                    binding.sendotpProgressbar.isVisible = true
                    binding.userSendOtp.isVisible = false
                    val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91$phone") // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "please enter a 10 digit number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "enter mobile NUmber",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }


        binding.userVerifyOtp.setOnClickListener {
            if (binding.otpTextfield.text.toString().isNotEmpty()) {
                if (binding.otpTextfield.text.toString().length == 6) {
                    binding.userVerifyOtp.isVisible = false
                    binding.verifyotpProgressBar.isVisible = true

                    val phoneauthCredential: PhoneAuthCredential =
                        PhoneAuthProvider.getCredential(
                            OTP,
                            binding.otpTextfield.text.toString()
                        )
                    signInWithPhoneAuthCredential(phoneauthCredential)

                } else {
                    Toast.makeText(
                        applicationContext,
                        "enter a valid otp",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "enter the otp", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.reqBtn.setOnClickListener {
            val phoneNo = binding.userPhoneNumber.text.toString()
            val name = binding.userName.text.toString()
            if (binding.userVerifyOtp.text.toString() == "Verified") {
                val editor = sharedPref.edit()
                editor.putString("name", name)
               editor.putString("phoneNo", phoneNo)
                editor.apply()

                database = FirebaseDatabase.getInstance().getReference("blood request users")
                val user = bloodReqUser(name, phoneNo)
                database.child(phoneNo).setValue(user).addOnSuccessListener {
                    binding.userPhoneNumber.text.clear()
                    binding.userName.text.clear()
                }
                val intent: Intent = Intent(this, member_user_request::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "please verify your number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}





