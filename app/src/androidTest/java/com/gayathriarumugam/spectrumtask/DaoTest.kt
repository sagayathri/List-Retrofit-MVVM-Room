package com.gayathriarumugam.spectrumtask

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gayathriarumugam.spectrumtask.Data.Database.AppRoomDatabase
import com.gayathriarumugam.spectrumtask.Data.Database.CompanyEntity
import com.gayathriarumugam.spectrumtask.Data.Database.DaoClass
import com.gayathriarumugam.spectrumtask.Data.Database.MemberEntity
import org.junit.*
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaoTest {

    private lateinit var daoObject: DaoClass
    private lateinit var db: AppRoomDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        daoObject = db.daoObject()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCompany() {
        val company = CompanyEntity("1",
            "TEST 1",
            "www.google.co.uk",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSpJQIfoLgXnS37EqZ1N1TZuUHLyOhBYCLcGQjHl1iomN11jwR7&usqp=CAU",
            "This is to test company table",
            0,
            0)
        daoObject.insertCompany(company)
        val allCompanies = daoObject.getAllCompanys().waitForValue()
        assertEquals(allCompanies[0].companyName, company.companyName)
    }

    @Test
    @Throws(Exception::class)
    fun getAllCompanies() {
        val company = CompanyEntity("2",
            "TEST 2",
            "www.google.co.uk",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSpJQIfoLgXnS37EqZ1N1TZuUHLyOhBYCLcGQjHl1iomN11jwR7&usqp=CAU",
            "This is to test company table",
            1,
            0)
        daoObject.insertCompany(company)
        val allCompanies = daoObject.getAllCompanys().waitForValue()
        assertEquals(allCompanies[0].companyName, company.companyName)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMember() {
        val member = MemberEntity("meb1",
            "1",
            23,
            "John",
            "Son",
            "john@xyz.com",
            "+1234567",
            0)
        daoObject.insertMember(member)
        val allMembers = daoObject.getAllMembers("1").waitForValue()
        assertEquals(allMembers[0].firstName, member.firstName)
    }

    @Test
    @Throws(Exception::class)
    fun getAllMembers() {
        val member = MemberEntity("meb1",
            "1",
            23,
            "John",
            "Son",
            "john@xyz.com",
            "+1234567",
            0)
        daoObject.insertMember(member)
        val allMembers = daoObject.getAllMembers("1").waitForValue()
        assertEquals(allMembers[0].firstName, member.firstName)
    }
}