package com.example.readingtrackerapp.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.readingtrackerapp.BookCallback
import com.example.readingtrackerapp.Navigable
import com.example.readingtrackerapp.R
import com.example.readingtrackerapp.SummaryRefreshListener
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

class BooksAdapter: RecyclerView.Adapter<BookViewHolder>() {

    private val data = mutableListOf<Book>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private var listener: SummaryRefreshListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(data[position])
        val binding = holder.getBinding()

        binding.root.setOnLongClickListener {
            val context = binding.root.context
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialog.setTitle(
                binding.root.resources.getString(
                    R.string.removing_are_you_sure,
                    binding.bookTitle.text.toString()
                )
            )
            alertDialog.setPositiveButton(binding.root.resources.getString(R.string.yes)) { _, _ ->
                thread {
                    val selectedBook = BookDB.open(context).books.getBook(binding.id.text.toString().toLong())
                    BookDB.open(context).books.removeBook(selectedBook)

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
                    replace(books)
                    listener?.summaryDataRefresh(context)
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

        binding.root.setOnClickListener {
            (binding.root.context as? Navigable)?.navigateWithBookId(Navigable.Destination.Edit, binding.id.text.toString().toLong())
        }
    }

    fun setSummaryRefreshListener(listener: SummaryRefreshListener){
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged", "DiscouragedApi")
    fun replace(newData: List<Book>){
        data.clear()
        data.addAll(newData.reversed()) //order from last added

        handler.post {
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sort() {
        val notSorted = data.toList()
        data.sortBy {
            it.currentPage.toDouble() / it.maxPages.toDouble() * 100
        }

        val callback = BookCallback(notSorted, data)
        val result = DiffUtil.calculateDiff(callback)

        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }

}