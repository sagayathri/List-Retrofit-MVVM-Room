package com.gayathriarumugam.spectrumtask.Data.Database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company")
data class CompanyEntity(
    @PrimaryKey
    @NonNull
    val id: String,
    val companyName: String?,
    val website: String?,
    val logo: String?,
    val about: String?,
    var isFav: Int,
    var isFollowing: Int
)

@Entity(tableName = "member")
data class MemberEntity(
    @PrimaryKey
    val memberID: String,
    val companyID: String?,
    val age: Long?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phone: String?,
    var isFav: Int
)