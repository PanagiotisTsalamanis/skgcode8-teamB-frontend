package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class Doctor(
    @SerializedName("id")
    val id: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String
)