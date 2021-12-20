package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class ScheduleAppResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("appointment")
    val appointment: Appointments
)