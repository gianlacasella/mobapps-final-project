package com.ifgarces.courseproject.networking

import com.ifgarces.courseproject.networking.PokerApiHandler.API_URL
import com.ifgarces.courseproject.networking.PokerApiHandler.pokerApiCallsService
import com.ifgarces.courseproject.networking.PokerApiHandler.retrofit
import com.ifgarces.courseproject.utils.Logf
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Encapsulates all API calls.
 * @property API_URL Base URL of the Auth API.
 * @property retrofit Retrofit instance (to ensure singleton usage).
 * @property pokerApiCallsService Holds the methods that encapsulate requests to the API.
 */
object PokerApiHandler {
    private const val API_URL :String = "https://kfeeav6eie.execute-api.us-east-1.amazonaws.com/"

    private lateinit var retrofit             :Retrofit
    private lateinit var pokerApiCallsService :PokerApiCalls

    public fun init() {
        Logf("[PokerApiHandler] Initializing")
        this.retrofit = Retrofit.Builder()
            .baseUrl(this.API_URL)
            .client(this.buildHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.pokerApiCallsService = this.retrofit.create(PokerApiCalls::class.java)
    }

    private fun getLoggingInterceptor(isDebug :Boolean) :HttpLoggingInterceptor {
        val logging :HttpLoggingInterceptor = HttpLoggingInterceptor()
        logging.level = if (isDebug)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        return logging
    }

    private fun buildHttpClient() = OkHttpClient().newBuilder()
        //.readTimeout(20, TimeUnit.SECONDS) // default: 10 seconds
        //.connectTimeout(20, TimeUnit.SECONDS) // default: 10 seconds
        .addInterceptor(this.getLoggingInterceptor(isDebug = true))
        .build()

    /**
     * Logs in user with ID `username` and password `password`.
     * @param onSuccess Action to execute when the API response is OK.
     * @param onFailure Action to perform when the API received a failure response. This can happen
     * if the user tries to login but their account doesn't exist yet.
     */
    public fun loginCall(
        onSuccess :(response :PokerAuthApiClasses.LoginResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        username :String,
        password :String
    ) {
        Logf("[PokerApiHandler] Executing loginCall...")
        this.pokerApiCallsService
            .login(PokerAuthApiClasses.LoginRequest(username, password))
            .enqueue(object : Callback<PokerAuthApiClasses.LoginResponse> {
                override fun onResponse(
                    call :Call<PokerAuthApiClasses.LoginResponse>,
                    response :Response<PokerAuthApiClasses.LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] loginCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerAuthApiClasses.LoginResponse? = response.body()
                        Logf("[PokerApiHandler] loginCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] loginCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerAuthApiClasses.LoginResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] loginCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    /**
     * Registers the user with ID `username`, person name `name` and password `password`.
     * @param onSuccess Action to execute when the API response is OK.
     * @param onFailure Action to perform when the API received a failure response.
     */
    public fun signupCall(
        onSuccess :(response :PokerAuthApiClasses.SignupResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        username :String,
        name :String,
        password :String
    ){
        Logf("[PokerApiHandler] Executing signupCall...")
        this.pokerApiCallsService
            .signup(PokerAuthApiClasses.SignupRequest(username, name, password))
            .enqueue(object : Callback<PokerAuthApiClasses.SignupResponse> {
                override fun onResponse(
                    call :Call<PokerAuthApiClasses.SignupResponse>,
                    response :Response<PokerAuthApiClasses.SignupResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] signupCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerAuthApiClasses.SignupResponse? = response.body()
                        Logf("[PokerApiHandler] signupCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] signupCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerAuthApiClasses.SignupResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] signupCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    /**
     * Creates a room.
     * @param onSuccess Action to execute when the API response is OK.
     * @param onFailure Action to perform when the API received a failure response.
     */
    public fun createRoomCall(
        onSuccess :(response :PokerRoomsApiClasses.CreateRoomResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        token :String,
        roomName :String,
        roomPassword :String,
        roomDeck :PokerRoomsApiClasses.Deck
    ) {
        Logf("[PokerApiHandler] Executing createRoomCall...")
        this.pokerApiCallsService
            .createRoom(
                token = token,
                createRoomRequest = PokerRoomsApiClasses.CreateRoomRequest(roomName, roomPassword, roomDeck)
            )
            .enqueue(object : Callback<PokerRoomsApiClasses.CreateRoomResponse> {
                override fun onResponse(
                    call :Call<PokerRoomsApiClasses.CreateRoomResponse>,
                    response :Response<PokerRoomsApiClasses.CreateRoomResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] createRoomCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerRoomsApiClasses.CreateRoomResponse? = response.body()
                        Logf("[PokerApiHandler] createRoomCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] createRoomCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerRoomsApiClasses.CreateRoomResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] createRoomCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    public fun joinRoomCall(
        onSuccess :(response :PokerRoomsApiClasses.JoinRoomResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        token :String,
        roomName :String,
        roomPassword :String,
    ) {
        Logf("[PokerApiHandler] Executing joinRoomCall...")
        this.pokerApiCallsService
            .joinRoom(
                token = token,
                joinRoomRequest = PokerRoomsApiClasses.JoinRoomRequest(roomName, roomPassword)
            )
            .enqueue(object : Callback<PokerRoomsApiClasses.JoinRoomResponse> {
                override fun onResponse(
                    call :Call<PokerRoomsApiClasses.JoinRoomResponse>,
                    response :Response<PokerRoomsApiClasses.JoinRoomResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] joinRoomCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerRoomsApiClasses.JoinRoomResponse? = response.body()
                        Logf("[PokerApiHandler] joinRoomCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] joinRoomCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerRoomsApiClasses.JoinRoomResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] joinRoomCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    public fun getAllDecksCall(
        onSuccess :(response :List<PokerRoomsApiClasses.Deck>) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        token :String
    ) {
        this.pokerApiCallsService
            .getAllDecks(
                token = token
            )
            .enqueue(object : Callback<List<PokerRoomsApiClasses.Deck>> {
                override fun onResponse(
                    call :Call<List<PokerRoomsApiClasses.Deck>>,
                    response :Response<List<PokerRoomsApiClasses.Deck>>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] getAllDecksCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :List<PokerRoomsApiClasses.Deck>? = response.body()
                        Logf("[PokerApiHandler] getAllDecksCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] getAllDecksCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<List<PokerRoomsApiClasses.Deck>>, t :Throwable) {
                    Logf("[PokerApiHandler] getAllDecksCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    public fun deleteRoomCall(
        onSuccess :(response :PokerRoomsApiClasses.SimpleResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        token :String,
        roomName :String,
        roomId :String
    ) {
        Logf("[PokerApiHandler] Executing deleteRoomCall...")
        this.pokerApiCallsService
            .deleteRoom(
                token = token,
                deleteRoomRequest = PokerRoomsApiClasses.DeleteRoomRequest(
                    name = roomName,
                    roomId = roomId
                )
            )
            .enqueue(object : Callback<PokerRoomsApiClasses.SimpleResponse> {
                override fun onResponse(
                    call :Call<PokerRoomsApiClasses.SimpleResponse>,
                    response :Response<PokerRoomsApiClasses.SimpleResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] deleteRoomCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerRoomsApiClasses.SimpleResponse? = response.body()
                        Logf("[PokerApiHandler] deleteRoomCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] deleteRoomCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerRoomsApiClasses.SimpleResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] deleteRoomCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    /**
     * Note: unused, see `PokerRoomsViewModel.init` method.
     */
    public fun getAllRoomsCall(
        onSuccess :(response :PokerRoomsApiClasses.GetAllRoomsResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        token :String
    ) {
        Logf("[PokerApiHandler] Executing getAllRoomsCall...")
        this.pokerApiCallsService
            .getAllRooms(token=token)
            .enqueue(object : Callback<PokerRoomsApiClasses.GetAllRoomsResponse> {
                override fun onResponse(
                    call :Call<PokerRoomsApiClasses.GetAllRoomsResponse>,
                    response :Response<PokerRoomsApiClasses.GetAllRoomsResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] getAllRoomsCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerRoomsApiClasses.GetAllRoomsResponse? = response.body()
                        Logf("[PokerApiHandler] getAllRoomsCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] getAllRoomsCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerRoomsApiClasses.GetAllRoomsResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] getAllRoomsCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    public fun getResultCall(
        onSuccess :(response :PokerRoomsApiClasses.GetResultResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        token :String,
        roomName :String //! query inside the request path, not in the body!
    ) {
        Logf("[PokerApiHandler] Executing getResultCall...")
        this.pokerApiCallsService
            .getVotingResult(
                token = token,
                roomName = roomName
            )
            .enqueue(object : Callback<PokerRoomsApiClasses.GetResultResponse> {
                override fun onResponse(
                    call :Call<PokerRoomsApiClasses.GetResultResponse>,
                    response :Response<PokerRoomsApiClasses.GetResultResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] getResultCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerRoomsApiClasses.GetResultResponse? = response.body()
                        Logf("[PokerApiHandler] getResultCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] getResultCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerRoomsApiClasses.GetResultResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] getResultCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }

    public fun voteCall(
        onSuccess :(response :PokerRoomsApiClasses.SimpleResponse) -> Unit,
        onFailure :(responseMessage :String?) -> Unit,
        token :String,
        roomName :String,
        vote :String
    ) {
        Logf("[PokerApiHandler] Executing voteCall...")
        this.pokerApiCallsService
            .voteForCard(
                token = token,
                voteRequest = PokerRoomsApiClasses.VoteRequest(
                    roomName = roomName,
                    vote = vote
                )
            )
            .enqueue(object : Callback<PokerRoomsApiClasses.SimpleResponse> {
                override fun onResponse(
                    call :Call<PokerRoomsApiClasses.SimpleResponse>,
                    response :Response<PokerRoomsApiClasses.SimpleResponse>
                ) {
                    if (response.isSuccessful) {
                        Logf("[PokerApiHandler] voteCall response successful with status code %d", response.code())
                    }
                    if (response.code() == 200) {
                        val body :PokerRoomsApiClasses.SimpleResponse? = response.body()
                        Logf("[PokerApiHandler] getResultCall response body: %s", body)
                        if (body == null) {
                            Logf("[PokerApiHandler] voteCall response body is unexpectedly null")
                            onFailure.invoke(null)
                        } else {
                            onSuccess.invoke(body)
                        }
                    } else {
                        onFailure.invoke(null)
                    }
                }

                override fun onFailure(call :Call<PokerRoomsApiClasses.SimpleResponse>, t :Throwable) {
                    Logf("[PokerApiHandler] voteCall request failed: %s", t.message)
                    onFailure.invoke(t.message)
                }
            })
    }
}
