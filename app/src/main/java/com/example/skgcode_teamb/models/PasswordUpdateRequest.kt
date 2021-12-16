package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class PasswordUpdateRequest(
    @SerializedName("password")
    val password: String
)