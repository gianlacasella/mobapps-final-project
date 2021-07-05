package com.ifgarces.courseproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog
import com.ifgarces.courseproject.utils.toastf


/**
 * Activity that handles the authentication views.
 * @property dummyAuth FOR DEBUGGING ONLY! Setting it to true performs a dummy login. It should be
 * always false on any serious build!
 */
class MainActivity : AppCompatActivity() {

    private class ActivityUI(owner :AppCompatActivity) {
        val progressBackground :View = owner.findViewById(R.id.auth_progressBackground)
        val progressBar        :View = owner.findViewById(R.id.auth_progressBar)

        val pageBanner        :TextView = owner.findViewById(R.id.auth_pageTitle)
        val authButton        :Button = owner.findViewById(R.id.auth_loginButton)
        val nameContainer     :View = owner.findViewById(R.id.auth_nameContainer)
        val nameTextBox       :EditText = owner.findViewById(R.id.auth_nameTextbox)
        val usernameTextBox   :EditText = owner.findViewById(R.id.auth_username)
        val passwordTextBox   :EditText = owner.findViewById(R.id.auth_passwordTextbox)
        val signupCheckBox    :CheckBox = owner.findViewById(R.id.auth_signupCheckBox)
        val dummyAuthCheckBox :CheckBox = owner.findViewById(R.id.auth_dummyCheckBox)
    }; private lateinit var UI :ActivityUI

    private var dummyAuth :Boolean = false
    
    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_login)
        this.UI = ActivityUI(owner=this)

        PokerApiHandler.init()
        ApiUser.logOut() // this is not necessary, but I don't want the app to crash. It does not harm.

        UI.progressBackground.visibility = View.GONE
        UI.progressBar.visibility = View.GONE

        UI.signupCheckBox.isChecked = false // by default, the view is for login, not signup
        this.refreshAuthView(UI.signupCheckBox.isChecked)

        UI.authButton.setOnClickListener {
            if (this.dummyAuth) {
                this.onAuthenticationSucceeded("dummy", "dummy")
                return@setOnClickListener
            }
            if (UI.signupCheckBox.isChecked) {
                this.signupRequest()
            }
            else {
                this.loginRequest()
            }
        }

        UI.signupCheckBox.setOnCheckedChangeListener { _ :CompoundButton?, isChecked :Boolean ->
            Logf("[MainActivity] Authentication mode switched to %s", if (isChecked) "SIGNUP" else "LOGIN")
            this.refreshAuthView(isChecked)
        }

        UI.dummyAuthCheckBox.setOnCheckedChangeListener { _ :CompoundButton?, isChecked :Boolean ->
            Logf("[MainActivity] Dummy authentication toggled to %s", if (isChecked) "true" else "false")
            this.dummyAuth = isChecked
            if (isChecked) this.toastf("Dummy authentication active, ignoring API calls")
        }
    }

    /**
     * Switches between login and signup mode for user authentication. Updates the views accordingly.
     * @param isSignupMode Boolean
     */
    private fun refreshAuthView(isSignupMode :Boolean) {
        if (isSignupMode) {
            UI.pageBanner.text = "Signup"
            UI.nameContainer.visibility = View.VISIBLE
        }
        else {
            UI.pageBanner.text = "Login"
            UI.nameContainer.visibility = View.GONE
        }
    }

    /**
     * User wants to register a new account.
     */
    private fun signupRequest() {
        if (UI.nameTextBox.text.isBlank() || UI.usernameTextBox.text.isBlank() || UI.passwordTextBox.text.isBlank()) {
            this.toastf("Please fill all name, email and password")
            return
        }

        Logf("[MainActivity] Registering new account...")
        this.showLoadingScreen()
        PokerApiHandler.signupCall(
            onSuccess = {
                this.infoDialog(
                    title = "Signup successful!",
                    message = "Server message: %s".format(it.message),
                    onDismiss = {
                        this.onAuthenticationSucceeded(userID=it.user_id, userToken=it.token)
                    },
                    icon = R.drawable.sparkles_icon
                )
            },
            onFailure = { serverMessage :String? ->
                this.hideLoadingScreen()
                this.infoDialog(
                    title = "Signup failed",
                    message = serverMessage ?: "Please check your internet connection",
                    icon = R.drawable.warning_icon,
                )
            },
            username = UI.usernameTextBox.text.toString(),
            name = UI.nameTextBox.text.toString(),
            password = UI.passwordTextBox.text.toString()
        )
    }

    /**
     * User wants to login with an existing account.
     */
    private fun loginRequest() {
        if (UI.usernameTextBox.text.isBlank() || UI.passwordTextBox.text.isBlank()) {
            this.toastf("Please fill both email and password")
            return
        }

        Logf("[MainActivity] Logging in...")
        this.showLoadingScreen()
        PokerApiHandler.loginCall(
            onSuccess = {
                this.onAuthenticationSucceeded(userID=it.user_id, userToken=it.token)
            },
            onFailure = { serverMessage :String? ->
                this.hideLoadingScreen()
                this.infoDialog(
                    title = "Login failed",
                    message = serverMessage ?: "Please check your internet connection",
                    icon = R.drawable.warning_icon
                )
            },
            username = UI.usernameTextBox.text.toString(),
            password = UI.passwordTextBox.text.toString()
        )
    }

    /**
     * Considers authentication was dealt with and navigates to `PlanningActivity`.
     */
    private fun onAuthenticationSucceeded(userID :String, userToken :String) {
        this.hideLoadingScreen()
        Logf("[MainActivity] Authentication succeeded. userID=%s, userToken=%s", userID, userToken)
        this.toastf("Authenticated with user ID %s", userID)
        ApiUser.onSignIn(userID, userToken)
        this.startActivity(
            Intent(this, PlanningActivity::class.java)
        )
    }

    private fun hideLoadingScreen() {
        UI.progressBackground.visibility = View.GONE
        UI.progressBar.visibility = View.GONE
        UI.authButton.isClickable = true
    }

    private fun showLoadingScreen() {
        UI.progressBackground.visibility = View.VISIBLE
        UI.progressBar.visibility = View.VISIBLE
        UI.authButton.isClickable = false
    }
}
