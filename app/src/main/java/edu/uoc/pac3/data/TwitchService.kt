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
import io.ktor.client.*
import io.ktor.client.request.*
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

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {


        val response = httpClient.get<StreamsResponse>(Endpoints.twitchStreamsUrl){

            /*
            Omitido y añadido en constructor de httpclient por defecto
            headers {
                append("Authorization","Bearer $cursor")
                append("Client-Id", OAuthConstants.clientId)
            }*/

        }

        return response
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
        TODO("Get User from Twitch")
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        TODO("Update User Description on Twitch")
    }
}