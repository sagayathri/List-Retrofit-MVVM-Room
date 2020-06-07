package com.gayathriarumugam.spectrumtask.Network

import com.gayathriarumugam.spectrumtask.Data.Model.Company
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("get/Vk-LhK44U")
    fun getCompanys(): Call<List<Company>>
}