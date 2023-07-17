package com.example.uddeshhyabloodportal

//import android.graphics.BitmapFactory

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.uddeshhyabloodportal.databinding.ActivityBloodRequestWrittenBinding
import com.example.uddeshhyabloodportal.models.bloodRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.UploadTask.TaskSnapshot
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.awaitAll
import java.io.ByteArrayOutputStream


class bloodRequestWritten : AppCompatActivity() {

    private lateinit var binding: ActivityBloodRequestWrittenBinding
    lateinit var database: DatabaseReference
     var  storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("Attachments")
    var attachment: String? = ""

    //    private var imageUri: Uri? = null
//
//    private val selectImg = registerForActivityResult(ActivityResultContracts.GetContent()) {
//        imageUri = it
//        binding.attachmentPreview.isVisible = true
//        binding.attachmentPreview.setImageURI(imageUri)
//    }
//
//
//    val imagelauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                if (it.data != null) {
//                    val storageref = Firebase.storage.reference.child("/attachments/")
//                    storageref.putFile(it.data!!.data!!).addOnSuccessListener {
//                        val result = it.metadata?.reference?.downloadUrl
//                        attachment = result.toString()
//                        Log.d("idgaf", attachment.toString())
//                    }
//
//                }
//            }
//        }
    private var imgUri: Uri? = null
    private val resLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imgUri = it
        binding.attachmentPreview.isVisible= true
        binding.attachmentPreview.setImageURI(imgUri)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodRequestWrittenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.attachmentsBtn.setOnClickListener {
            resLauncher.launch("image/*")
        }
//    binding.attachmentPreview.setOnClickListener {
//        val resID = resources.getIdentifier(imageUri.toString(), "drawable", packageName)
//
//        val bmp = BitmapFactory.decodeResource(resources, resID)
//        val stream = ByteArrayOutputStream()
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val byteArray = stream.toByteArray()
//
//        val intent = Intent(this, imagePreview::class.java)
//        intent.putExtra("picture", byteArray)
//        startActivity(intent)
//            val intent = Intent(this,imagePreview::class.java)
//            intent.putExtra("previewImg",imageUri)
//            startActivity(intent)
//    }

//        binding.attachmentsBtn.setOnClickListener {
//            val intent = Intent()
//            intent.action = Intent.ACTION_PICK
//            intent.type = "image/*"
//            imagelauncher.launch(intent)
////            selectImg.launch("image/*")
////            Toast.makeText(this, "image gaiiiiiiii", Toast.LENGTH_SHORT).show()
//        }


        binding.submitBtn.setOnClickListener {
            val requirements = binding.requirement.text.toString()
            val patAge = binding.patientAge.text.toString()
            val hospital = binding.HospitalName.text.toString()
            val person = binding.contactPerson.text.toString()
            val number = binding.userPhoneNumber.text.toString()
            val others = binding.otherInfo.text.toString()
            uploadImg()
//    val contactnumber = binding.phonenumber.text.toString()

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
                                attachment
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
                                    Toast.makeText(
                                        this@bloodRequestWritten,
                                        "Successfully Registered",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
//                                            flag = false
                                }.addOnFailureListener {
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

    }

    private fun uploadImg() {
        storageReference = storageReference.child(System.currentTimeMillis().toString())
        imgUri?.let {
            storageReference.putFile(it).addOnSuccessListener{
//                if (task.isSuccessful){
//                    storageReference.downloadUrl.addOnSuccessListener {
                        val map = HashMap<String, Any>()
                        map["pic"]=imgUri.toString()

                     val x=    it.metadata?.reference?.downloadUrl.toString()

                Log.d("fk",x)
//                            FirebaseStorage.getInstance().getReference("Attachments/ ").downloadUrl.addOnCompleteListener{ it ->
//                                val profileImageUrl =it.result.toString();
//                                Log.d("URL",profileImageUrl);
//                            }





//                 val path =storageReference.downloadUrl.toString()
//                 val path = getFileName(applicationContext,imgUri!!)
                //                val path = it.task.snapshot.metadata?.reference?.downloadUrl.toString()
//                Toast.makeText(this, path, Toast.LENGTH_SHORT).show()
//                val pt = imgUri!!.path
//                if (pt != null) {
//                    Log.d("img",pt)
//                }

//                    Log.d("img",path!!)

//                Log.d("img",imgUri.toString())

//                        attachment = path

//                    }
//                }else{
//                    Toast.makeText(this, "image error", Toast.LENGTH_SHORT).show()
//                }

            }
        }
    }

//    @SuppressLint("Range")
//    fun getFileName(context: Context, uri: Uri): String? {
//        if (uri.scheme == "content") {
//            val cursor = context.contentResolver.query(uri, null, null, null, null)
//            cursor.use {
//                if (cursor != null) {
//                    if(cursor.moveToFirst()) {
//                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                    }
//                }
//            }
//        }
//        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
//    }
}

