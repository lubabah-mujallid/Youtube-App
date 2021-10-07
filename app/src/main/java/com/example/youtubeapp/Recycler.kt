package com.example.youtubeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.android.synthetic.main.item_row.view.*

class Recycler(val context: Context, val videoList: ArrayList<Video>, val player: YouTubePlayer, val tv: TextView): RecyclerView.Adapter<Recycler.ViewHolder>(){
    var currentVideoPosition : Int = 0
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val button: Button = itemView.rvButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTitle = videoList[position].title
        val currentID = videoList[position].id
        holder.itemView.apply {
            holder.button.text = currentTitle
            holder.button.setOnClickListener {
                player.loadVideo(currentID, 0f)
                tv.text = currentTitle
                currentVideoPosition = position
            }
        }

    }

    override fun getItemCount(): Int = videoList.size

    fun getVideoPosition(): Int { return currentVideoPosition }
    fun getVideoTimeStamp() {}
}
