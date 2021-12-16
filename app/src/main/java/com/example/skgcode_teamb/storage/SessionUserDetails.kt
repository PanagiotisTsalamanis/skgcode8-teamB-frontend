package com.example.skgcode_teamb.storage

import com.google.gson.annotations.SerializedName

data class SessionUserDetails (
    val healthIdNumber: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val birthDate: String?,
    val bloodType: String?,
    val familyDoctorId: String?,
    val familyDoctorName: String?
)