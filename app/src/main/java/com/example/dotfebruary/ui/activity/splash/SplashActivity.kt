package com.example.dotfebruary.ui.activity.splash

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.example.dotfebruary.R
import com.example.dotfebruary.common.AppSettings.LOG_TAG
import com.example.dotfebruary.model.FacebookProfile
import com.example.dotfebruary.ui.activity.main.MainActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val facebookCallbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (AccessToken.isCurrentAccessTokenActive()) {
            onTokenCheckSuccess(AccessToken.getCurrentAccessToken())
        }

        facebookLoginButton.setPermissions(listOf("public_profile", "email"))
        facebookLoginButton.registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    onTokenCheckSuccess(loginResult?.accessToken)
                }

                override fun onCancel() {
                    showLoggingInError()
                }

                override fun onError(exception: FacebookException) {
                    showLoggingInError()
                }
            })
    }

    fun onTokenCheckSuccess(accessToken: AccessToken?) {
        loadingBar.isVisible = true

        val request = GraphRequest.newMeRequest(accessToken) { jsonObject, _ ->
            loadingBar.isVisible = false
            Log.d(LOG_TAG, jsonObject.toString())
            val profile: FacebookProfile
            try {
                profile = Gson().fromJson(jsonObject.toString(), FacebookProfile::class.java)
                viewModel.saveFacebookProfileToDb(profile, {
                    Log.d(LOG_TAG, "profile: $profile")
                    startMainActivity()
                }, {
                    showLoggingInError()
                    LoginManager.getInstance().logOut()
                })

            } catch (e: Exception) {
                showLoggingInError()
                LoginManager.getInstance().logOut()
                Log.d(LOG_TAG, e.message ?: "")
            }
        }

        request.parameters = bundleOf("fields" to "id, name, email")
        request.executeAsync()
    }

    private fun startMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        this.finish()
    }

    private fun showLoggingInError() {
        AlertDialog.Builder(this@SplashActivity)
            .setTitle(R.string.error_facebook_login_title)
            .setMessage(R.string.error_facebook_login_message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}
