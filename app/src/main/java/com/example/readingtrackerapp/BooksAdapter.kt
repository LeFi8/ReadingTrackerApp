package com.example.readingtrackerapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readingtrackerapp.databinding.ListItemBinding

class BookViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book) {
        binding.bookTitle.text = book.title
        binding.readingStatus.text = binding.root.context.getString(R.string.status, book.status)
        binding.currentReadingPage.text = binding.root.context.getString(R.string.page, book.currentPage)
        binding.image.setImageResource(book.resId)
    }
}

class BooksAdapter : RecyclerView.Adapter<BookViewHolder>() {

    private val data = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun replace(newData: List<Book>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}