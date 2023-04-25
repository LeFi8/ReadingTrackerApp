package com.example.readingtrackerapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readingtrackerapp.Navigable
import com.example.readingtrackerapp.adapters.BooksAdapter
import com.example.readingtrackerapp.R
import com.example.readingtrackerapp.data.BookDB
import com.example.readingtrackerapp.databinding.FragmentListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.readingtrackerapp.SummaryRefreshListener
import com.example.readingtrackerapp.model.Book
import kotlin.concurrent.thread

class ListFragment : Fragment(), SummaryRefreshListener{

    private lateinit var binding: FragmentListBinding
    private var adapter: BooksAdapter? = null
    private lateinit var overallProgressText: TextView
    private lateinit var overallProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = BooksAdapter()
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

        overallProgressText = view.findViewById(R.id.overallProgress)
        overallProgressBar = view.findViewById(R.id.overallProgressBar)

        summaryRefresh(view.context)
        adapter?.setSummaryRefreshListener(this)

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addBtn.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Edit)
        }

        binding.sortBtn.setOnClickListener {
            adapter?.sort()
        }
    }

    private fun loadData() = thread {
        adapter?.refresh(requireContext())
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun summaryRefresh(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            var currentPagesSum = 0
            var maxPagesSum = 0
            val data = withContext(Dispatchers.IO) {
                BookDB.open(context).books.getAll()
            }

            data.forEach {
                currentPagesSum += it.currentPage
                maxPagesSum += it.maxPage
            }

            overallProgressText.text = context.resources.getString(
                R.string.overall_progress_text, currentPagesSum, maxPagesSum
            )

            overallProgressBar.progress =
                (currentPagesSum.toDouble() / maxPagesSum.toDouble() * 100).toInt()
        }
    }

    override fun summaryDataRefresh(context: Context) {
        summaryRefresh(context)
    }


}