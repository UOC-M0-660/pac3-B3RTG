package edu.uoc.pac3.twitch.streams

import androidx.recyclerview.widget.DiffUtil
import edu.uoc.pac3.data.streams.Stream

class StreamsDiffCallback(internal var newStreamsList: List<Stream>, internal var oldStreamList: List<Stream>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldStreamList.size
    }

    override fun getNewListSize(): Int {
        return newStreamsList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldObject = oldStreamList[oldItemPosition]
        val newObject = newStreamsList[newItemPosition]
        var itemsSame = false

        if (oldObject.id.equals(newObject.userName, ignoreCase = true)) {
            itemsSame = true
        }
        return itemsSame
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStreamList[oldItemPosition] == newStreamsList[newItemPosition]
    }

}