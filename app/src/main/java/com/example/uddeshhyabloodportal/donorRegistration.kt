package com.example.uddeshhyabloodportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.uddeshhyabloodportal.databinding.ActivityDonorRegistrationBinding
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.bloodPortalViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.FieldPosition
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class donorRegistration : AppCompatActivity() {
    lateinit var binding: ActivityDonorRegistrationBinding
    lateinit var database: DatabaseReference
    lateinit var credential: PhoneAuthCredential
//    var otp: Int? = null!!

    //    lateinit var bloodPortalViewModel: bloodPortalViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorRegistrationBinding.inflate(layoutInflater)

        setContentView(binding.root)
//        bloodPortalViewModel = ViewModelProvider(this).get(bloodPortalViewModel::class.java)
//        bloodPortalViewModel.liveDesignation.observe(this, Observer {
//            if (designation==it){
//                binding.yearSpinner.isVisible=false
//            }
//        })
//        fun des(){
//    val designationCHek = binding.designationSpinner.selectedItem.toString()
//    if (designationCHek=="Faculty"){
//        binding.yearSpinner.isVisible=false
//    }
//        }


        binding.designationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (position == 2) {
                        binding.yearSpinner.isVisible = false
                        binding.libraryId.isVisible = false
                        binding.spinnerBranch.isVisible = false
                    } else if (position == 1) {
                        binding.yearSpinner.isVisible = true
                        binding.libraryId.isVisible = true
                        binding.spinnerBranch.isVisible = true
                    }
//                    } else if (position == 0) {
//                        Toast.makeText(applicationContext,"please select your designation",Toast.LENGTH_SHORT).show()
//                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


//        binding.mobileSendOtp.setOnClickListener {
//            if (phone.trim().isNotEmpty()) {
//                if (phone.trim().length == 10) {
//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        "+91$phone",
//                        60,
//                        TimeUnit.SECONDS,
//                        this,
//                        object : OnVerificationStateChangedCallbacks() {
//                            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                                binding.mobileSendOtp.text="sent"
//                            }
//
//                            override fun onVerificationFailed(p0: FirebaseException) {
//                                Toast.makeText(applicationContext, " verification failed", Toast.LENGTH_SHORT).show()
//                            }
//
//                            override fun onCodeSent(
//                                backendOtp: String, p1: PhoneAuthProvider.ForceResendingToken) {
//                                otp = backendOtp.toInt()
//                                Log.d("idk",otp.toString())
////                                    val intent: Intent = Intent(
////                                        this@donorRegistration,
////                                        donorRegistration::class.java
////                                    )
////                                    intent.putExtra("backendotp", backendOtp)
//                            }
//                        }
//                    )
//                } else {
//                    Toast.makeText(
//                        applicationContext,
//                        "please enter a 10 digit number",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                Toast.makeText(applicationContext, "enter mobile NUmber", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }


//        binding.mobileVerifyOtp.setOnClickListener {
//            if (binding.otpTextfield.text.toString().isNotEmpty()) {
////                    val getotp = intent.getStringExtra("backendotp")
////                    if (binding.otpTextfield.text.toString().length == getotp?.length) {
//                if (binding.otpTextfield.text.toString().length == otp.toString().length) {
//                    val phoneauthCredential: PhoneAuthCredential = PhoneAuthProvider.getCredential(otp.toString(), binding.otpTextfield.text.toString())
//
//                    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//                        FirebaseAuth.getInstance().signInWithCredential(credential)
//                            .addOnCompleteListener{ task ->
//                                if (task.isSuccessful) {
//
//                                    binding.mobileVerifyOtp.text = "Verified"
//                                    // Sign in success, update UI with the signed-in user's information
////                                        Log.d(TAG, "signInWithCredential:success")
//
////                                        val user = task.result?.user
//                                } else {
//                                    Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                    }
//                    signInWithPhoneAuthCredential(phoneauthCredential)
//                } else {
//                    Toast.makeText(
//                        applicationContext,
//                        "check internet connection",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                Toast.makeText(applicationContext, "enter otp", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.mobileSendOtp.setOnClickListener {
            val phone = binding.editTextPhone.text.toString()
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationFailed(e: FirebaseException) {

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
                    } else if (e is FirebaseTooManyRequestsException) {
                        Toast.makeText(applicationContext, "failed 2.0", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
//                    val credential =  PhoneAuthProvider.getCredential(verificationId,)
                    Toast.makeText(applicationContext, "idk", Toast.LENGTH_SHORT).show()
                  val  storedVerificationId = verificationId
                    val resendToken = token
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                    signInWithPhoneAuthCredential(credential)
                    Toast.makeText(applicationContext, "idfc", Toast.LENGTH_SHORT).show()

                }

            }
//    binding.mobileSendOtp.text="sent"
            if (phone.trim().isNotEmpty()) {
                if (phone.trim().length == 10) {

                    val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91" +phone) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)


//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        "+91$phone",
//                        60,
//                        TimeUnit.SECONDS,
//                        this,
//                        object : OnVerificationStateChangedCallbacks() {
//                            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                                binding.mobileSendOtp.text = "sent"
//                            }
//
//                            override fun onVerificationFailed(p0: FirebaseException) {
//                                Toast.makeText(
//                                    applicationContext,
//                                    " verification failed",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//
//                            override fun onCodeSent(
//                                backendOtp: String,
//                                p1: PhoneAuthProvider.ForceResendingToken
//                            ) {
//
//                            }
//                        })
                } else {
                    Toast.makeText(
                        applicationContext,
                        "please enter a 10 digit number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(applicationContext, "enter mobile NUmber", Toast.LENGTH_SHORT).show()
            }


//        des()
            binding.registerButton.setOnClickListener {

                val fullname = binding.fullName.text.toString()
                val designation = binding.designationSpinner.selectedItem.toString()
                val bloodgroup = binding.spinnerBloodGroup.selectedItem.toString()
                val branch = binding.spinnerBranch.selectedItem.toString()
                val year = binding.yearSpinner.selectedItem.toString()
                val phone = binding.editTextPhone.text.toString()
                val email = binding.editTextTextEmailAddress.text.toString()
                val libraryId = binding.libraryId.text.toString()
                val gender = binding.genderSpinner.selectedItem.toString()
                val altmobile = binding.alternateNumber.text.toString()


                //            database = FirebaseDatabase.getInstance().getReference("DonorsV2")
//            val donor = Donor(fullname, bloodgroup, phone, email, branch)
//            binding.yearSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//                  if (position==0){
//                      year = "Faculty"
//                }
//                }
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                    year = "Faculty"
//                }
//            }


                //new DB
                database = FirebaseDatabase.getInstance().getReference("BloodDonors")
                val donor = Donor(
                    fullname,
                    bloodgroup,
                    phone,
                    email,
                    branch,
                    libraryId,
                    designation,
                    year,
                    gender,
                    altmobile
                )


                database.child(bloodgroup).child(libraryId).setValue(donor).addOnSuccessListener {

                    binding.fullName.text.clear()
                    binding.editTextPhone.text.clear()
                    binding.editTextTextEmailAddress.text.clear()
                    binding.libraryId.text.clear()
                    binding.spinnerBranch.setSelection(0)
                    binding.spinnerBloodGroup.setSelection(0)
                    binding.yearSpinner.setSelection(0)
                    binding.designationSpinner.setSelection(0)
                    binding.genderSpinner.setSelection(0)
                    binding.alternateNumber.text.clear()
//                finish()
                    Toast.makeText(this, "Successfully Registed", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {

                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
//    val credential = PhoneAuthProvider.getCredential(verificationId!!, binding.otpTextfield.text.toString())
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        FirebaseAuth.getInstance().signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                } else {
//                }
//            }
//    }
//}


//
//private fun <TResult> Task<TResult>.addOnCompleteListener(
//    onCompleteListener: OnCompleteListener<TResult>,
//    function: () -> Unit
//) {
//    TODO("Not yet implemented")
//}
