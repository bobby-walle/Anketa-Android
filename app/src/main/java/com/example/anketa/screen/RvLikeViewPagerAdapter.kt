package com.example.anketa.screen

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anketa.data.ResourceType
import com.example.anketa.databinding.ItemRvViewpagerBinding

class RvLikeViewPagerAdapter(resources: Array<Pair<Int, ResourceType>>, val packageName: String) :
    RecyclerView.Adapter<RvLikeViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRvViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root)


    var arrayOfResources = resources
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvLikeViewPagerAdapter.ViewHolder {
        val binding =
            ItemRvViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RvLikeViewPagerAdapter.ViewHolder, position: Int) {
        val resource = arrayOfResources[position]
        with(holder.binding) {
            holder.setIsRecyclable(false)
            when (resource.second) {
                ResourceType.Drawable -> {
                    View.GONE.let {
                        video.visibility = it
                        btnPause.visibility = it
                        btnPlay.visibility = it
                    }
                    initImage(this, holder.itemView.context, resource.first)
                }
                ResourceType.Raw -> {
                    image.visibility = View.GONE
                    initVideo(this, resource.first)
                }
            }
        }
    }

    private fun initVideo(binding: ItemRvViewpagerBinding, videoId: Int) {
        with(binding) {
            video.setVideoURI(
                Uri.parse(
                    "android.resource://"
                            + packageName + "/" + videoId
                )
            )
//            val mediaController = MediaController(holder.itemView.context).apply {
//                setAnchorView(video)
//            }
//            video.setMediaController(mediaController)
//            video.requestFocus()
            video.seekTo(1)
            video.setOnPreparedListener {
                it.isLooping = true
//                it.setVolume(0F, 0F)
            }

            fun onClick() {
                if (video.isPlaying) {
                    video.stopPlayback()
                    btnPlay.visibility = View.VISIBLE
                    btnPause.visibility = View.INVISIBLE
                } else {
                    btnPause.visibility = View.VISIBLE
                    btnPlay.visibility = View.INVISIBLE
                    video.start()
                    video.resume()
                }
            }

            video.setOnClickListener {
                onClick()
            }
        }
    }


    private fun initImage(binding: ItemRvViewpagerBinding, context: Context, imgID: Int) {
        with(binding) {
            image.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    imgID,
                    null
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return arrayOfResources.size
    }
}
