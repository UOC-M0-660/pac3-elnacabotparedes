package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        // TODO: Get Streams

        val twitchApiService = TwitchApiService(Network.createHttpClient(applicationContext))

        val sessionManager = SessionManager(applicationContext)

        val accessToken = sessionManager.getAccessToken()
        Log.d("OAuth", accessToken)


        lifecycleScope.launch(Dispatchers.IO)
        {
            val accessToken = sessionManager.getAccessToken()
            val streams = accessToken?.let { twitchApiService.getStreams(it) }
            Log.d("OAuth", streams.toString())
        }

    }

    private fun initRecyclerView() {
        // TODO: Implement

    }

}