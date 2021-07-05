package com.ifgarces.courseproject.networking


/**
 * Encapsulating classes that will be only used when communicating with the Poker API
 * (authentication only).
 */
object PokerAuthApiClasses {

    // -------------- For logging in -------------- //

    data class LoginRequest(
        val username :String,
        val password :String
    )

    data class LoginResponse(
        val user_id :String,
        val token   :String
    )

    // -------------- For registering -------------- //

    data class SignupRequest(
        val username :String,
        val name     :String,
        val password :String
    )

    data class SignupResponse(
        val message :String,
        val user_id :String,
        val token   :String
    )

}
