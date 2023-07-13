package com.example.uddeshhyabloodportal

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.databinding.ActivityMainBinding
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.Donors
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.Objects


class SearchResults : AppCompatActivity() {

    private lateinit var donorRecyclerView: RecyclerView
    private lateinit var donorArraylist: ArrayList<Donor?>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainxml: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainxml.root)

        val json = this.intent.getStringExtra("json")
        val donorList = Gson().fromJson(json, Donors::class.java)


        donorArraylist = donorList.listOfDonors!!
//        Log.d("mess2", donorArraylist.toString())

        donorRecyclerView = mainxml.donorListRV
        donorRecyclerView.layoutManager = LinearLayoutManager(this)
        donorRecyclerView.adapter = donorDetailsAdapter(donorArraylist)


    }


    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        donorArraylist.clear()
        donorRecyclerView.adapter?.notifyDataSetChanged()
    }

}
