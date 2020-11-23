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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        twitchApiService = TwitchApiService(Network.createHttpClient(applicationContext))
        sessionManager = SessionManager(applicationContext)

        lifecycleScope.launch(Dispatchers.IO)
        {
            val accessToken = sessionManager.getAccessToken()
            val user = accessToken?.let { twitchApiService.getUser(it) }
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
        }

        updateDescription()
        logout()

    }

    private fun updateDescription()
    {
        updateDescriptionButton.setOnClickListener {
            var newDescription = userDescriptionEditText.text.toString()
            lifecycleScope.launch(Dispatchers.IO)
            {
                val accessToken = sessionManager.getAccessToken()
                val newDescription = twitchApiService.updateUserDescription(accessToken, newDescription)
                withContext(Dispatchers.Main)
                {
                    showMessageDescription()
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