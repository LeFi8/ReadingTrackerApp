package com.example.readingtrackerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.splitToIntList
import com.example.readingtrackerapp.*
import com.example.readingtrackerapp.adapters.BookImagesAdapter
import com.example.readingtrackerapp.data.BookDB
import com.example.readingtrackerapp.data.model.BookEntity
import com.example.readingtrackerapp.databinding.FragmentEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread


class EditFragment(private val id: Long = -1) : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: BookImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BookImagesAdapter(false)

        if (id.toInt() != -1) {
            CoroutineScope(Dispatchers.Main).launch {
                val data = withContext(Dispatchers.IO) {
                    BookDB.open(view.context).books.getBook(id)
                }
                binding.title.setText(data.title)
                binding.status.setText(data.status)
                binding.currentPage.setText(data.currentPage.toString())
                binding.maxPages.setText(data.maxPage.toString())

                val image = requireContext().resources.getIdentifier(data.icon, "drawable", requireContext().packageName)
                binding.images.apply {
                    adapter = BookImagesAdapter(true, image)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }

        }


        binding.images.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.saveBtn.setOnClickListener {
            if (id.toInt() == -1) {
                val newBook = BookEntity(
                    title = binding.title.text.toString(),
                    status = binding.status.text.toString(),
                    currentPage = binding.currentPage.text.toString().toInt(),
                    maxPage = binding.maxPages.text.toString().toInt(),
                    icon = resources.getResourceName(adapter.selectedResId)
                )
                thread {
                    BookDB.open(requireContext()).books.addBook(newBook)
                    (activity as? Navigable)?.navigate(Navigable.Destination.List)
                }
            } else {
                val book = BookEntity(
                    id = id,
                    title = binding.title.text.toString(),
                    status = binding.status.text.toString(),
                    currentPage = binding.currentPage.text.toString().toInt(),
                    maxPage = binding.maxPages.text.toString().toInt(),
                    icon = resources.getResourceName(adapter.selectedResId)
                )
                thread {
                    BookDB.open(requireContext()).books.updateBook(book)
                    (activity as? Navigable)?.navigate(Navigable.Destination.List)
                }
            }
            Toast.makeText(this.context, this.getString(R.string.saved), Toast.LENGTH_SHORT).show()
        }
    }

}