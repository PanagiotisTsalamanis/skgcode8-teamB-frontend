package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("healthIdNumber")
    val healthIdNumber: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("birthDate")
    val birthDate: String?,
    @SerializedName("bloodType")
    val bloodType: String?
)