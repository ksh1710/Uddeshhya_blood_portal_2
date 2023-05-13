package com.example.uddeshhyabloodportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
//import androidx.lifecycle.lifecycleScope
import com.example.uddeshhyabloodportal.databinding.ActivitySearchDonorBinding
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.Donors
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlin.math.log


class searchDonor : AppCompatActivity() {
    lateinit var binding: ActivitySearchDonorBinding
    lateinit var database: DatabaseReference
    var donors : ArrayList<Donor?>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySearchDonorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBtn.setOnClickListener {
            val bloodgrouptext:String = binding.searchText.text.toString()
            if (bloodgrouptext.isNotEmpty()){
                //lifecycleScope.launch(Dispatchers.Default) {
                        readData(bloodgrouptext)
               // }

            }else{
                Toast.makeText(this,"enter value",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private /*suspend*/ fun readData(bloodgrouptext: String) {
//        database=FirebaseDatabase.getInstance().getReference("DonorsV2")
        database=FirebaseDatabase.getInstance().getReference("BloodDonors")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(bloodgrouptext))
                {
                    for(i in snapshot.child(bloodgrouptext).children)
                    {
                        val donor = i.getValue(Donor::class.java)
                        Toast.makeText(this@searchDonor, "Successfully Read", Toast.LENGTH_LONG).show()
                        donors?.add(donor)
                    }
//                    Log.d("hello", donors.toString())

                    val  donorList = Donors(donors)
                    val json = Gson().toJson(donorList).toString()
                    val intent = Intent(this@searchDonor, SearchResults::class.java)
                    intent.putExtra("json",json)
                    startActivity(intent)

                }
                else
                {
                    Toast.makeText(this@searchDonor, "No Users Exist", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })




        /*database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children)
                {
                    val donor  = i.getValue(Donor::class.java)
                    if(donor?.bloodGroup.equals(bloodgrouptext))
                    {
                        Toast.makeText(this@searchDonor,"successfully Read",Toast.LENGTH_SHORT).show()
                        donors?.add(donor)
                    }
                }



                val  donorList = Donors(donors)
                val i = Intent(this@searchDonor, SearchResults::class.java)
                i.putExtra("json",Gson().toJson(donorList).toString())
                startActivity(i)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })*/

    }
}