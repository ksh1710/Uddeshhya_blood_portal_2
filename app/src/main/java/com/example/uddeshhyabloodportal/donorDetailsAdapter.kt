package com.example.uddeshhyabloodportal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.models.Donors

class donorDetailsAdapter(private val donorDetails: ArrayList<Donors?>) :RecyclerView.Adapter<donorDetailsAdapter.myViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val donorDetail = LayoutInflater.from(parent.context).inflate(R.layout.donor_details,parent,false)
        return myViewHolder(donorDetail)

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
            val currentItem = donorDetails[position]
            holder.name.text = currentItem?.listOfDonors?.get(position)?.fullName
            holder.mobileNo.text = currentItem?.listOfDonors?.get(position)?.mobileNo
            holder.email.text = currentItem?.listOfDonors?.get(position)?.email
    }
//    ?.listOfDonors?.get(position)?
    override fun getItemCount(): Int {
    return donorDetails.size
}


    class myViewHolder(donorView:View):RecyclerView.ViewHolder(donorView){
        val name : TextView=donorView.findViewById(R.id.nameTv)
        val mobileNo: TextView=donorView.findViewById(R.id.mobileTv)
        val email: TextView = donorView.findViewById(R.id.emailTv)

    }

}