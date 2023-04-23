package com.example.readingtrackerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readingtrackerapp.adapters.BooksAdapter
import com.example.readingtrackerapp.Navigable
import com.example.readingtrackerapp.databinding.FragmentListBinding
import kotlin.concurrent.thread

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private var adapter: BooksAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BooksAdapter()
        loadData()

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addBtn.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
        }
    }

    private fun loadData () = thread {
        adapter?.refresh(requireContext())
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

}