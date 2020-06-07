package com.gayathriarumugam.spectrumtask.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.gayathriarumugam.spectrumtask.Data.Database.AppRoomDatabase
import com.gayathriarumugam.spectrumtask.Data.Database.CompanyEntity
import com.gayathriarumugam.spectrumtask.Data.Database.DaoClass
import com.gayathriarumugam.spectrumtask.Data.Database.MemberEntity
import com.gayathriarumugam.spectrumtask.Data.Model.Company
import com.gayathriarumugam.spectrumtask.Repo.DataRepository
import com.gayathriarumugam.spectrumtask.Repo.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompanyViewModel(application: Application) : AndroidViewModel(application) {

    internal var application: Application
    val daoObject: DaoClass
    private var repository: RoomRepository? = null
    var allCompanies: LiveData<List<CompanyEntity>>? = null
    var allMembers: LiveData<List<MemberEntity>>? = null
    var sortedCompanyList = MutableLiveData<List<CompanyEntity>>()
    var sortCompanyList: List<CompanyEntity>? = null
    var isMemberSorted  = false

    init {
        this.application = application
        daoObject = AppRoomDatabase.getDatabase(application, viewModelScope).daoObject()
        repository = RoomRepository(daoObject)
    }

    fun fetchCompany(): MutableLiveData<List<Company>>? {
        return DataRepository().getNetworkResponse(application = application, daoObject = daoObject, scope = viewModelScope)
    }

    fun updateDatabase(companies: List<Company>?) = viewModelScope.launch(Dispatchers.IO) {
        companies?.forEach { company ->
            val companyEntity = CompanyEntity(
                id = company.id,
                companyName = company.company,
                website = company.website,
                logo = company.logo,
                about = company.about,
                isFav = 0,
                isFollowing = 0
            )
            repository?.insertCompanies(companyEntity)
            for (member in company.members) {
                val memberEntity = MemberEntity(
                    memberID = member.id,
                    companyID = company.id,
                    age = member.age,
                    firstName = member.name.first,
                    lastName = member.name.last,
                    email = member.email,
                    phone = member.phone,
                    isFav = 0
                )
                repository?.insertMember(memberEntity)
            }
        }
    }

    fun getAllCompanys(): LiveData<List<CompanyEntity>>? {
        allCompanies = repository?.getAllCompanies()
        return allCompanies
    }

    fun getAllMembers(companyID: String): LiveData<List<MemberEntity>>? {
        allMembers = repository?.getAllMembers(companyID = companyID)
        return allMembers
    }

    fun updateCompany(companyEntity: CompanyEntity) = viewModelScope.launch(Dispatchers.Default) {
        repository?.updateCompany(companyEntity)
    }

    fun updateMember(memberEntity: MemberEntity) = viewModelScope.launch(Dispatchers.Default) {
        repository?.updateMember(memberEntity)
    }

    fun sortCompanyByNameAsend() {
        viewModelScope.launch(Dispatchers.IO) {
            sortCompanyList = repository?.sortCompanyByNameAsend()!!
            sortedCompanyList.postValue(sortCompanyList)
        }
    }

    fun sortCompanyByNameDesend() {
        viewModelScope.launch(Dispatchers.IO) {
            sortCompanyList = repository?.sortCompanyByNameDesend()!!
            sortedCompanyList.postValue(sortCompanyList)
        }
    }

    fun sortMemberByNameAsend(companyID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isMemberSorted = true
            allMembers = repository?.sortMemberByNameAsend(companyID)
        }
    }

    fun sortMemberByNameDesend(companyID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isMemberSorted = true
            allMembers = repository?.sortMemberByNameDesend(companyID)
        }
    }

    fun sortMemberByAgeAsend(companyID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isMemberSorted = true
            allMembers = repository?.sortMemberByAgeAsend(companyID)
        }
    }

    fun sortMemberByAgeDesend(companyID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isMemberSorted = true
            allMembers = repository?.sortMemberByAgeDesend(companyID)
        }
    }
}