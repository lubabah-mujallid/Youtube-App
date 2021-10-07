package com.example.youtubeapp

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker




class MainActivity : AppCompatActivity() {
    private lateinit var adapter: Recycler
    private lateinit var myList : ArrayList<Video>
    private lateinit var videoPlayerView : YouTubePlayerView
    private lateinit var player : YouTubePlayer
    private lateinit var videoTitle : TextView
    private lateinit var currentVideoID : String
    private var timeStamp = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeVideoList()
        checkInternet()
        videoPlayerView = findViewById(R.id.videoPlayer)
        videoTitle = findViewById(R.id.tvVideoTitle)

        videoPlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                player = youTubePlayer
                player.loadVideo(myList[0].id,timeStamp)
                videoTitle.text = "150-Hour Chocolate Cake"
                intializeRecycler()

                val tracker = YouTubePlayerTracker()
                player.addListener(tracker)
                currentVideoID = tracker.videoId.toString()
                timeStamp = tracker.currentSecond
            }
        })

    }

    private fun intializeRecycler() {
        adapter = Recycler(this, myList, player, videoTitle)
        rvVideo.adapter = adapter
        rvVideo.layoutManager = LinearLayoutManager(this)
    }

    private fun initializeVideoList() {
        myList = ArrayList()
        myList.add(Video("kDdUdvNQndo","Brownies | Basics with Babish"))
        myList.add(Video("rzpJWKvOmdg","150-Hour Chocolate Cake"))
        myList.add(Video("f6kzypYDLRg","Making Cinnabon Cinnamon Rolls At Home | But Better"))
        myList.add(Video("3vUtRRZG0xY","The Best Chewy Chocolate Chip Cookies"))
        myList.add(Video("23rjYgN3vuc","Brownie Cookies!"))
        myList.add(Video("6dMc3jM0YNk","Perfect Homemade Theme Park Funnel Cakes (3 Ways)"))
        myList.add(Video("aw22j8M048w","Best Ice Cream Sandwiches Completely From Scratch (2 Ways)"))
    }

    private fun checkInternet(){
        if(!connectedToInternet()){
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Internet Connection Not Found")
                .setPositiveButton("RETRY"){_, _ -> checkInternet()}
                .show()
        }
    }

    private fun connectedToInternet(): Boolean{
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            videoPlayerView.enterFullScreen()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            videoPlayerView.exitFullScreen()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("currentVideo", currentVideoID)
        outState.putFloat("timeStamp", timeStamp)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        currentVideoID = savedInstanceState.getString("currentVideo").toString()
        timeStamp = savedInstanceState.getFloat("timeStamp")
    }
}
