package com.example.readingtrackerapp.data

import androidx.room.*
import com.example.readingtrackerapp.data.model.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM book;")
    fun getAll(): List<BookEntity>

    @Query("SELECT * from book WHERE id = :id;")
    fun getBook(id: Long): BookEntity

    @Insert
    fun addBook(newBook: BookEntity)

    @Update
    fun updateBook(book: BookEntity)

    @Delete
    fun removeBook(bookId: BookEntity)


}