package com.example.readingtrackerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readingtrackerapp.databinding.FragmentEditBinding


class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: BookImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BookImagesAdapter()
        binding.images.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.saveBtn.setOnClickListener {
            val newBook = Book (
                binding.title.text.toString(),
                binding.status.text.toString(),
                binding.currentPage.text.toString().toInt(),
                binding.maxPages.text.toString().toInt(),
                adapter.selectedResId
            )

            DataSource.books.add(newBook)
            Toast.makeText(this.context, this.getString(R.string.saved), Toast.LENGTH_SHORT).show()

            (activity as? Navigable)?.navigate(Navigable.Destination.List)
        }
    }

}