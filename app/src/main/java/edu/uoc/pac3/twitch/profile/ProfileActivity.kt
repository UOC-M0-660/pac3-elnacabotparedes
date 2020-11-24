package edu.uoc.pac3.twitch.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.Stream
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    private lateinit var sessionManager: SessionManager
    private lateinit var  twitchApiService: TwitchApiService

    private var userName: String? = null
    private var description: String? = null
    private var profile_image_url: String? = null
    private var viewers: Int? = null

    private var failRefresh: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        twitchApiService = TwitchApiService(Network.createHttpClient(applicationContext))
        sessionManager = SessionManager(applicationContext)

        getProfileDetails(sessionManager.getAccessToken());

        updateDescriptionButton.setOnClickListener {
            updateDescription(sessionManager.getAccessToken())
        }

        logout()

    }

    private fun getProfileDetails(token: String?)
    {
        lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                val user = token?.let { twitchApiService.getUser(it) }
                if (user != null) {
                    userName = user.data?.get(0)?.userName
                    description = user.data?.get(0)?.description
                    profile_image_url = user.data?.get(0)?.profile_image
                    viewers = user.data?.get(0)?.view_count
                }
                withContext(Dispatchers.Main)
                {
                    updateUserInfo()
                }
            }catch (t: Throwable)
            {
                when(t)
                {
                    UnauthorizedException ->
                        LoadDataWithRefreshToken("details")
                }
            }

        }
    }

    private fun LoadDataWithRefreshToken(petition: String)
    {
        if(sessionManager.getAccessToken() != null)
        {
            Log.d("OAuth", "Error with Access Token")

            sessionManager.clearAccessToken()
            var refreshToken = sessionManager.getRefreshToken()
            lifecycleScope.launch(Dispatchers.IO)
            {
                try {

                    val response = twitchApiService.getAccessToken(refreshToken)
                    if (response != null) {
                        response.accessToken?.let { sessionManager.saveAccessToken(it) }
                        response.refreshToken?.let { sessionManager.saveRefreshToken(it) }
                        withContext(Dispatchers.Main)
                        {

                            when(petition)
                            {
                                "details" ->
                                {
                                    getProfileDetails(response.accessToken)
                                }
                                "update" ->
                                {
                                    updateDescription(response.accessToken)
                                }
                            }
                        }
                    }
                }catch (t: Throwable)
                {
                    when(t)
                    {
                        UnauthorizedException->
                            failRefresh = true
                    }
                }

                withContext(Dispatchers.IO)
                {
                    if(failRefresh == true)
                    {
                        Log.d("OAuth", "Fail with refresh token")
                        gotoLogin()
                    }
                }
            }

        }
    }

    private fun gotoLogin()
    {
        startActivity(Intent(this, LaunchActivity::class.java))
    }

    private fun updateDescription(token: String?)
    {
        var newDescription = userDescriptionEditText.text.toString()
        lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                val newDescription = twitchApiService.updateUserDescription(token, newDescription)
                withContext(Dispatchers.Main)
                {
                    showMessageDescription()
                }
            }
            catch (t: Throwable)
            {
                when(t)
                {
                    UnauthorizedException ->
                        LoadDataWithRefreshToken("update")
                }
            }
        }
    }


    private fun logout()
    {
        logoutButton.setOnClickListener{
            sessionManager.clearAccessToken()
            sessionManager.clearAccessToken()
            startActivity(Intent(this, LaunchActivity::class.java))
        }
    }

    private fun showMessageDescription()
    {
        Toast.makeText(this, "Description Updated", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserInfo()
    {
        if(userName != null)
        {
            userNameTextView.text = userName
        }
        if( description != null)
        {
            userDescriptionEditText.setText(description)
        }

        if(profile_image_url != null)
        {
            Glide.with(this)
                    .load(profile_image_url)
                    .into(imageView)
        }

        if(viewers != null)
        {
            viewsText.text = viewers.toString()
        }
    }
}