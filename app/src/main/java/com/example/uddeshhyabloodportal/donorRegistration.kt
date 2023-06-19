package com.example.uddeshhyabloodportal

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.uddeshhyabloodportal.databinding.ActivityDonorRegistrationBinding
import com.example.uddeshhyabloodportal.models.Donor
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
import java.util.concurrent.TimeUnit


class donorRegistration : AppCompatActivity() {
    lateinit var binding: ActivityDonorRegistrationBinding
    lateinit var database: DatabaseReference
    lateinit var credential: PhoneAuthCredential
    lateinit var OTP: String
    private lateinit var auth: FirebaseAuth
    final val fauth = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        FirebaseApp.initializeApp(/*context=*/this)
//        val firebaseAppCheck = FirebaseAppCheck.getInstance()
//        firebaseAppCheck.installAppCheckProviderFactory(
//            PlayIntegrityAppCheckProviderFactory.getInstance()
//        )


        auth = Firebase.auth
        val cityArray = resources.getStringArray(R.array.city)
        val occupationArray = resources.getStringArray(R.array.occupation)
        val genderArray = resources.getStringArray(R.array.gender)
//        val branchArray = resources.getStringArray(R.array.Branch)
//        val yearArray = resources.getStringArray(R.array.year)
        val cityAdapter = ArrayAdapter(this, R.layout.spinner_item, cityArray)
        val bloodgroupArray = resources.getStringArray(R.array.bloodGroup)
        val bloodgroupAdapter = ArrayAdapter(this, R.layout.spinner_item, bloodgroupArray)
        val genderAdapter = ArrayAdapter(this, R.layout.spinner_item, genderArray)
//        val branchAdapter = ArrayAdapter(this, R.layout.spinner_item, branchArray)
        val occupationAdapter = ArrayAdapter(this, R.layout.spinner_item, occupationArray)
//        val yearAdapter = ArrayAdapter(this, R.layout.spinner_item, yearArray)
        binding.inputFieldSpinner.setAdapter(occupationAdapter)
        binding.genderSpinner.setAdapter(genderAdapter)
//        binding.branchSpinner.setAdapter(branchAdapter)
        binding.spinnerBloodGroup.setAdapter(bloodgroupAdapter)
        binding.citySpinner.setAdapter(cityAdapter)


        fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
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
                    }
                }
        }

        binding.mobileSendOtp.setOnClickListener {
            val phone = binding.editTextPhone.text.toString()
            val callbacks = object : OnVerificationStateChangedCallbacks() {

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
                    } else if (e is FirebaseTooManyRequestsException) {
                        Toast.makeText(
                            applicationContext,
                            "too many requests...try later!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
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
                }

            }
            if (phone.trim().isNotEmpty()) {
                if (phone.trim().length == 10) {

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




        binding.mobileVerifyOtp.setOnClickListener {
            if (binding.otpTextfield.text.toString().isNotEmpty()) {
                if (binding.otpTextfield.text.toString().length == 6) {
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



        if (binding.emailSendotp.text.toString() == "send email") {
            binding.emailSendotp.setOnClickListener {
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
                                binding.emailSendotp.text = "verified??"
                            }
                        } else if (binding.emailSendotp.text == "verified??") {
                            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                                if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
//                                    Toast.makeText(this, "authenticated", Toast.LENGTH_SHORT).show()
                                    binding.emailSendotp.setTextColor(Color.parseColor("#00FF00"))
                                    binding.emailSendotp.text = "success!!"
//                                    FirebaseAuth.getInstance().currentUser?.delete()
                                }else {
                                    Toast.makeText(
                                        this,
                                        "please verify your email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }.addOnFailureListener {
                                Toast.makeText(this, "errrrrrorrrrr!!!!", Toast.LENGTH_SHORT).show()
                            }
                        }else if(binding.emailSendotp.text == "success!!"){
                        Toast.makeText(this, "email already verified", Toast.LENGTH_SHORT).show()
                    }
                    }
            }
        }

        binding.registerButton.setOnClickListener {

            FirebaseAuth.getInstance().currentUser?.delete()
            Log.d("qwe", "test 1")
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
            val cbCheck: String
            if (binding.chronicCB.isChecked) {
                cbCheck = "No"
            } else {
                cbCheck = "Yes"
            }

            if (fullname.isBlank() || occupationSpinner.isBlank() || age.isBlank() || phone.isBlank() || email.isBlank() || gender.isBlank() || city.isBlank() || bloodgroup.isBlank() || pincode.isBlank()) {
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
                                        cbCheck
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
                                            Toast.makeText(
                                                this@donorRegistration,
                                                "Successfully Registered",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
//                                            flag = false
                                        }.addOnFailureListener {
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
