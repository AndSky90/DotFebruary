package com.example.dotfebruary.repository.facebookProfile

import com.example.dotfebruary.model.FacebookProfile
import com.example.dotfebruary.repository.facebookProfile.facebookDatabase.FacebookDatabaseApi
import com.example.dotfebruary.repository.facebookProfile.facebookDatabase.FacebookProfileDao
import org.koin.core.KoinComponent
import org.koin.core.inject


class FacebookRepository : FacebookRepositoryApi, KoinComponent {

    private val di by inject<FacebookDatabaseApi>()
    private val profileDao: FacebookProfileDao

    init {
        profileDao = di.profileDao()
    }

    override fun getFacebookProfile() = profileDao.getProfile()

    override fun saveFacebookProfile(profile: FacebookProfile) = profileDao.insert(profile)

}