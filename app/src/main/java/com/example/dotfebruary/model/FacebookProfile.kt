package com.example.dotfebruary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dotfebruary.common.AppSettings.FACEBOOK_PROFILE_TABLE_NAME
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = FACEBOOK_PROFILE_TABLE_NAME)
data class FacebookProfile(

    @PrimaryKey
    @ColumnInfo(name = "primaryKey")
    @Expose(serialize = false, deserialize = false)
    val primaryKey: Int = 1,

    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    val id: String = "",

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    val name: String = "",

    @ColumnInfo(name = "email")
    @SerializedName("email")
    @Expose
    val email: String = ""

)