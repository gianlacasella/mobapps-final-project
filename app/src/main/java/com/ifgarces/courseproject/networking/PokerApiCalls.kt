package com.ifgarces.courseproject.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url


/**
 * Poker API for online playing.
 */
interface PokerApiCalls {
    @Headers("Content-Type:application/json")
    @POST("login")
    public fun login(
        @Body loginRequest :PokerAuthApiClasses.LoginRequest
    ) :Call<PokerAuthApiClasses.LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("signup")
    public fun signup(
        @Body signupRequest :PokerAuthApiClasses.SignupRequest
    ) :Call<PokerAuthApiClasses.SignupResponse>

    @Headers("Content-Type:application/json")
    @GET("decks")
    public fun getAllDecks(
        @Header("token") token :String
    ) :Call<List<PokerRoomsApiClasses.Deck>>

    @Headers("Content-Type:application/json")
    @POST("rooms")
    public fun createRoom(
        @Header("token") token :String,
        @Body createRoomRequest :PokerRoomsApiClasses.CreateRoomRequest
    ) :Call<PokerRoomsApiClasses.CreateRoomResponse>

    @Headers("Content-Type:application/json")
    @POST("joinRoom")
    public fun joinRoom(
        @Header("token") token :String,
        @Body joinRoomRequest :PokerRoomsApiClasses.JoinRoomRequest
    ) :Call<PokerRoomsApiClasses.JoinRoomResponse>

    @Headers("Content-Type:application/json")
    @HTTP(method="DELETE", path="/room", hasBody=true)
    public fun deleteRoom(
        @Header("token") token :String,
        @Body deleteRoomRequest :PokerRoomsApiClasses.DeleteRoomRequest
    ) :Call<PokerRoomsApiClasses.SimpleResponse>

    @Headers("Content-Type:application/json")
    @GET("rooms")
    public fun getAllRooms(
        @Header("token") token :String
    ) :Call<PokerRoomsApiClasses.GetAllRoomsResponse>

    @Headers("Content-Type:application/json")
    @GET("rooms/{mRoomName}")
    public fun getRoom(
        @Header("token") token :String,
        @Path(value="mRoomName") roomName :String
    ) :Call<PokerRoomsApiClasses.GetRoomResponse>

    @Headers("Content-Type:application/json")
    @GET("getResult/{mRoomName}")
    public fun getVotingResult(
        @Header("token") token :String,
        @Path(value="mRoomName") roomName :String
    ) :Call<PokerRoomsApiClasses.GetResultResponse>

    @Headers("Content-Type:application/json")
    @POST("vote")
    public fun voteForCard(
        @Header("token") token :String,
        @Body voteRequest :PokerRoomsApiClasses.VoteRequest
    ) :Call<PokerRoomsApiClasses.SimpleResponse>


    @Headers("Content-Type:application/json")
    @POST("reportLocation")
    public fun reportLocation(
        @Header("token") token :String,
        @Body reportLocationRequest :PokerRoomsApiClasses.ReportLocationRequest
    ) :Call<PokerRoomsApiClasses.SimpleResponse>
}
