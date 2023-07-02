package com.example.uddeshhyabloodportal

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.uddeshhyabloodportal.databinding.ActivitySearchDonor2Binding
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.Donors
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class searchDonor : AppCompatActivity() {
    lateinit var binding: ActivitySearchDonor2Binding
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDonor2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val signOutBtn = binding.signOutTV

        signOutBtn.paintFlags = Paint.UNDERLINE_TEXT_FLAG
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
//        val isLogin = sharedPref.getString("Email", "1")
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

        binding.searchBtn.setOnClickListener {
            val bloodgrouptext: String = binding.bloodGrpSpinner.text.toString()
            if (bloodgrouptext.isNotEmpty()) {
                //lifecycleScope.launch(Dispatchers.Default) {
                readData(bloodgrouptext)
                // }

            } else {
                Toast.makeText(this, "enter value", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readData(bloodgrouptext: String) {
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
//                    Log.d("hello", donors.toString())

                    val donorList = Donors(donors)
                    val json = Gson().toJson(donorList).toString()
                    val intent = Intent(this@searchDonor, SearchResults::class.java)
                    intent.putExtra("json", json)
                    startActivity(intent)

                } else {
                    Toast.makeText(this@searchDonor, "No Users Exist", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}