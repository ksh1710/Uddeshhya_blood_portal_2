package com.example.uddeshhyabloodportal

import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.uddeshhyabloodportal.databinding.ActivityDonorRegistrationBinding
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.customDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthProvider.getCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


class donorRegistration : AppCompatActivity() {
    lateinit var binding: ActivityDonorRegistrationBinding
    lateinit var database: DatabaseReference
    lateinit var credential: PhoneAuthCredential
    lateinit var OTP: String
    lateinit var donorRegistrationAdView: AdView
    private lateinit var auth: FirebaseAuth
    final val fauth = FirebaseAuth.getInstance().currentUser
val loading = customDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        donorRegistrationAdView = findViewById(R.id.donorRegistrationAd)
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        donorRegistrationAdView.loadAd(adRequest)

        donorRegistrationAdView.adListener = object: AdListener() {
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
                donorRegistrationAdView.loadAd(adRequest)
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



        auth = Firebase.auth
        val cityArray = resources.getStringArray(R.array.city)
        val occupationArray = resources.getStringArray(R.array.occupation)
        val genderArray = resources.getStringArray(R.array.gender)
        val cityAdapter = ArrayAdapter(this, R.layout.spinner_item, cityArray)
        val bloodgroupArray = resources.getStringArray(R.array.bloodGroup)
        val bloodgroupAdapter = ArrayAdapter(this, R.layout.spinner_item, bloodgroupArray)
        val genderAdapter = ArrayAdapter(this, R.layout.spinner_item, genderArray)
        val occupationAdapter = ArrayAdapter(this, R.layout.spinner_item, occupationArray)
        binding.inputFieldSpinner.setAdapter(occupationAdapter)
        binding.genderSpinner.setAdapter(genderAdapter)
        binding.spinnerBloodGroup.setAdapter(bloodgroupAdapter)
        binding.citySpinner.setAdapter(cityAdapter)


        fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    binding.mobileVerifyOtp.isVisible = true
                    binding.verifyotpProgressBar.isVisible = false
                    binding.sendOtpProgressBar.isVisible = false
                    binding.mobileSendOtp.isVisible = true
                    if (task.isSuccessful) {
                        binding.mobileVerifyOtp.text = "Verified"
                        binding.mobileVerifyOtp.setTextColor(Color.parseColor("#00FF00"))
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
                        Log.e("idk",task.exception.toString())
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    binding.mobileVerifyOtp.isVisible = true
                    binding.verifyotpProgressBar.isVisible = false

                }

        }

        binding.mobileSendOtp.setOnClickListener {
            val phone = binding.editTextPhone.text.toString()
            val callbacks = object : OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)

                }

                override fun onVerificationFailed(e: FirebaseException) {

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            applicationContext,
                            "invalid mobile number",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.mobileSendOtp.isVisible = true
                        binding.sendOtpProgressBar.isVisible = false

                        binding.mobileVerifyOtp.isVisible = true
                        binding.verifyotpProgressBar.isVisible = false

                    } else if (e is FirebaseTooManyRequestsException) {
                        Toast.makeText(
                            applicationContext,
                            "too many requests...try later!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.mobileSendOtp.isVisible = true
                        binding.sendOtpProgressBar.isVisible = false

                    } else {
                        Toast.makeText(applicationContext, "an error eoccured", Toast.LENGTH_SHORT)
                            .show()
                        binding.mobileSendOtp.isVisible = true
                        binding.sendOtpProgressBar.isVisible = false

                        binding.mobileVerifyOtp.isVisible = true
                        binding.verifyotpProgressBar.isVisible = false

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

                    binding.sendOtpProgressBar.isVisible = false
                    binding.mobileSendOtp.isVisible = true
                }

            }
            if (phone.trim().isNotBlank()) {
                if (phone.trim().length == 10) {
                    binding.sendOtpProgressBar.isVisible = true
                    binding.mobileSendOtp.isVisible = false

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
                    "enter mobile number",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }




        binding.mobileVerifyOtp.setOnClickListener {
            if (binding.otpTextfield.text.toString().isNotEmpty()) {
                if (binding.otpTextfield.text.toString().length == 6) {
                    binding.mobileVerifyOtp.isVisible = false
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

        binding.emailSendotp.setOnClickListener {
            binding.emailProgressBar.isVisible = true
            binding.emailSendotp.isVisible = false
            if (binding.editTextEmailAddress.text.isBlank()) {
                Toast.makeText(this, "please enter your email id", Toast.LENGTH_SHORT).show()
                binding.emailProgressBar.isVisible = false
                binding.emailSendotp.isVisible = true
            } else if (binding.editTextEmailAddress.text.isNotBlank()) {
                if (binding.emailSendotp.text.toString() == "send email") {
                    val pass = "xxxxxxxx"
                    val email = binding.editTextEmailAddress.text.toString().trim()
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->

                            if (task.isSuccessful) {
                                auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "verification link sent successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.emailProgressBar.isVisible = false
                                    binding.emailSendotp.isVisible = true
                                    binding.emailSendotp.text = "verified??"
                                }
                            }
                        }
                } else if (binding.emailSendotp.text == "verified??") {
                    val pass = "xxxxxxxx"
                    val email = binding.editTextEmailAddress.text.toString().trim()
                    auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                        if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                            binding.emailSendotp.setTextColor(
                                Color.parseColor(
                                    "#00FF00"
                                )
                            )
                            binding.emailSendotp.text = "success!!"
                            binding.emailProgressBar.isVisible = false
                            binding.emailSendotp.isVisible = true

                            FirebaseAuth.getInstance().currentUser?.delete()
                        } else {
                            Toast.makeText(
                                this,
                                "please verify your email",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.emailProgressBar.isVisible = false
                            binding.emailSendotp.isVisible = true

                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "error!!", Toast.LENGTH_SHORT)
                            .show()
                        binding.emailProgressBar.isVisible = false
                        binding.emailSendotp.isVisible = true

                    }
                } else if (binding.emailSendotp.text == "success!!") {
                    Toast.makeText(this, "email already verified", Toast.LENGTH_SHORT)
                        .show()
                    binding.emailProgressBar.isVisible = false
                    binding.emailSendotp.isVisible = true

                }

            }
        }

        binding.registerButton.setOnClickListener {

            FirebaseAuth.getInstance().currentUser?.delete()
            val fullname = binding.fullName.text.toString()
            val occupationSpinner = binding.inputFieldSpinner.text.toString()
            val age = binding.Age.text.toString()
            val phone = binding.editTextPhone.text.toString()
            val altmobile = binding.alternateNumber.text.toString()
            val email = binding.editTextEmailAddress.text.toString()
            val gender = binding.genderSpinner.text.toString()
            val city = binding.citySpinner.text.toString()
            val bloodgroup = binding.spinnerBloodGroup.text.toString()
            val pincode = binding.pincodeInput.text.toString()
            val chronicCbCheck: String
            val ageweightCB: String
            if (binding.AgeWeightCheck.isChecked) {
                ageweightCB = "Yes"
            } else {
                ageweightCB = "No"
            }
            if (binding.chronicCB.isChecked) {
                chronicCbCheck = "No"
            } else {
                chronicCbCheck = "Yes"
            }

            if (fullname.isBlank() || occupationSpinner.isBlank() || age.isBlank() || phone.isBlank() || email.isBlank() || gender.isBlank() || city.isBlank() || bloodgroup.isBlank() || pincode.isBlank() || !binding.AgeWeightCheck.isChecked || !binding.chronicCB.isChecked) {
                Toast.makeText(
                    this,
                    "please fill all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.emailSendotp.text == "send email" || binding.emailSendotp.text == "verified??") {
                Toast.makeText(
                    this@donorRegistration,
                    "please verify  email first",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (binding.mobileVerifyOtp.text != "Verified") {
                Toast.makeText(this, "verify mobile first", Toast.LENGTH_SHORT).show()
            } else {
                loading.dialogRunning()
//                var flag  = true
                FirebaseDatabase.getInstance().getReference("BloodDonors").child(bloodgroup)
                    .addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.hasChild(binding.editTextPhone.text.toString())) {
//                                if (snapshot.hasChild(binding.editTextPhone.text.toString())&&flag) {

                                    Toast.makeText(
                                        this@donorRegistration,
                                        "user already exists",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    database =
                                        FirebaseDatabase.getInstance().getReference("BloodDonors")
                                    val donor = Donor(
                                        fullname,
                                        bloodgroup,
                                        phone,
                                        email,
                                        city,
                                        age,
                                        occupationSpinner,
                                        pincode,
                                        gender,
                                        altmobile,
                                        chronicCbCheck,
                                        ageweightCB
                                    )
                                    database.child(bloodgroup).child(phone)
                                        .setValue(donor)
                                        .addOnSuccessListener {
                                            binding.fullName.text.clear()
                                            binding.editTextPhone.text.clear()
                                            binding.editTextEmailAddress.text.clear()
                                            binding.Age.text.clear()
                                            binding.citySpinner.text.clear()
                                            binding.spinnerBloodGroup.text.clear()
                                            binding.pincodeInput.text.clear()
                                            binding.genderSpinner.text.clear()
                                            binding.alternateNumber.text.clear()
                                            binding.otpTextfield.text.clear()
                                            binding.chronicCB.isSelected = false
                                            binding.AgeWeightCheck.isSelected = false
                                            loading.dialogClose()
                                            Toast.makeText(
                                                this@donorRegistration,
                                                "Successfully Registered",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            finish()
//                                            flag = false
                                        }.addOnFailureListener {
                                            loading.dialogClose()
                                            Toast.makeText(
                                                this@donorRegistration,
                                                "error",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                }

                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
            }
        }
    }
}
