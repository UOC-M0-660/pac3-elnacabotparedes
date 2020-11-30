package edu.uoc.pac3.twitch.streams

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uoc.pac3.R
import edu.uoc.pac3.data.streams.Stream


//Adapter for the recycleview in the Stream activity
class StreamCardAdapter(context: Context) :
    RecyclerView.Adapter<StreamCardAdapter.MyViewHolder>() {

    private var streams: List<Stream>? = null
    private var context: Context = context

    fun setStreams(streams: List<Stream>)
    {
        this.streams = streams
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // TODO("Not yet implemented")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stream_card, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    {
        val title: TextView = view.findViewById(R.id.title_stream)
        val thumnail: ImageView = view.findViewById(R.id.thumnail_stream)
        val author: TextView = view.findViewById(R.id.author_stream)

    }

    //Show the streaming information
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //TODO("Not yet implemented")

        holder.title.text = streams?.get(position)?.title
        holder.author.text = streams?.get(position)?.userName

        Glide.with(context)
            .load(streams?.get(position)?.thumbnail_url?.let { imageSizeReplace(it) })
            .into(holder.thumnail)

    }

    //Make a resize of the image
    private fun imageSizeReplace(image: String): String? {
        var new_image = image.replace("{width}", "1920").replace("{height}", "1080")
        return new_image
    }

    override fun getItemCount(): Int {
        //TODO("Not yet implemented")

         streams?.count()?.let {
             return it
         }
        return 0
    }


}