package com.example.dotfebruary.repository.facebookProfile

import androidx.lifecycle.LiveData
import com.example.dotfebruary.model.FacebookProfile

interface FacebookRepositoryApi {

    fun getFacebookProfile(): LiveData<FacebookProfile>
    fun saveFacebookProfile(profile: FacebookProfile)
}