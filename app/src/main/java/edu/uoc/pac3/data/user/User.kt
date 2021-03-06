package edu.uoc.pac3.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Created by alex on 07/09/2020.
 */

//Prepare the information from the user

@Serializable
data class User(
        @SerialName("display_name")val userName: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("profile_image_url") val profile_image: String? = null,
        @SerialName("view_count") val view_count: Int? = null,
)

@Serializable
data class Users(
        val data: List<User>? = null,
)