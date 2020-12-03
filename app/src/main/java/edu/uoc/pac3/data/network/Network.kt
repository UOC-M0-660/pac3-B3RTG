package edu.uoc.pac3.data.network

import android.content.Context
import android.util.Log
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.oauth.OAuthConstants
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

/**
 * Created by alex on 07/09/2020.
 */
object Network {

    private const val TAG = "Network"
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    fun createHttpClient(context: Context): HttpClient {
        return HttpClient(OkHttp) {

            // Json tools
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }

            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }

            // Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }

            // Apply to All Requests
            defaultRequest {
                //parameter("api_key", "some_api_key")
                // Content Type
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                val bearerToken =  SessionManager(context).getAccessToken()
                headers {
                    append("Authorization","Bearer $bearerToken")
                    append("Client-Id", OAuthConstants.clientId)
                }
            }
            // Optional OkHttp Interceptors
            engine {
                //addInterceptor(CurlInterceptor(Loggable { Log.v("Curl", it) }))
            }

        }
    }
}