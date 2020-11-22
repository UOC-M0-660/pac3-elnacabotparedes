package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.streams.Stream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var recycleView: RecyclerView
    private lateinit var viewAdapter: StreamCardAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var stream_list: List<Stream>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView

        viewAdapter = StreamCardAdapter(this)

        // TODO: Get Streams

        val twitchApiService = TwitchApiService(Network.createHttpClient(applicationContext))

        val sessionManager = SessionManager(applicationContext)

        val accessToken = sessionManager.getAccessToken()
        Log.d("OAuth", accessToken)


        lifecycleScope.launch(Dispatchers.IO)
        {
            val accessToken = sessionManager.getAccessToken()
            val streams = accessToken?.let { twitchApiService.getStreams(it) }
            if (streams != null) {
                stream_list = streams.data
            }

            Log.d("OAuth", streams?.data?.get(0)?.thumbnail_url)

            withContext(Dispatchers.Main)
            {
                stream_list?.let { viewAdapter.setStreams(it) }
            }
        }

        stream_list?.let { viewAdapter.setStreams(it) }
        initRecyclerView()

    }


    private fun initRecyclerView() {
        // TODO: Implement

        viewManager = LinearLayoutManager(this)

        recycleView = findViewById(R.id.recyclerView)
        recycleView.layoutManager = viewManager

        recycleView.adapter = viewAdapter

    }

}