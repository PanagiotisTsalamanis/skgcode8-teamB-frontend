package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class ScheduleAppRequest(
    @SerializedName("hospitalId")
    val hospitalId: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("date")
    val date: String
)