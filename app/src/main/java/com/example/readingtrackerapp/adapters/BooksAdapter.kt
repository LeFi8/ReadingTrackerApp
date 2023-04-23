package com.example.readingtrackerapp.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.readingtrackerapp.R
import com.example.readingtrackerapp.data.BookDB
import com.example.readingtrackerapp.data.model.BookEntity
import com.example.readingtrackerapp.databinding.ListItemBinding
import com.example.readingtrackerapp.model.Book
import kotlin.concurrent.thread

class BookViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book) {
        binding.bookTitle.text = book.title
        binding.readingStatus.text = binding.root.context.getString(R.string.status, book.status)
        binding.readingProgress.text = binding.root.context.getString(R.string.page, book.currentPage, book.maxPages)
        binding.image.setImageResource(book.resId)
        binding.image.tag = book.resId // useful for removing
        val progress: Double = book.currentPage.toDouble() / book.maxPages.toDouble() * 100
        binding.progressBar.progress = progress.toInt()
    }
}

class BooksAdapter : RecyclerView.Adapter<BookViewHolder>() {

    private val data = mutableListOf<Book>()
    private val handler: android.os.Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // TODO: fix removing, would be better if it checks id
        binding.root.setOnLongClickListener {
            val selectedBook = BookEntity(
                title = binding.bookTitle.text.toString(),
                status = binding.readingStatus.text.toString(),
                currentPage = binding.readingProgress.text.toString()
                    .replace(binding.root.resources.getString(R.string.page_match_to_replace), "")
                    .split("/")[0]
                    .trim()
                    .toInt(),
                maxPage = binding.readingProgress.text.toString().split("/")[1].toInt(),
                icon = parent.context.resources.getResourceName(binding.image.tag.toString().toInt())
            )

            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(parent.context)
            alertDialog.setTitle(
                parent.resources.getString(
                    R.string.removing_are_you_sure,
                    selectedBook.title
                )
            )
            alertDialog.setPositiveButton(parent.resources.getString(R.string.yes)) { _, _ ->
                thread {
                    BookDB.open(parent.context).books.removeBook(selectedBook)
                    refresh(parent.context)
                }
                Toast.makeText(
                    parent.context,
                    parent.resources.getString(R.string.removed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            alertDialog.setNegativeButton(parent.resources.getString(R.string.no)) { _, _ -> {} }
            alertDialog.show()

            true
        }

        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(context: Context){
        val books = BookDB.open(context).books.getAll().map {
            Book(
                it.title,
                it.status,
                it.currentPage,
                it.maxPage,
                context.resources.getIdentifier(it.icon, "drawable", context.packageName)
            )
        }
        data.clear()
        data.addAll(books)

        handler.post {
            notifyDataSetChanged()
        }
    }


}