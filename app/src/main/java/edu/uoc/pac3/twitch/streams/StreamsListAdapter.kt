package edu.uoc.pac3.twitch.streams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uoc.pac3.R
import edu.uoc.pac3.data.streams.Stream


class StreamsListAdapter(private var streams: List<Stream>) : RecyclerView.Adapter<StreamsListAdapter.ViewHolder>() {

    private fun getStream(position: Int): Stream {
        return streams[position]
    }

    fun setStreams(newStreams: List<Stream>) {

        //val diffResult = DiffUtil.calculateDiff(StreamsDiffCallback(newStreams,this.streams))
        //diffResult.dispatchUpdatesTo(this)

        // Reloads the RecyclerView with new adapter data
        this.streams = newStreams
        notifyDataSetChanged()

    }

    // Returns total items in Adapter
    override fun getItemCount(): Int {
        return streams.size
    }

    // Creates View Holder for re-use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_stream_list_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = getStream(position)
        holder.titleView.text = stream.title
        holder.authorView.text = stream.userName
        val imageSize: String = R.dimen.stream_item_image_width_small.toString() + "x" + R.dimen.stream_item_image_height.toString()
        Glide.with(holder.view).load(stream.thumbnailUrl?.replace("{width}x{height}",imageSize)).into(holder.thumbView);
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.stream_title)
        val authorView: TextView = view.findViewById(R.id.stream_user)
        val thumbView: ImageView = view.findViewById(R.id.stream_thumbnail)
    }

}