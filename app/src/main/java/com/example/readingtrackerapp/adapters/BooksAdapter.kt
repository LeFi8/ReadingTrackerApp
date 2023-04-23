package com.example.readingtrackerapp.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
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

class BookViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book) {
        binding.bookTitle.text = book.title
        binding.readingStatus.text = binding.root.context.getString(R.string.status, book.status)
        binding.currentReadingPage.text = binding.root.context.getString(R.string.page, book.currentPage)
        binding.image.setImageResource(book.resId)
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

        binding.root.setOnLongClickListener{
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(parent.context)
            alertDialog.setTitle(parent.resources.getString(R.string.removing_are_you_sure, binding.bookTitle.text))
            alertDialog.setPositiveButton(parent.resources.getString(R.string.yes)){ _, _ ->
//                BookDB.open(parent.context).books.removeBook()

                Toast.makeText(parent.context,parent.resources.getString(R.string.removed),Toast.LENGTH_SHORT).show()
            }
            alertDialog.setNegativeButton(parent.resources.getString(R.string.no)){ _, _ -> {} }
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
    fun replace(newData: List<Book>){
        data.clear()
        data.addAll(newData)

        handler.post {
            notifyDataSetChanged()
        }
    }
}