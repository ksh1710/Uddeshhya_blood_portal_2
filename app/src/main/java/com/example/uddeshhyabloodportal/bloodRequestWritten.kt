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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodRequestWrittenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val reqArray = resources.getStringArray(R.array.requirementArray)
        val reqAdapter = ArrayAdapter(this, R.layout.spinner_item, reqArray)
        binding.requirement.setAdapter(reqAdapter)


        binding.attachmentPreview.setOnClickListener {

            val selImgUri = imgUri!!
//            val pt = binding.attachmentPreview.drawable
//            intent.putExtra("im",\)
//            binding.attachmentPreview.isDrawingCacheEnabled = true
//            val b: Bitmap = binding.attachmentPreview.getDrawingCache()
            val i = Intent(this, imagePreview::class.java)
            Log.d("finalfksucc", selImgUri.toString())
//            i.putExtra("Bitmap", b)
            i.setData(selImgUri)
            startActivity(i)
//val intent = Intent(this,imagePreview::class.java)
//            intent.putExtra("image",binding.attachmentPreview.imageAlpha)
//            startActivity(intent)
        }

        binding.attachmentsBtn.setOnClickListener {
            resLauncher.launch("image/*")
        }


        binding.submitBtn.setOnClickListener {
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
            loading.dialogRunning()
            uploadImg()
        }
    }

    private fun uploadImg() {

        val storageRef = FirebaseStorage.getInstance().getReference("Report_Attachments")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(imgName.toString())


        storageRef.putFile(imgUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                storeDate(it)
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
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
                                loading.dialogRunning()
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

