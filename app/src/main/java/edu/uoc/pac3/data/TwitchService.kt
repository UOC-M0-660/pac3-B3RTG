package edu.uoc.pac3.data

import android.se.omapi.Session
import android.util.Log
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network.createHttpClient
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.data.user.UsersResponse
import io.ktor.client.*
import io.ktor.client.request.*
import java.lang.Exception
import kotlin.jvm.Throws

/**
 * Created by alex on 24/10/2020.
 * Modified by albert on 01/12/2020
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {
        val response = httpClient.post<OAuthTokensResponse>(Endpoints.oauthTokensUrl){
            parameter("client_id", OAuthConstants.clientId)
            parameter("client_secret", OAuthConstants.clientSecret)
            parameter("code", authorizationCode)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", OAuthConstants.redirectUri)
        }

        Log.d(TAG, "Access Token: ${response.accessToken}. Refresh Token: ${response.refreshToken}")

        return response
    }

    suspend fun refreshToken(refreshToken: String): OAuthTokensResponse {
        val response = httpClient.post<OAuthTokensResponse>(Endpoints.oauthTokensUrl){
            parameter("client_id", OAuthConstants.clientId)
            parameter("client_secret", OAuthConstants.clientSecret)
            parameter("refresh_token", refreshToken)
            parameter("grant_type", "refresh_token")
        }

        Log.d(TAG, "Access Token: ${response.accessToken}. Refresh Token: ${response.refreshToken}")

        return response
    }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {
        var response: StreamsResponse? = null
        try {
            response = httpClient.get<StreamsResponse>(Endpoints.twitchStreamsUrl) {
                if (cursor != "") {
                    parameter("first", 20)
                    parameter("after", cursor)
                }
            }

            Log.d(TAG, response.pagination?.cursor.toString())

        } catch (e: UnauthorizedException)
        {
            Log.d(TAG, "Unautorized Exception ${e.toString()}")
            throw UnauthorizedException
        } catch (e: Exception) {
            Log.d(TAG, "Exception ${e.toString()}")
        }
        return response
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
        var response: UsersResponse? = null

        try {
            response = httpClient.get<UsersResponse>(Endpoints.twitchUserUrl){
                // If neither a user ID nor a login name is specified, the user is looked up by Bearer token. Then we get the login user.
            }
            Log.d(TAG, "User data: ${response.data}.")
        }  catch (e: UnauthorizedException)
        {
            Log.d(TAG, "Unautorized Exception ${e.toString()}")
            throw UnauthorizedException
        } catch (e: Exception) {
            Log.d(TAG, "Exception ${e.toString()}")
        }

        return response?.data?.first()
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        var response: UsersResponse? = null
        try {
            response = httpClient.put<UsersResponse>(Endpoints.twitchUserUrl){
                //Updates the description of a user specified by a Bearer token. No parameters needed
                parameter("description",description)
            }
        } catch (e: UnauthorizedException)
        {
            Log.d(TAG, "Unautorized Exception ${e.toString()}")
            throw UnauthorizedException
        } catch (e: Exception) {
            Log.d(TAG, "Exception ${e.toString()}")
        }

        return response?.data?.first()
    }
}