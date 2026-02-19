package com.securetech.wallpapers.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.securetech.wallpapers.R
import com.securetech.wallpapers.databinding.ItemPreviewPagerBinding
import com.securetech.wallpapers.domain.model.Wallpaper

class PreviewPagerAdapter : RecyclerView.Adapter<PreviewPagerAdapter.PreviewViewHolder>() {

    private val wallpapers = mutableListOf<Wallpaper>()

    fun submitList(list: List<Wallpaper>) {
        wallpapers.clear()
        wallpapers.addAll(list)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Wallpaper? {
        return wallpapers.getOrNull(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val binding = ItemPreviewPagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PreviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(wallpapers[position])
    }

    override fun getItemCount(): Int = wallpapers.size

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
}
