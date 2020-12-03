package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
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
        val ts = TwitchApiService(Network.createHttpClient(this))
        val sm = SessionManager(this)

        lifecycleScope.launch {
            whenStarted {

                var streams = ts.getStreams(cursor)
                cursor = streams?.pagination?.cursor!!

                Log.d(TAG, "Next currsor $cursor")
                Log.d(TAG, "Set streams to adapter")
                adapter.setStreams(streams?.data!!)
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

}
