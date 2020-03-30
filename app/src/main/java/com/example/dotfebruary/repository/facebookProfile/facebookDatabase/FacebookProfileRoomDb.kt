package com.example.dotfebruary.repository.facebookProfile.facebookDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dotfebruary.common.AppSettings.FACEBOOK_PROFILE_DB_NAME
import com.example.dotfebruary.model.FacebookProfile

@Database(entities = [FacebookProfile::class], version = 1, exportSchema = false)

abstract class FacebookProfileRoomDb : RoomDatabase(), FacebookDatabaseApi {

    abstract override fun profileDao(): FacebookProfileDao

    companion object {
        @Volatile
        private var INSTANCE: FacebookProfileRoomDb? = null

        fun getDatabase(context: Context): FacebookProfileRoomDb {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FacebookProfileRoomDb::class.java,
                    FACEBOOK_PROFILE_DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}