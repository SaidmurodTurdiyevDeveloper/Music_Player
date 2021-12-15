package com.example.musicplayer.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.HelperData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.zZz__utilites.extencions.getDurationText
import com.example.musicplayer.zZz__utilites.extencions.inflate

class CheckAdapter : RecyclerView.Adapter<CheckAdapter.ViewHolder>() {
    private var differList = AsyncListDiffer(this, ITEM_DIFF)
    private var listener: ((MusicDataStoradge, Int) -> Unit)? = null

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<HelperData<Boolean, MusicDataStoradge>>() {
            override fun areItemsTheSame(oldItem: HelperData<Boolean, MusicDataStoradge>, newItem: HelperData<Boolean, MusicDataStoradge>): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: HelperData<Boolean, MusicDataStoradge>,
                newItem: HelperData<Boolean, MusicDataStoradge>
            ): Boolean =
                oldItem.element == newItem.element && oldItem.data == newItem.data
        }
    }

    fun setListener(block: (MusicDataStoradge, Int) -> Unit) {
        listener = block
    }


    fun submitList(list: List<HelperData<Boolean, MusicDataStoradge>>) {
        differList.submitList(list.toMutableList())
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
                listener?.invoke(differList.currentList[absoluteAdapterPosition].data, absoluteAdapterPosition)
            }
        }

        @SuppressLint("ResourceType")
        fun bind() {
            val d = differList.currentList[bindingAdapterPosition]
            val image = itemView.findViewById<ImageView>(R.id.image_music_item)
            try {
                val uri = d.data.imageUri
                if (uri == null) {
                    Glide.with(itemView.context).load(R.drawable.music_image).into(image)
                } else {
                    Glide.with(itemView.context).load(uri).centerCrop().placeholder(R.drawable.music_image).error(R.drawable.ic_music).into(image)
                }
            } catch (e: Exception) {
                Glide.with(itemView.context).load(R.drawable.ic_music).into(image)
            }
            if (d.element)
                itemView.findViewById<LinearLayout>(R.id.itemBackground).setBackgroundResource(R.drawable.stroce_view)
            else
                itemView.findViewById<LinearLayout>(R.id.itemBackground).setBackgroundResource(R.drawable.stroke_check)
            itemView.findViewById<TextView>(R.id.textMusicItem).text = d.data.title
            itemView.findViewById<TextView>(R.id.textTimeItem).text = d.data.duration?.getDurationText()
        }
    }
}