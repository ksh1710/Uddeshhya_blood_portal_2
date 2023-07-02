package com.example.uddeshhyabloodportal

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.models.memberInfo

class memberToContactAdapter(private val memberInfo: List<memberInfo?>) :
    RecyclerView.Adapter<memberToContactAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val memberinfo = LayoutInflater.from(parent.context).inflate(R.layout.activity_uddeshya_member_to_contact, parent, false)
        return myViewHolder(memberinfo)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentItem = memberInfo[position]
        holder.name.text=currentItem?.name
        holder.branch.text=currentItem?.branch
        holder.year.text=currentItem?.year
        holder.contact.text=currentItem?.contact


        holder.memberCallBtn.setOnClickListener {
            val intent: Intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:+91" + holder.contact.text.toString().trim())
            )
            holder.memberCallBtn.context.startActivity(intent)
        }
//        holder.callImgBtn.setOnClickListener {
//            val intent: Intent = Intent(
//                Intent.ACTION_DIAL,
//                Uri.parse("tel:+91" + holder.contact.text.toString().trim())
//            )
//            holder.callImgBtn.context.startActivity(intent)
//        }
//
//        holder.memberCallBtn.setOnClickListener {
//            val intent= Intent(  Intent.ACTION_DIAL,
//                Uri.parse("tel:+91" + holder.contact.text))
//       holder.memberCallBtn.context.startActivity(intent)
//        }
//        holder.callImg.setOnClickListener {
//            val intent:Intent= Intent(  Intent.ACTION_DIAL,
//                Uri.parse("tel:+91" + holder.contact.text.toString().trim()))
//            holder.memberCallBtn.context.startActivity(intent)
//
//        }
    }


    override fun getItemCount(): Int {
//        Log.d("idk",memberInfo.size.toString())
        return memberInfo.size
    }


    class myViewHolder(memberView: View) : RecyclerView.ViewHolder(memberView) {
        val name: TextView = memberView.findViewById(R.id.memberName)
        val branch: TextView = memberView.findViewById(R.id.memberBranch)
        val year: TextView = memberView.findViewById(R.id.memberYear)
        val contact: TextView = memberView.findViewById(R.id.memberContact)
        val memberCallBtn:TextView=memberView.findViewById(R.id.callBtn)
//        val callImgBtn:ImageView=memberView.findViewById(R.id.callImgBtn)
    }
}