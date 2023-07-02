package com.example.uddeshhyabloodportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.databinding.ActivityMembersListBinding
import com.example.uddeshhyabloodportal.models.memberInfo

class membersList : AppCompatActivity() {

    lateinit var memberRecyclerView: RecyclerView
    lateinit var binding: ActivityMembersListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMembersListBinding.inflate(layoutInflater)
        setContentView(binding.root)
val members= mutableListOf<memberInfo>()
        members.add(memberInfo("Kshitiz Agarwal","CSE","1st","7017946250"))
        members.add(memberInfo("lokendra Sir","pta nhi","3rd","7618564923"))
        members.add(memberInfo("Maahi Mam","nhi pta","2nd","7417389192"))
        members.add(memberInfo("Extra Agarwal","IDK","IDK","420420420"))



         memberRecyclerView = binding.memberList
        memberRecyclerView.adapter=memberToContactAdapter(members)
        memberRecyclerView.layoutManager=LinearLayoutManager(this)
    }
}