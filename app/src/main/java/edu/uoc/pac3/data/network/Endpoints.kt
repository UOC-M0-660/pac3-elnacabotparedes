package edu.uoc.pac3.data.network

/**
 * Created by alex on 07/09/2020.
 */

//Prepare the endpoints

object Endpoints {

    // OAuth2 API Endpoints
    private const val oauthBaseUrl = "https://id.twitch.tv/oauth2"
    // TODO: Add all remaining endpoints

    const val token = "https://id.twitch.tv/oauth2/token"
    const val streams = "https://api.twitch.tv/helix/streams"
    const val user = "https://api.twitch.tv/helix/users"

    // Twitch API Endpoints
    private const val twitchBaseUrl = "https://api.twitch.tv/helix"
    // TODO: Add all remaining endpoints
}