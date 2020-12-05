package edu.uoc.pac3.data.network

import android.content.Context
import android.util.Log
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.UnauthorizedException
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

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

            val sessionManager = SessionManager(context)
            /*install(OAuthFeature) {
                getToken = { sessionManager.getAccessToken().toString() }
                refreshToken = { sessionManager.getRefreshToken() }
            }*/

            // Apply to All Requests
            defaultRequest {
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                val bearerToken =  sessionManager.getAccessToken()
                headers {
                    append("Authorization","Bearer $bearerToken")
                    append("Client-Id", OAuthConstants.clientId)
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    val statusCode= response.status.value
                    when (statusCode)
                    {
                        401 -> throw UnauthorizedException
                    }
                }
            }

            // Optional OkHttp Interceptors
            engine {
                //addInterceptor()
                //addInterceptor(CurlInterceptor())
            }

        }
    }
}

public class CurlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("Inteceptor", "Error $chain")
        val request: Request = chain.request()
        return chain.proceed(request)
    }

}