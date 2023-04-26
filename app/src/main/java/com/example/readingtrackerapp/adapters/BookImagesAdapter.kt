package com.example.readingtrackerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readingtrackerapp.R
import com.example.readingtrackerapp.databinding.BookImageBinding

class BookImageViewHolder(private val binding: BookImageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(resId: Int, isSelected: Boolean, editView: Boolean, selectedPos: Int) {
        binding.image.setImageResource(resId)

        if (editView && selectedPos == resId)
            binding.selected.visibility = View.VISIBLE
        else
            binding.selected.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

class BookImagesAdapter(private val editView: Boolean, iconId: Int = 0) : RecyclerView.Adapter<BookImageViewHolder>() {

    private val images = listOf(R.drawable.book, R.drawable.manga)
    private var selectedPos: Int = iconId
    val selectedResId: Int
        get() = images[selectedPos]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookImageViewHolder {

        val binding = BookImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookImageViewHolder(binding).also { viewHolder ->
            binding.root.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = viewHolder.layoutPosition
                notifyItemChanged(selectedPos)
            }
        }
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: BookImageViewHolder, position: Int) {
        holder.bind(images[position], selectedPos == position, editView, selectedPos)
    }
}