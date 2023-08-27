package com.example.uddeshhyabloodportal

//import android.graphics.BitmapFactory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.uddeshhyabloodportal.databinding.ActivityBloodRequestWrittenBinding
import com.example.uddeshhyabloodportal.databinding.LoadingDialogBinding
import com.example.uddeshhyabloodportal.models.bloodRequest
import com.example.uddeshhyabloodportal.models.customDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class bloodRequestWritten : AppCompatActivity() {

    private var imgName: String? = null
    private lateinit var binding: ActivityBloodRequestWrittenBinding
    lateinit var database: DatabaseReference
    lateinit var createBloodReqformAdView: AdView
    private var imgUri: Uri? = null
    val loading = customDialog(this)


    private val resLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imgUri = it
        binding.attachmentPreview.isVisible = true
        binding.attachmentPreview.setImageURI(imgUri)
        imgName = getFileName(applicationContext, it!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodRequestWrittenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        createBloodReqformAdView = findViewById(R.id.createBloodReqformAD)
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        createBloodReqformAdView.loadAd(adRequest)

        createBloodReqformAdView.adListener = object: AdListener() {
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
                createBloodReqformAdView .loadAd(adRequest)
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



        val reqArray = resources.getStringArray(R.array.requirementArray)
        val reqAdapter = ArrayAdapter(this, R.layout.spinner_item, reqArray)
        binding.requirement.setAdapter(reqAdapter)


        binding.attachmentPreview.setOnClickListener {

            val selImgUri = imgUri!!
         val i = Intent(this, imagePreview::class.java)
            Log.d("finalfksucc", selImgUri.toString())
            i.data=selImgUri
            startActivity(i)

        }

        binding.attachmentsBtn.setOnClickListener {
            resLauncher.launch("image/*")
        }


        binding.submitBtn.setOnClickListener {
            Log.d("idk","smthng")
            validateData()
        }

    }

    private fun validateData() {
        if (binding.requirement.text.toString().isBlank()
            || binding.patientAge.text.toString().isBlank()
            || binding.HospitalName.text.toString().isBlank()
            || binding.contactPerson.text.toString().isBlank()
            || binding.userPhoneNumber.text.toString().isBlank()
            || imgUri == null
        ) {
            Toast.makeText(this, "please enter all fields", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("idk","smthng 2")
            loading.dialogRunning()
            uploadImg()
        }
    }

    private fun uploadImg() {
        Log.d("idk","kuch toh")
        val storageRef = FirebaseStorage.getInstance().getReference("Report_Attachments")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(imgName.toString())
        storageRef.putFile(imgUri!!).addOnSuccessListener {
            Log.d("idk",it.toString())
            storageRef.downloadUrl.addOnSuccessListener {
                Log.d("idk",it.toString())
                storeDate(it)
            }.addOnFailureListener {
                Log.d("idk",it.message.toString())
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Log.d("idk",it.message.toString())
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun storeDate(imgUrl: Uri?) {
        val requirements = binding.requirement.text.toString()
        val patAge = binding.patientAge.text.trim().toString()
        val hospital = binding.HospitalName.text.trim().toString()
        val person = binding.contactPerson.text.trim().toString()
        val number = binding.userPhoneNumber.text.trim().toString()
        val others = binding.otherInfo.text.trim().toString()
        val image = imgUrl.toString()

        FirebaseDatabase.getInstance().getReference("Active Blood Requests")
            .child(requirements)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        database =
                            FirebaseDatabase.getInstance()
                                .getReference("Active Blood Requests")
                        val request = bloodRequest(
                            requirements,
                            patAge,
                            hospital,
                            person,
                            number,
                            others,
                            image
                        )
                        database.child(requirements).child(number)
                            .setValue(request)
                            .addOnSuccessListener {
                                binding.requirement.text.clear()
                                binding.patientAge.text.clear()
                                binding.HospitalName.text.clear()
                                binding.contactPerson.text.clear()
                                binding.userPhoneNumber.text.clear()
                                binding.otherInfo.text.clear()
                                loading.dialogClose()
                                finish()
                                Toast.makeText(
                                    this@bloodRequestWritten,
                                    "Successfully Registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {
                                Log.d("idk",it.message.toString())
                                loading.dialogClose()
                                Toast.makeText(
                                    this@bloodRequestWritten,
                                    "error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                }
            )
    }


    @SuppressLint("Range")
    fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
}

