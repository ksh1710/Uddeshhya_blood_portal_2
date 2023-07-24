package com.example.uddeshhyabloodportal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ContentInfoCompat.Flags
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.Requests
import com.example.uddeshhyabloodportal.models.bloodRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.collection.LLRBNode.Color
import kotlinx.coroutines.NonDisposableHandle.parent

class activeReqAdapter(private val requests: ArrayList<bloodRequest?>) :

    RecyclerView.Adapter<activeReqAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val newRequest =
            LayoutInflater.from(parent.context).inflate(R.layout.requestlayout, parent, false)
        return MyViewHolder(newRequest)

    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(myholder: MyViewHolder, position: Int) {
        val currentReq = requests[position]
        myholder.requirement.text = currentReq?.requirement
        myholder.contName.text = currentReq?.contactPersonName
        myholder.contNum.text = currentReq?.contactPersonMobile
        myholder.patientage.text = currentReq?.patientAge
        myholder.hospital.text = currentReq?.hospitalName
        myholder.otherINFO.text = currentReq?.otherInfo
        Glide.with(myholder.itemView.context).load(currentReq?.attachments)
            .into(myholder.fetchedImg)
        myholder.reqStatus.text= currentReq?.status

        if (myholder.reqStatus.text=="Fulfilled"){
            myholder.reqStatus.setTextColor(ContextCompat.getColor(myholder.reqStatus.context,R.color.green))
        }else if (myholder.reqStatus.text=="Pending"){
            myholder.reqStatus.setTextColor(ContextCompat.getColor(myholder.reqStatus.context,R.color.red))

        }

        myholder.reqStatusButton.setOnClickListener {
            if (myholder.reqStatus.text=="Fulfilled"){
                val newStatus:String = "Pending"
                val dbdef = FirebaseDatabase.getInstance().getReference("Active Blood Requests")
                    .child(currentReq?.requirement.toString())
                    .child(currentReq?.contactPersonMobile.toString())
                val updates: MutableMap<String, Any> = HashMap()
                updates["status"] = newStatus
                dbdef.updateChildren(updates)
            }else if(myholder.reqStatus.text=="Pending"){
                val newStatus:String = "Fulfilled"
                val dbdef = FirebaseDatabase.getInstance().getReference("Active Blood Requests")
                    .child(currentReq?.requirement.toString())
                    .child(currentReq?.contactPersonMobile.toString())
                val updates: MutableMap<String, Any> = HashMap()
                updates["status"] = newStatus
                dbdef.updateChildren(updates)

            }
        }


        myholder.callReqBtn.setOnClickListener {
            val intent: Intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:+91" + myholder.contNum.text.toString().trim())
            )
            myholder.callReqBtn.context.startActivity(intent)
        }


        myholder.fetchedImg.setOnClickListener {
            val intent = Intent(myholder.itemView.context, memberPreviewImage::class.java)
            intent.putExtra("fbimg", currentReq?.attachments)
            myholder.fetchedImg.context.startActivity(intent)

        }

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
        val callReqBtn: ImageView = reqView.findViewById(R.id.callReqBtn)
        val fetchedImg: ImageView = reqView.findViewById(R.id.fetchedImg)
        val otherINFO: TextView = reqView.findViewById(R.id.otherTV)
        val reqStatus: TextView = reqView.findViewById(R.id.requestStatus)
        val reqStatusButton: Button = reqView.findViewById(R.id.requestStatusButton)

    }

}