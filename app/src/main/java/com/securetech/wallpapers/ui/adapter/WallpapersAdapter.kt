package com.securetech.wallpapers.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.securetech.wallpapers.R
import com.securetech.wallpapers.databinding.ItemWallpaperBinding
import com.securetech.wallpapers.domain.model.Wallpaper

class WallpapersAdapter(
    private val onWallpaperClick: (Wallpaper) -> Unit
) : ListAdapter<Wallpaper, WallpapersAdapter.WallpaperViewHolder>(WallpaperDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val binding = ItemWallpaperBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WallpaperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WallpaperViewHolder(
        private val binding: ItemWallpaperBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(wallpaper: Wallpaper) {
            binding.imageViewWallpaper.load(wallpaper.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_wallpaper)
                error(R.drawable.placeholder_wallpaper)
            }
            binding.root.setOnClickListener {
                onWallpaperClick(wallpaper)
            }
        }
    }

    private class WallpaperDiffCallback : DiffUtil.ItemCallback<Wallpaper>() {
        override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
            oldItem == newItem
    }
}
