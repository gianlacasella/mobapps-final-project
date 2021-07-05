package com.ifgarces.courseproject.networking


/**
 * Encapsulating classes that will be only used when communicating with the Poker API
 * (Poker rooms handling only).
 */
object PokerRoomsApiClasses {
    // -------------- Create a room -------------- //
    data class Deck(
        val name :String,
        val cards :List<String>
    )

    data class CreateRoomRequest(
        val roomName :String,
        val password :String,
        val deck :Deck
    )

    data class CreateRoomResponse(
        val message :String,
        val roomId :String
    )

    // -------------- Join a room -------------- //
    data class JoinRoomRequest(
        val roomName :String,
        val password :String
    )

    data class JoinRoomResponse(
        val message :String,
        val members :List<String>
    )

    // -------------- Delete a room -------------- //
    data class DeleteRoomRequest(
        val name :String,
        val roomId :String
    )

    data class SimpleResponse(
        val message :String
    )

    // -------------- Get a room -------------- //
    data class GetRoomResponse(
        val roomId :String,
        val roomName :String,
        val deck :Deck,
        val members :List<String>
    )

    // -------------- Get all rooms -------------- //
    data class GetAllRoomsResponse(
        val rooms :List<GetAllRoomsResponseItem>
    )

    data class GetAllRoomsResponseItem(
        val roomId :String,
        val roomName :String
    )

    // -------------- Card votation -------------- //
    data class VoteRequest(
        val roomName :String,
        val vote :String
    )

    // -------------- Get vote result -------------- //
    data class GetResultResponse(
        val roomId :String,
        val deck :Deck,
        val results :List<GetResultResponseItem>
    )

    data class GetResultResponseItem(
        val name :String,
        val vote :String
    )
}