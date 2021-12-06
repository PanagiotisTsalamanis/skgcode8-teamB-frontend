package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User
)