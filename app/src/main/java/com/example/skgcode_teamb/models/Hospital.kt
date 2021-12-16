package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class Hospital(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)