package com.securetech.wallpapers.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.securetech.wallpapers.R
import com.securetech.wallpapers.databinding.ItemPreviewPagerBinding
import com.securetech.wallpapers.domain.model.Wallpaper

class PreviewPagerAdapter : ListAdapter<Wallpaper, PreviewPagerAdapter.PreviewViewHolder>(
    PreviewDiffCallback()
) {

    fun getItemOrNull(position: Int): Wallpaper? {
        return if (position in 0 until itemCount) getItem(position) else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val binding = ItemPreviewPagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PreviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PreviewViewHolder(
        private val binding: ItemPreviewPagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(wallpaper: Wallpaper) {
            binding.imageViewFullPreview.load(wallpaper.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_wallpaper)
                error(R.drawable.placeholder_wallpaper)
            }
        }
    }

    private class PreviewDiffCallback : DiffUtil.ItemCallback<Wallpaper>() {
        override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
            oldItem == newItem
    }
}
