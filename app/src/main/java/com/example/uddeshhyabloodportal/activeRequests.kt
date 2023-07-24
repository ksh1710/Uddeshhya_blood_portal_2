package com.example.uddeshhyabloodportal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.databinding.ActivityActiveRequestsBinding
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.bloodRequest
import com.example.uddeshhyabloodportal.models.Requests
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

class ActiveRequests : AppCompatActivity() {
     lateinit var binding: ActivityActiveRequestsBinding

    private lateinit var reqRecyclerView: RecyclerView
    private lateinit var reqArraylist: ArrayList<bloodRequest?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonreq =this.intent.getStringExtra("jsonReq")
        val reqList = Gson().fromJson(jsonreq, Requests::class.java)
//        Log.d("check7",reqList.toString())
        reqArraylist = reqList.listOfRequests!!
//        Log.d("check7",reqArraylist.toString())
        reqRecyclerView = binding.reqRV
        reqRecyclerView.layoutManager = LinearLayoutManager(this)
        reqRecyclerView.adapter = activeReqAdapter(reqArraylist)
    }


    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        reqArraylist.clear()
        reqRecyclerView.adapter?.notifyDataSetChanged()
    }
}