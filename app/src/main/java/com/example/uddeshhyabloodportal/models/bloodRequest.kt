package com.example.uddeshhyabloodportal.models



data class bloodRequest(
    val requirement: String? = "",
    val patientAge: String? = "",
    val hospitalName: String? = "",
    val contactPersonName: String? = "",
    val contactPersonMobile: String? = "",
    val otherInfo: String? = "",
    val attachments:String?="",
    val status:String? = "Pending"
)
