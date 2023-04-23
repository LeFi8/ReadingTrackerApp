package com.example.readingtrackerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.readingtrackerapp.data.model.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1
)
abstract class BookDB : RoomDatabase() {
    abstract val books: BookDao

    companion object {
        fun open(context: Context): BookDB = Room.databaseBuilder(context, BookDB::class.java, "books.db").build()
    }
}