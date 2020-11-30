package edu.uoc.pac3.twitch.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.twitch.profile.ProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var recycleView: RecyclerView
    private lateinit var viewAdapter: StreamCardAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var sessionManager: SessionManager
    private lateinit var  twitchApiService: TwitchApiService
    private var stream_list: MutableList<Stream>? = null

    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var firstVisbileItem: Int = 0

    private var cursor: String? = null
    private var failRefresh: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView

        //Init variables
        init()

        // TODO: Get Streams
        getStreams(sessionManager.getAccessToken())

        //Init recycleview
        stream_list?.let { viewAdapter.setStreams(it) }
        initRecyclerView()

        //Add Scroll Listner
        ScrollListener()
    }

    //Ask the petition to recive streams
    fun getStreams(accessToken: String?)
    {
        lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                var streams: StreamsResponse? = null
                if( cursor == null)
                {
                    streams = accessToken?.let { twitchApiService.getStreams(it) }

                    if (streams != null) {
                        stream_list = streams.data as ArrayList<Stream>?
                    }
                }
                else
                {
                    streams = accessToken?.let { twitchApiService.getStreams(it, cursor) }
                    if (streams != null) {
                        AddToList(streams)
                    }
                }
                cursor = streams?.pagination?.cursor
                withContext(Dispatchers.Main)
                {
                    //Refresh the list when obtained
                    stream_list?.let { viewAdapter.setStreams(it) }
                }

            }
            catch (t: Throwable)
            {

                when(t)
                {
                    UnauthorizedException ->
                        LoadWithRefreshToken()
                }
            }

        }
    }

    //Ask the petition but now with refresh token
    private fun LoadWithRefreshToken()
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
                            getStreams(response.accessToken);
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

    //Go to login the refresh token fail
    private fun gotoLogin()
    {
        sessionManager.clearRefreshToken()
        sessionManager.clearAccessToken()
        startActivity(Intent(this, LaunchActivity::class.java))
    }

    //Detect when the recycleview is in the bottom and make a petition for 20 more streams
    private fun ScrollListener()
    {
        recycleView.addOnScrollListener(object: RecyclerView.OnScrollListener()
        {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    //Detect the bottom of the ScrollView

                    lifecycleScope.launch(Dispatchers.IO)
                    {

                        getStreams(sessionManager.getAccessToken())

                    }

                }

            }

        })
    }

    //Add new streams to the list
    private fun AddToList(streams: StreamsResponse)
    {

        if( streams.data != null)
        {
            for ( stream in streams.data)
            {
                stream_list?.add(stream)
            }

        }
    }

    //Init all the parameters
    private fun init()
    {
        viewAdapter = StreamCardAdapter(this)
        recycleView = findViewById(R.id.recyclerView)
        viewManager = LinearLayoutManager(this)

        twitchApiService = TwitchApiService(Network.createHttpClient(applicationContext))
        sessionManager = SessionManager(applicationContext)
    }

    //Init the recycleview
    private fun initRecyclerView() {
        // TODO: Implement

        viewManager = LinearLayoutManager(this)

        recycleView.layoutManager = viewManager

        recycleView.adapter = viewAdapter

    }

    //Create the menu in the top
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    //Detect if the user button is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId)
        {
            R.id.user_profile ->
            {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}