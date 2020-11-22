package edu.uoc.pac3.data.network

/**
 * Created by alex on 07/09/2020.
 */
object Endpoints {

    // OAuth2 API Endpoints
    private const val oauthBaseUrl = "https://id.twitch.tv/oauth2"
    // TODO: Add all remaining endpoints

    public const val token = "https://id.twitch.tv/oauth2/token"
    public const val streams = "https://api.twitch.tv/helix/streams"

    // Twitch API Endpoints
    private const val twitchBaseUrl = "https://api.twitch.tv/helix"
    // TODO: Add all remaining endpoints
}