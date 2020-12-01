package edu.uoc.pac3.data.oauth

/**
 * Created by alex on 07/09/2020.
 * Modified by albert on 01/12/2020
 */
object OAuthConstants {

    const val redirectUri :String = "http://localhost"
    const val clientId :String = "d4reov7basujhvgjo7ch5pjtdb5lfm"
    const val twitchUri :String = "https://id.twitch.tv/oauth2/authorize"
    const val clientSecret :String = "c0h2hyv99dn5csvx9satbjtdxzqbor"

    //this variable may not be saved there
    val scopes = listOf<String>("user:read:email","user:edit")

}