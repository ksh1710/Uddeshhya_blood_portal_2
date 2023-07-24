package com.example.uddeshhyabloodportal

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.models.Donor
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
        holder.city.text = currentItem?.city
        holder.lastDonatedTV.text = currentItem?.lastDonatedOn
        if (currentItem?.lastDonatedOn == "") {
            holder.lastDonatedTV.text = "Not Donated Yet"
        }



        holder.setDateBtn.setOnClickListener {

            val myCalender = Calendar.getInstance()
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myCalender.set(Calendar.YEAR, year)
                myCalender.set(Calendar.MONTH, month)
                myCalender.set(Calendar.DAY_OF_MONTH, day)
                val myFormat = "dd-MM-yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.UK)
                val lastDonatedOn = sdf.format(myCalender.time)
                val dbdef = FirebaseDatabase.getInstance().getReference("BloodDonors")
                    .child(currentItem?.bloodGroup.toString())
                    .child(currentItem?.mobileNo.toString())
                val updates: MutableMap<String, Any> = HashMap()
                updates["lastDonatedOn"] = lastDonatedOn
                dbdef.updateChildren(updates)
            }
            DatePickerDialog(
                it.context,
                datePicker,
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }





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
        val lastDonatedTV: TextView = donorView.findViewById(R.id.lastDonatedTv)
        val setDateBtn: Button = donorView.findViewById(R.id.setDateBtn)


    }

}