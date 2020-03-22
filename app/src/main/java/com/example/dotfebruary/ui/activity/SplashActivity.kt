package com.example.dotfebruary.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.example.dotfebruary.R
import com.example.dotfebruary.common.AppSettings.LOG_TAG
import com.facebook.*
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    private val facebookCallbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (AccessToken.isCurrentAccessTokenActive()) {
            startMainActivity()
        }

        setContentView(R.layout.activity_splash)

        /*   LoginManager.getInstance().registerCallback(facebookCallbackManager,
               object : FacebookCallback<LoginResult> {
                   override fun onSuccess(result: LoginResult?) {
                       startMainActivity()
                   }

                   override fun onCancel() {
                       showErrorLoggingIn()
                   }

                   override fun onError(error: FacebookException?) {
                       showErrorLoggingIn()
                   }

                   private fun showErrorLoggingIn() {
                       AlertDialog.Builder(this@SplashActivity)
                           .setTitle(R.string.error_facebook_login_title)
                           .setMessage(R.string.error_facebook_login_message)
                           .setPositiveButton(R.string.ok, null)
                           .show()
                   }
               }
           )*/

        facebookLoginButton.setPermissions(listOf("public_profile", "email"))

        facebookLoginButton.registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    loadingBar.isVisible = true

                    //val accessToken = loginResult?.accessToken?.token

                    val request = GraphRequest.newMeRequest(loginResult?.accessToken)
                    { `object`, _ ->
                        loadingBar.isVisible = false
                        Log.d(LOG_TAG, `object`.toString())
                        startMainActivity()
                    }

                    request.parameters = bundleOf("fields" to "id, name, email, picture")
                    request.executeAsync()
                }

                override fun onCancel() {
                    showLoggingInError()
                }

                override fun onError(exception: FacebookException) {
                    showLoggingInError()
                }

                private fun showLoggingInError() {
                    AlertDialog.Builder(this@SplashActivity)
                        .setTitle(R.string.error_facebook_login_title)
                        .setMessage(R.string.error_facebook_login_message)
                        .setPositiveButton(R.string.ok, null)
                        .show()
                }
            })

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        this.finish()
    }

}
