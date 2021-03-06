package edu.uoc.pac3.data.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Created by alex on 07/09/2020.
 */

//Prepare the petition of the stream

@Serializable
data class Stream(
        @SerialName("user_name") val userName: String? = null,
        @SerialName("title") val title: String? = null,
        @SerialName("thumbnail_url") val thumbnail_url: String? = null,
)

@Serializable
data class Cursor(
        @SerialName("cursor") val cursor: String? = null,
)

@Serializable
data class StreamsResponse(
        val data: List<Stream>? = null,
        val pagination: Cursor? = null
)