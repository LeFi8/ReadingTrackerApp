package com.example.readingtrackerapp.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.readingtrackerapp.R
import com.example.readingtrackerapp.data.BookDB
import com.example.readingtrackerapp.databinding.ListItemBinding
import com.example.readingtrackerapp.model.Book
import kotlin.concurrent.thread

class BookViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book) {
        binding.id.text = book.id.toString()
        binding.bookTitle.text = book.title
        binding.readingStatus.text = binding.root.context.getString(R.string.status, book.status)
        binding.readingProgress.text = binding.root.context.getString(R.string.page, book.currentPage, book.maxPages)
        binding.image.setImageResource(book.resId)
        binding.image.tag = book.resId // useful for removing
        val progress: Double = book.currentPage.toDouble() / book.maxPages.toDouble() * 100
        binding.progressBar.progress = progress.toInt()
    }

    fun getBinding() = binding
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

        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(data[position])
        val binding = holder.getBinding()

        binding.root.setOnLongClickListener {
            val context = binding.root.context
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialog.setTitle(
                binding.root.resources.getString(
                    R.string.removing_are_you_sure,
                    data[position].title
                )
            )
            alertDialog.setPositiveButton(binding.root.resources.getString(R.string.yes)) { _, _ ->
                thread {
                    val selectedBook = BookDB.open(context).books.getBook(data[position].id)
                    BookDB.open(context).books.removeBook(selectedBook)
                    refresh(context)
                }
                Toast.makeText(
                    context,
                    binding.root.resources.getString(R.string.removed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            alertDialog.setNegativeButton(binding.root.resources.getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()

            true
        }

    }

    @SuppressLint("NotifyDataSetChanged", "DiscouragedApi")
    fun refresh(context: Context){
        val books = BookDB.open(context).books.getAll().map {
            Book(
                it.id,
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

    @SuppressLint("NotifyDataSetChanged")
    fun sort() {
        data.sortBy {
            it.currentPage / it.maxPages * 100
        }
        handler.post {
            notifyDataSetChanged()
        }
    }


}