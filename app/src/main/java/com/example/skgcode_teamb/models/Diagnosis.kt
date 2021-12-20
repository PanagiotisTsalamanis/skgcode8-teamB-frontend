package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class Diagnosis(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("doctor")
    val doctor: Doctor,
    @SerializedName("date")
    val date: String,
    @SerializedName("examination")
    val examination: String,
    @SerializedName("results")
    val results: String,
    @SerializedName("diagnosis")
    val diagnosis: String,
    @SerializedName("__v")
    val __v: Int
)