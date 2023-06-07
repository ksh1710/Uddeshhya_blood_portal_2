package com.example.uddeshhyabloodportal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.models.Donor
import com.example.uddeshhyabloodportal.models.Donors
import kotlinx.coroutines.NonDisposableHandle.parent

class donorDetailsAdapter(private val donorDetails: ArrayList<Donor?>) :
    RecyclerView.Adapter<donorDetailsAdapter.myViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val donorDetail =
            LayoutInflater.from(parent.context).inflate(R.layout.donor_details, parent, false)
        return myViewHolder(donorDetail)

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentItem = donorDetails[position]
        holder.name.text = currentItem?.fullName
        holder.mobileNo.text = currentItem?.mobileNo
        holder.email.text = currentItem?.email
        holder.altMobile.text = currentItem?.altmobileNo
        holder.occupation.text = currentItem?.occupation
//        if (currentItem?.year == "") {
//            holder.year.text = "-"
//        } else {
//            holder.year.text = currentItem?.year
//        }
        holder.city.text = currentItem?.city

        holder.callBtn.setOnClickListener {
            val intent: Intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:+91" + holder.mobileNo.text.toString().trim())
            )
            holder.callBtn.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return donorDetails.size
    }


    class myViewHolder(donorView: View) : RecyclerView.ViewHolder(donorView) {
        val name: TextView = donorView.findViewById(R.id.nameTv)
        val mobileNo: TextView = donorView.findViewById(R.id.mobileTv)
        val email: TextView = donorView.findViewById(R.id.emailTv)
        val altMobile: TextView = donorView.findViewById(R.id.alternateTv)
        val occupation: TextView = donorView.findViewById(R.id.occupationTv)
        val city: TextView = donorView.findViewById(R.id.cityTv)
        val callBtn: ImageView = donorView.findViewById(R.id.callBtn)
    }

}