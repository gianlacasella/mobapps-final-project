package com.ifgarces.courseproject.networking

import com.ifgarces.courseproject.networking.ApiUser.id
import com.ifgarces.courseproject.networking.ApiUser.isAuthenticated
import com.ifgarces.courseproject.networking.ApiUser.token
import com.ifgarces.courseproject.utils.Logf


/**
 * Needed for storing user metadata when authentication is successful.
 * @property isAuthenticated Tells if the user was properly auth-ed with the API (has signed up or
 * logged in successfully).
 * @property id User ID given by the API server.
 * @property token Authentication token given by the API server.
 */
object ApiUser {
    private var isAuthenticated :Boolean = false
    private var id :String? = null
    private var token :String? = null

    // Getters for private attributes
    public fun getAuthStatus() = this.isAuthenticated
    public fun getID() = this.id
    public fun getToken() = this.token

    /**
     * Should be called after a successful authentication with the API.
     */
    public fun onSignIn(userID :String, userToken :String) {
        Logf("[ApiUser] Storing user metadata: ID=%s, token=%s", userID, userToken)
        if (this.isAuthenticated) {
            throw Exception("[ApiUser] Tried to sign in when user was already authenticated!")
        }
        this.isAuthenticated = true
        this.id = userID
        this.token = userToken
    }

    public fun logOut() {
        Logf("[ApiUser] Just logged out")
        this.isAuthenticated = false
        this.id = null
        this.token = null
    }
}