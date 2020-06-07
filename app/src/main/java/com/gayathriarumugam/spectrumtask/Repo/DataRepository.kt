package com.gayathriarumugam.spectrumtask.Repo

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gayathriarumugam.spectrumtask.Data.Database.DaoClass
import com.gayathriarumugam.spectrumtask.Data.Model.Company
import com.gayathriarumugam.spectrumtask.Network.API
import com.gayathriarumugam.spectrumtask.Utils.Constant
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataRepository() {
    val constant = Constant()
    private var repository: RoomRepository? = null
    var companies: List<Company>? = null
    var listOfCompanyies = MutableLiveData<List<Company>>()

    //Builds and create Retrofit
    private fun retrofit(): API {
        return Retrofit.Builder()
            .baseUrl(constant.base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }

    //Makes a network call in a thread and suspend it once it completed
    fun getNetworkResponse(application: Application, daoObject: DaoClass, scope: CoroutineScope): MutableLiveData<List<Company>>? {
        retrofit().getCompanys().enqueue(object : retrofit2.Callback<List<Company>> {
            override fun onResponse(call: Call<List<Company>>, response: Response<List<Company>>) {
                if (response.body() != null) {
                    companies = response.body()
                    listOfCompanyies.postValue(companies)
                }
            }

            override fun onFailure(call: Call<List<Company>>, t: Throwable) {
                Log.e("Failure: ", t.localizedMessage)
            }
        })
        return listOfCompanyies
    }
}