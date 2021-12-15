package com.example.musicplayer.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.zZz__utilites.extencions.inflate

class AlbomAdapter : RecyclerView.Adapter<AlbomAdapter.ViewHolder>() {
    private var differList = AsyncListDiffer(this, ITEM_DIFF)
    private var listener: ((EntityAlboms) -> Unit)? = null
    private var listenerMore: ((EntityAlboms,View) -> Unit)? = null


    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<EntityAlboms>() {
            override fun areItemsTheSame(oldItem: EntityAlboms, newItem: EntityAlboms): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: EntityAlboms,
                newItem: EntityAlboms
            ): Boolean =
                oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.image == newItem.image
        }
    }

    fun setListener(block: (EntityAlboms) -> Unit) {
        listener = block
    }

    fun setListenerMore(block: (EntityAlboms,View) -> Unit) {
        listenerMore = block
    }

    fun submitList(list: List<EntityAlboms>) {
        differList.submitList(list.toMutableList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = parent.inflate(R.layout.item_albom)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = differList.currentList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener { listener?.invoke(differList.currentList[absoluteAdapterPosition]) }
            itemView.findViewById<Button>(R.id.moreSettingAlbomItem).setOnClickListener {
                listenerMore?.invoke(differList.currentList[absoluteAdapterPosition],it)
            }
        }
        fun bind() {
            val d=differList.currentList[bindingAdapterPosition]
            try {
            itemView.findViewById<ImageView>(R.id.albomImageItem).setBackgroundResource(d.image)
            }catch (e:Exception){
                itemView.findViewById<ImageView>(R.id.albomImageItem).setBackgroundResource(R.drawable.naushnik_albom)
            }
            itemView.findViewById<TextView>(R.id.textAlbomTextItem).text=d.name
        }
    }
}