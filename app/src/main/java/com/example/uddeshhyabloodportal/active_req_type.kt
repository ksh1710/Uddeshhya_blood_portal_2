package com.example.uddeshhyabloodportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.uddeshhyabloodportal.databinding.ActivityActiveReqTypeBinding
import com.example.uddeshhyabloodportal.models.Requests
import com.example.uddeshhyabloodportal.models.bloodRequest
import com.example.uddeshhyabloodportal.models.customDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class active_req_type : AppCompatActivity() {
    private lateinit var binding: ActivityActiveReqTypeBinding
    lateinit var database: DatabaseReference
    val loading = customDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveReqTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val reqArray = resources.getStringArray(R.array.requirementArray)
        val reqTyoeAdapter = ArrayAdapter(this, R.layout.spinner_item, reqArray)
        binding.reqType.setAdapter(reqTyoeAdapter)


        binding.reqWalaBtn.setOnClickListener {
            val bloodgrouptext: String = binding.reqType.text.toString()
            if (bloodgrouptext.isNotEmpty()) {
                loading.dialogRunning()
                readActiveRequests(bloodgrouptext)
            } else {
                Toast.makeText(this, "enter value", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readActiveRequests(bloodgrouptext: String) {
        database = FirebaseDatabase.getInstance().getReference("Active Blood Requests")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(bloodgrouptext)){
                    var active: ArrayList<bloodRequest?>? = ArrayList()

                    for (i in snapshot.child(bloodgrouptext).children){
                        val request = i.getValue(bloodRequest::class.java)
                        active?.add(request)
                    }
                    val requestlist = Requests(active)
                    val jsonReq = Gson().toJson(requestlist).toString()
                    val intent2 = Intent(this@active_req_type, ActiveRequests::class.java)
                    intent2.putExtra("jsonReq", jsonReq)
                    startActivity(intent2)
                    loading.dialogClose()
                }else {
                    Toast.makeText(this@active_req_type, "No Reqeusts avaiable", Toast.LENGTH_LONG).show()
                    loading.dialogClose()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(parent, "error in db bc", Toast.LENGTH_SHORT).show()
            }
        })

    }
}