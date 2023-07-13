package com.example.uddeshhyabloodportal

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.uddeshhyabloodportal.databinding.ActivitySearchDonor2Binding
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.Donors
import com.example.uddeshhyabloodportal.models.Requests
import com.example.uddeshhyabloodportal.models.bloodRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.io.Console

class searchDonor : AppCompatActivity() {
    lateinit var binding: ActivitySearchDonor2Binding
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDonor2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val signOutBtn = binding.signOutTV
        signOutBtn.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        val bloodGroupArray = resources.getStringArray(R.array.bloodGroup)
        val bloodgroupAdapter = ArrayAdapter(this, R.layout.spinner_item, bloodGroupArray)
        binding.bloodGrpSpinner.setAdapter(bloodgroupAdapter)

        signOutBtn.setOnClickListener {
            val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()
            finish()
            Toast.makeText(this, "successfully signed out", Toast.LENGTH_SHORT).show()
        }




        binding.activeReqBtn.setOnClickListener {
            val bloodgrouptext: String = binding.bloodGrpSpinner.text.toString()
            if (bloodgrouptext.isNotEmpty()) {
                readActiveRequests(bloodgrouptext)
            } else {
                Toast.makeText(this, "enter value", Toast.LENGTH_SHORT).show()
            }
        }




        binding.searchBtn.setOnClickListener {
            val bloodgrouptext: String = binding.bloodGrpSpinner.text.toString()
            if (bloodgrouptext.isNotEmpty()) {
                readData(bloodgrouptext)
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
//                    var ActiveRequestsNew : ArrayList<bloodRequest?>? = ArrayList()
                    var active: ArrayList<bloodRequest?>? = ArrayList()

                    for (i in snapshot.child(bloodgrouptext).children){
                        val request = i.getValue(bloodRequest::class.java)
                        active?.add(request)
                    }
                    val requestlist = Requests(active)
                    val jsonReq = Gson().toJson(requestlist).toString()
                    Log.d("check6",jsonReq)
                    val intent2 = Intent(this@searchDonor, ActiveRequests::class.java)
                    intent2.putExtra("jsonReq", jsonReq)
                    startActivity(intent2)
                }else {
                    Toast.makeText(this@searchDonor, "No Reqeusts avaiable", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(parent, "error in db bc", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun readData(bloodgrouptext: String) {
//    private fun readData(bloodgrouptext: String) {
//        database=FirebaseDatabase.getInstance().getReference("DonorsV2")
        database = FirebaseDatabase.getInstance().getReference("BloodDonors")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(bloodgrouptext)) {
                    var donors: ArrayList<Donor?>? = ArrayList()
                    for (i in snapshot.child(bloodgrouptext).children) {
                        val donor = i.getValue(Donor::class.java)
                        Toast.makeText(this@searchDonor, "Successfully Read", Toast.LENGTH_LONG)
                            .show()
                        donors?.add(donor)
                    }
                    val donorList = Donors(donors)
                    val json = Gson().toJson(donorList).toString()
                    val intent = Intent(this@searchDonor, SearchResults::class.java)
                    intent.putExtra("json", json)
                    startActivity(intent)

                } else {
                    Toast.makeText(this@searchDonor, "No Donor of this blood group available", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}