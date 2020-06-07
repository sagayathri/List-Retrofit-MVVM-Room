package com.gayathriarumugam.spectrumtask.Repo

import androidx.lifecycle.LiveData
import com.gayathriarumugam.spectrumtask.Data.Database.CompanyEntity
import com.gayathriarumugam.spectrumtask.Data.Database.DaoClass
import com.gayathriarumugam.spectrumtask.Data.Database.MemberEntity

class RoomRepository(private val daoObject: DaoClass) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    fun getAllCompanies(): LiveData<List<CompanyEntity>>? {
        return daoObject.getAllCompanys()
    }

    fun getAllMembers(companyID: String): LiveData<List<MemberEntity>>{
        return daoObject.getAllMembers(companyID)
    }

    fun insertCompanies(company: CompanyEntity) {
        daoObject.insertCompany(company)
    }

    fun insertMember(member: MemberEntity) {
        daoObject.insertMember(member)
    }

    fun updateCompany(company: CompanyEntity){
        if(daoObject.getCompanyName(company.companyName.toString())) {
            daoObject.updateCompany(company)
        }
    }

    fun updateMember(member: MemberEntity) {
        if (daoObject.getMemberName(member.firstName.toString())) {
            daoObject.updateMember(member)
        }
    }

    fun sortCompanyByNameAsend():  List<CompanyEntity>{
        return daoObject.sortCompanyByNameAsend()
    }

    fun sortCompanyByNameDesend(): List<CompanyEntity>{
        return daoObject.sortCompanyByNameDesend()
    }

    fun sortMemberByNameAsend(companyID: String): LiveData<List<MemberEntity>> {
        return daoObject.sortMemberByNameAsend(companyID = companyID)
    }

    fun sortMemberByNameDesend(companyID: String): LiveData<List<MemberEntity>> {
        return daoObject.sortMemberByNameDesend(companyID = companyID)
    }

    fun sortMemberByAgeAsend(companyID: String): LiveData<List<MemberEntity>> {
        return daoObject.sortMemberByAgeAsend(companyID = companyID)
    }

    fun sortMemberByAgeDesend(companyID: String): LiveData<List<MemberEntity>> {
        return daoObject.sortMemberByAgeDesend(companyID = companyID)
    }
}