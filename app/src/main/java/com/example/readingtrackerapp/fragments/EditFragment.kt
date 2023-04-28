package com.example.readingtrackerapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var imagesAdapter: BookImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagesAdapter = BookImagesAdapter(false)

        val spinner = binding.status
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.available_status)
        )
        spinner.adapter = spinnerAdapter

        if (id.toInt() != -1) {
            CoroutineScope(Dispatchers.Main).launch {
                val data = withContext(Dispatchers.IO) {
                    BookDB.open(view.context).books.getBook(id)
                }
                binding.title.setText(data.title)
                binding.status.setSelection(spinnerAdapter.getPosition(data.status))
                binding.currentPage.setText(data.currentPage.toString())
                binding.maxPages.setText(data.maxPage.toString())

                val image = requireContext().resources.getIdentifier(
                    data.icon,
                    "drawable",
                    requireContext().packageName
                )
                imagesAdapter = BookImagesAdapter(true, image)
                binding.images.apply {
                    adapter = this@EditFragment.imagesAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        } else {
            binding.images.apply {
                adapter = this@EditFragment.imagesAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        binding.saveBtn.setOnClickListener {
            val title = binding.title
            val currentPage = binding.currentPage
            val maxPage = binding.maxPages

            if (title.text.isEmpty()) {
                title.error = resources.getString(R.string.empty_title_alert)
                return@setOnClickListener
            }

            val currentPageValueExists = binding.currentPage.text.isNotEmpty()
            val maxPageValueExists = binding.maxPages.text.isNotEmpty()
            if (currentPageValueExists && maxPageValueExists) {
                if (currentPage.text.toString().toInt() > maxPage.text.toString().toInt()) {
                    currentPage.error = resources.getString(R.string.page_error)
                    maxPage.error = resources.getString(R.string.page_error)
                    return@setOnClickListener
                }
            }

            if (id.toInt() == -1) { // adding
                val newBook = BookEntity(
                    title = title.text.toString(),
                    status = binding.status.selectedItem.toString(),
                    currentPage = if (currentPage.text.isEmpty()) 0 else currentPage.text.toString()
                        .toInt(),
                    maxPage = if (maxPage.text.isEmpty()) 0 else maxPage.text.toString().toInt(),
                    icon = resources.getResourceName(imagesAdapter.selectedResId)
                )
                thread {
                    BookDB.open(requireContext()).books.addBook(newBook)
                    parentFragmentManager.popBackStack()
                }
            } else { // editing
                val book = BookEntity(
                    id = id,
                    title = title.text.toString(),
                    status = binding.status.selectedItem.toString(),
                    currentPage = if (currentPage.text.isEmpty()) 0 else currentPage.text.toString()
                        .toInt(),
                    maxPage = if (maxPage.text.isEmpty()) 0 else maxPage.text.toString().toInt(),
                    icon = resources.getResourceName(imagesAdapter.selectedResId)
                )
                thread {
                    BookDB.open(requireContext()).books.updateBook(book)
                    parentFragmentManager.popBackStack()
                }
            }
            Toast.makeText(this.context, this.getString(R.string.saved), Toast.LENGTH_SHORT).show()
        }
    }
}