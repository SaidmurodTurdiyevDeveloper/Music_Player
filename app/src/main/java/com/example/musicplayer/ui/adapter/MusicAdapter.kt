package com.example.musicplayer.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.service.ServiceHelper
import com.example.musicplayer.zZz__utilites.extencions.getDurationText
import com.example.musicplayer.zZz__utilites.extencions.inflate
import com.gauravk.audiovisualizer.visualizer.BarVisualizer
import timber.log.Timber

class MusicAdapter : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    private var differList = AsyncListDiffer(this, ITEM_DIFF)
    private var listener: ((MusicDataStoradge) -> Unit)? = null
    private var listenerMenu: ((MusicDataStoradge, View) -> Unit)? = null

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<MusicDataStoradge>() {
            override fun areItemsTheSame(oldItem: MusicDataStoradge, newItem: MusicDataStoradge): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MusicDataStoradge,
                newItem: MusicDataStoradge
            ): Boolean =
                oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.artist == newItem.artist && oldItem.favourute == newItem.favourute
        }
    }

    fun setListener(block: (MusicDataStoradge) -> Unit) {
        listener = block
    }

    fun submitList(list: List<MusicDataStoradge>) {
        differList.submitList(list)
    }

    fun submitMenu(block: (MusicDataStoradge, View) -> Unit) {
        listenerMenu = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = parent.inflate(R.layout.item_music)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = differList.currentList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                listener?.invoke(differList.currentList[absoluteAdapterPosition])
            }
            itemView.setOnLongClickListener {
                listenerMenu?.invoke(differList.currentList[absoluteAdapterPosition], itemView)
                return@setOnLongClickListener true
            }
        }

        @SuppressLint("ResourceType")
        fun bind() {
            val d = differList.currentList[bindingAdapterPosition]
            val image = itemView.findViewById<ImageView>(R.id.image_music_item)
            try {
                val uri = d.imageUri
                if (uri == null) {
                    Glide.with(itemView.context).load(R.drawable.music_image).into(image)
                } else {
                    Glide.with(itemView.context).load(uri).centerCrop()
                        .placeholder(R.drawable.music_image).error(R.drawable.ic_music).into(image)
                }
            } catch (e: Exception) {
                Glide.with(itemView.context).load(R.drawable.ic_music).into(image)
            }
            try {
                val blast = itemView.findViewById<BarVisualizer>(R.id.blast)
                if (ConstantMusic.getCurrentMusic().id == d.id) {
                    val id = ServiceHelper.getMusicPlayer()?.audioSessionId ?: -1
                    if (id != -1 && !blast.isActivated) {
                        blast.setAudioSessionId(id)
                        blast.show()
                    }
                } else {
                    blast.release()
                    blast.hide()
                }

            } catch (e: Exception) {
                Timber.d("Xatolik " + e.message)
            }
            itemView.findViewById<TextView>(R.id.textMusicItem).text = d.title
            itemView.findViewById<TextView>(R.id.textArtistItem).text = d.artist
            itemView.findViewById<TextView>(R.id.textTimeItem).text = d.duration?.getDurationText()
        }
    }
}