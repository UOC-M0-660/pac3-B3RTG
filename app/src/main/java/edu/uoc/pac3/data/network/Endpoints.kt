package edu.uoc.pac3.data.network

/**
 * Created by alex on 07/09/2020.
 * Modified by albert on 01/12/2020
 */
object Endpoints {

    // OAuth2 API Endpoints
    private const val oauthBaseUrl = "https://id.twitch.tv/oauth2"
    const val oauthAuthorizationUrl = "$oauthBaseUrl/authorize"
    const val oauthTokensUrl = "$oauthBaseUrl/token"
    const val oauthRevokeUrl = "$oauthBaseUrl/revoke"

    // Twitch API Endpoints
    private const val twitchBaseUrl = "https://api.twitch.tv/helix"
    const val twitchStreamsUrl ="$twitchBaseUrl/streams"
    const val twitchUserUrl ="$twitchBaseUrl/users"
}