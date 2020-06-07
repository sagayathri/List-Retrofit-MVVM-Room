package com.gayathriarumugam.spectrumtask.Data.Database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gayathriarumugam.spectrumtask.Data.Model.Company
import com.gayathriarumugam.spectrumtask.Data.Model.Member
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [CompanyEntity::class, MemberEntity::class], version = 1, exportSchema = false)
public abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun daoObject(): DaoClass

    companion object {
        //Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "this_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }


        private class AppDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.daoObject())
                    }
                }
            }
        }

        //Populate the database in a new coroutines
        fun populateDatabase(daoObject: DaoClass) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
//            daoObject.deleteCompany()
//            daoObject.deleteMembers()
        }
    }
}