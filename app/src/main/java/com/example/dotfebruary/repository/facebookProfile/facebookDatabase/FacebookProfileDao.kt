package com.example.dotfebruary.repository.facebookProfile.facebookDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dotfebruary.common.AppSettings.FACEBOOK_PROFILE_TABLE_NAME
import com.example.dotfebruary.model.FacebookProfile

@Dao
interface FacebookProfileDao {

    @Query("SELECT * from $FACEBOOK_PROFILE_TABLE_NAME")
    fun getProfile(): LiveData<FacebookProfile>    //возвратит 1й элемент листа(единственный )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: FacebookProfile)

    @Query("DELETE FROM $FACEBOOK_PROFILE_TABLE_NAME")
    fun deleteAll()

}