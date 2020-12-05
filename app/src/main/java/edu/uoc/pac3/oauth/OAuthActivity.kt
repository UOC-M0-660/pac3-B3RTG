package edu.uoc.pac3.oauth

import android.content.Intent
import android.net.Network
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.google.android.material.snackbar.Snackbar
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network.createHttpClient
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import java.util.*

import kotlinx.coroutines.*


class OAuthActivity : AppCompatActivity() {

    private val TAG = "OAuthActivity"
    private val uniqueState = UUID.randomUUID().toString()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)

        //test()
        sessionManager = SessionManager(this)
        launchOAuthAuthorization()

    }

    fun buildOAuthUri(): Uri {
        return Uri.parse(Endpoints.oauthAuthorizationUrl)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.clientId)
            .appendQueryParameter("redirect_uri", OAuthConstants.redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", OAuthConstants.scopes.joinToString(separator = " "))
            .appendQueryParameter("state", uniqueState)
            .build()
    }

    private fun openLaunchActivity()
    {
        startActivity(Intent(this, LaunchActivity::class.java))
        finish()
    }

    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(OAuthConstants.redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
                        if (responseState == uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { code ->
                                // Got it!
                                Log.d(TAG, "Here is the authorization code! $code")
                                onAuthorizationCodeRetrieved(code)
                            } ?: run {
                                // User cancelled the login flow
                                Toast.makeText(applicationContext, "Login error",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {

        // Show Loading Indicator
        webView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        val twitchService :TwitchApiService = TwitchApiService(createHttpClient(this))
        var token: OAuthTokensResponse? = null

        lifecycleScope.launch {
            whenStarted {

                val job = GlobalScope.launch {
                    token = twitchService.getTokens(authorizationCode)
                    Log.d(TAG, "Here is the authorization code! $token")
                }
                Log.d(TAG, "Obteniedo token")

                job.join()

                Log.d(TAG, "Se acabo, continuamos para bingo")
                token?.accessToken?.let { sessionManager.saveAccessToken(it) }
                token?.refreshToken?.let { sessionManager.saveRefreshToken(it) }
                openLaunchActivity()
            }
        }

    }
}