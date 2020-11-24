package edu.uoc.pac3.data

import android.se.omapi.Session
import android.util.Log
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network.createHttpClient
import edu.uoc.pac3.data.oauth.OAuthConstants.clientID
import edu.uoc.pac3.data.oauth.OAuthConstants.clientSecret
import edu.uoc.pac3.data.oauth.OAuthConstants.redirectUri
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.data.user.Users
import io.ktor.client.*
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.*
import io.ktor.http.cio.parseResponse
import java.io.IOException
import java.lang.Exception
import kotlin.jvm.Throws

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {
        // TODO("Get Tokens from Twitch")
        val response = httpClient.post<OAuthTokensResponse>(Endpoints.token) {
            parameter("client_id", clientID)
            parameter("client_secret", clientSecret)
            parameter("code", authorizationCode)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", redirectUri)
        }
        Log.d("OAuth", "Access Token: ${response.accessToken}. Refresh Token: ${response.refreshToken}")
        return response
    }


    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(accessToken: String?, cursor: String? = null): StreamsResponse? {
        // TODO("Get Streams from Twitch")
        // TODO("Support Pagination")



            if(cursor == null)
            {
                try {
                    val response = httpClient.get<StreamsResponse>(Endpoints.streams) {
                        headers {
                            append("Client-ID", clientID)
                            append("Authorization", "Bearer $accessToken")
                        }
                    }

                    return response
                }catch (t: Throwable)
                {
                    getAnotherCredentials(t)
                }

            }
            else
            {
                try {
                    val response = httpClient.get<StreamsResponse>(Endpoints.streams) {
                        headers {
                            append("Client-ID", edu.uoc.pac3.data.oauth.OAuthConstants.clientID)
                            append("Authorization", "Bearer $accessToken")
                        }
                        parameter("after", cursor)
                    }
                    return response
                }catch (t: Throwable)
                {
                    getAnotherCredentials(t)
                }

            }

        return null

    }


    fun getAnotherCredentials(t: Throwable)
    {
        when(t)
        {
            is ClientRequestException ->
            {
                if (t.response?.status?.value == 401 || t.response?.status?.value == 400)
                {
                    throw UnauthorizedException
                }

            }
        }
    }

    @Throws(UnauthorizedException::class)
    suspend fun getAccessToken(refreshToken: String?): OAuthTokensResponse?
    {
        try {
            val response = httpClient.post<OAuthTokensResponse>(Endpoints.token) {
                parameter("client_id", clientID)
                parameter("client_secret", clientSecret)
                parameter("refresh_token", refreshToken)
                parameter("grant_type", "refresh_token")
            }
            Log.d("OAuth", "Access Token: ${response.accessToken}. Refresh Token: ${response.refreshToken}")
            return response
        }catch (t: Throwable)
        {
            getAnotherCredentials(t)
        }
        return null
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(accessToken: String?): Users? {
        //TODO("Get User from Twitch")

        val response = httpClient.get<Users>(Endpoints.user) {
            headers {
                append("Client-ID", clientID)
                append("Authorization", "Bearer $accessToken")
            }
        }

        Log.d("OAuth", "response user" +response.toString())

        return response
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(accessToken: String?, description: String): Users? {
        // TODO("Update User Description on Twitch")

        val response = httpClient.put<Users>(Endpoints.user)
        {
            headers {
                append("Client-ID", clientID)
                append("Authorization", "Bearer $accessToken")
            }
            parameter("description", description)
        }

        return response

    }
}