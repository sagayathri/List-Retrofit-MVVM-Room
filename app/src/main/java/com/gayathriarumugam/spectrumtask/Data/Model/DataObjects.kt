package com.gayathriarumugam.spectrumtask.Data.Model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Company (
    @SerializedName("_id")
    val id: String,
    val company: String,
    val website: String,
    val logo: String,
    val about: String,
    val members: List<Member>
)

@JsonClass(generateAdapter = true)
data class Member (
    @SerializedName("_id")
    val id: String,
    val age: Long,
    val name: Name,
    val email: String,
    val phone: String
)

data class Name (
    val first: String,
    val last: String
)