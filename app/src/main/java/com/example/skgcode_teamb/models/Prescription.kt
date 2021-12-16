package com.example.skgcode_teamb.models


import com.google.gson.annotations.SerializedName

data class Prescription(
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("__v")
    val v: Int,
    @SerializedName("doctor")
    val doctor: Doctor,
    @SerializedName("drugCode")
    val drugCode: String
)