package com.example.uddeshhyabloodportal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.Requests
import com.example.uddeshhyabloodportal.models.bloodRequest

class activeReqAdapter(private val requests: ArrayList<bloodRequest?>) :
    RecyclerView.Adapter<activeReqAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val newRequest =
            LayoutInflater.from(parent.context).inflate(R.layout.requestlayout, parent, false)
        return MyViewHolder(newRequest)

    }

    override fun onBindViewHolder(myholder: MyViewHolder, position: Int) {
        val currentReq = requests[position]
        myholder.requirement.text = currentReq?.requirement
        myholder.contName.text = currentReq?.contactPersonName
        myholder.contNum.text = currentReq?.contactPersonMobile
        myholder.patientage.text = currentReq?.patientAge
        myholder.hospital.text = currentReq?.hospitalName
        myholder.otherINFO.text = currentReq?.otherInfo
//        Log.d("check7",requests.size.toString())
//        Log.d("check7",currentReq.toString())

    }


    override fun getItemCount(): Int {
        return requests.size
    }

    class MyViewHolder(reqView: View) : RecyclerView.ViewHolder(reqView) {
        val contName: TextView = reqView.findViewById(R.id.maangnewala)
        val contNum: TextView = reqView.findViewById(R.id.maangnewalanumber)
        val requirement: TextView = reqView.findViewById(R.id.req)
        val patientage: TextView = reqView.findViewById(R.id.patAge)
        val hospital: TextView = reqView.findViewById(R.id.hospName)
        val otherINFO: TextView = reqView.findViewById(R.id.otherTV)
    }

}