package com.gayathriarumugam.spectrumtask.Data.Database

import androidx.lifecycle.LiveData
import androidx.room.*


//Interface for database access on [Company, Member] related operations.

@Dao
interface DaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompany(company: CompanyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMember(member: MemberEntity)

    @Query("SELECT * FROM company")
    fun getAllCompanys(): LiveData<List<CompanyEntity>>

    @Query("SELECT * FROM member WHERE companyID = :companyID")
    fun getAllMembers(companyID: String): LiveData<List<MemberEntity>>

    @Query("SELECT * from company ORDER BY companyName ASC")
    fun sortCompanyByNameAsend(): List<CompanyEntity>

    @Query("SELECT * from company ORDER BY companyName DESC")
    fun sortCompanyByNameDesend(): List<CompanyEntity>

    @Query("SELECT * from member WHERE companyID = :companyID ORDER BY firstName ASC")
    fun sortMemberByNameAsend(companyID: String): LiveData<List<MemberEntity>>

    @Query("SELECT * from member WHERE companyID = :companyID ORDER BY firstName DESC")
    fun sortMemberByNameDesend(companyID: String): LiveData<List<MemberEntity>>

    @Query("SELECT * from member WHERE companyID = :companyID ORDER BY age ASC")
    fun sortMemberByAgeAsend(companyID: String): LiveData<List<MemberEntity>>

    @Query("SELECT * from member WHERE companyID = :companyID ORDER BY age DESC")
    fun sortMemberByAgeDesend(companyID: String): LiveData<List<MemberEntity>>

    @Query("SELECT COUNT(*) FROM company WHERE companyName = :companyName")
    fun getCompanyName(companyName: String): Boolean

    @Query("SELECT COUNT(*) FROM member WHERE firstName = :firstName")
    fun getMemberName(firstName: String): Boolean

    @Update
    fun updateCompany(company: CompanyEntity)

    @Update
    fun updateMember(member: MemberEntity)
}