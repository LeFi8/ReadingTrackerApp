package com.example.readingtrackerapp

import androidx.recyclerview.widget.DiffUtil
import com.example.readingtrackerapp.model.Book

class BookCallback(private val notSorted: List<Book>, private val sorted: List<Book>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = notSorted.size

    override fun getNewListSize(): Int = sorted.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] === sorted[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] == sorted[newItemPosition]
}