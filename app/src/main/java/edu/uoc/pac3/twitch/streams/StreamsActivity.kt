package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"
    private lateinit var adapter: StreamsListAdapter


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)


        // Init RecyclerView
        initRecyclerView()


        // TODO: Get Streams
        val ts = TwitchApiService(Network.createHttpClient(this))
        val sm = SessionManager(this)

       lifecycleScope.launch {
            whenStarted {

                var streams = ts.getStreams(sm.getAccessToken().toString())
                Log.d(TAG, "Set streams to adapter")
                adapter.setBooks(streams?.data!!)
            }
        }


    }

    private fun initRecyclerView() {

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

        //Set layout manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager

        //Init Adapter
        adapter = StreamsListAdapter(emptyList())
        recyclerview.adapter = adapter
    }

}
