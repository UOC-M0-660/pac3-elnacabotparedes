package edu.uoc.pac3.data.oauth

/**
 * Created by alex on 07/09/2020.
 */

//Constants for the OAuth

object OAuthConstants {

    // TODO: Set OAuth2 Variables
    val clientID = "r3a61otu0cssqb3wck6hmd9v4xnqdi"
    val redirectUri = "http://localhost"
    val clientSecret = "kig9c9wt3xx3jfupve3h7bpboae70y"
    val scopes = listOf("user", "read", "email user", "edit")
    val authorizationUrl = "https://id.twitch.tv/oauth2/authorize"

}