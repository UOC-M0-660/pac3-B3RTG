package edu.uoc.pac3.twitch.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.twitch.profile.ProfileActivity
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"
    private lateinit var adapter: StreamsListAdapter
    private var cursor: String = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)


        // Init RecyclerView
        initRecyclerView()
        //get first load
        getStreamData()

    }
    private fun getStreamData()
    {
        val sm = SessionManager(this)
        lifecycleScope.launch {
            whenStarted {
                try {
                    var streams = getStreamDataAsync()
                    cursor = streams?.pagination?.cursor!!
                    Log.d(TAG, "Next currsor $cursor")
                    Log.d(TAG, "Set streams to adapter")
                    adapter.setStreams(streams?.data!!)
                } catch (e: UnauthorizedException) {
                    Log.d(TAG, "Unauthorized, Try refresh token")
                    var response= getRefreshTokenAsync()

                    sm.saveAccessToken(response.accessToken)
                    sm.saveRefreshToken(response.refreshToken.toString())
                    //Volver a pedir los streams
                    var streams = getStreamDataAsync()
                    cursor = streams?.pagination?.cursor!!
                    adapter.setStreams(streams?.data!!)
                }

            }
        }
    }

    private suspend fun getStreamDataAsync(): StreamsResponse? {
        val ts = TwitchApiService(Network.createHttpClient(this))
        val sm = SessionManager(this)

        return ts.getStreams(cursor)
    }

    private suspend fun getRefreshTokenAsync(): OAuthTokensResponse {
        val ts = TwitchApiService(Network.createHttpClient(this))
        val sm = SessionManager(this)

        return ts.refreshToken(sm.getRefreshToken().toString())
    }

    private fun initRecyclerView() {

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

        //Set layout manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager

        //Init Adapter
        adapter = StreamsListAdapter(emptyList())
        recyclerview.adapter = adapter

        //scroll listener
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !recyclerView.canScrollVertically(1)) {
                    Log.i(TAG, "FETCH NEW DATA");
                    getStreamData()
                } /*else if (dy < 0) {
                    Log.i(TAG, "SCROLLING UP");
                }*/
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.profile_item -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
