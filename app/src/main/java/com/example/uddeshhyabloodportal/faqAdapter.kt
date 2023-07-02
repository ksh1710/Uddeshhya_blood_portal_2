package com.example.uddeshhyabloodportal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.models.faqList

class faqAdapter(private val faqList: List<faqList?>) :
    RecyclerView.Adapter<faqAdapter.myViewHolder>() {


    class myViewHolder(userView: View) : RecyclerView.ViewHolder(userView) {
        val question: TextView = userView.findViewById(R.id.question)
        val answer: TextView = userView.findViewById(R.id.answer)
        val imgDown: ImageView = userView.findViewById(R.id.imgUPDOWN)
    }


    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentFaq = faqList[position]
        holder.question.text = currentFaq?.question
        holder.answer.text = currentFaq?.answer
        var isexpandable: Boolean = currentFaq!!.isExpandable
        holder.imgDown.rotation=180F
        holder.answer.visibility = if (isexpandable) {
            View.VISIBLE
        } else {
            holder.imgDown.rotation=0F
            View.GONE
        }

        holder.imgDown.setOnClickListener {
            isItemXpanded(position)
            currentFaq.isExpandable = !currentFaq.isExpandable
            notifyItemChanged(position)
        }
    }

    private fun isItemXpanded(position: Int) {
        val temp = faqList.indexOfFirst {
            it!!.isExpandable
        }
        if (temp >= 0 && temp != position) {
            faqList[temp]!!.isExpandable = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val faqitem =
            LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        return myViewHolder(faqitem)
    }


    override fun getItemCount(): Int {
        return faqList.size
    }

}