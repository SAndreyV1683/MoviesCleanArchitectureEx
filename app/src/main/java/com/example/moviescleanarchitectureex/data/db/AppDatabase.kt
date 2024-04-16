package com.example.moviescleanarchitectureex.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviescleanarchitectureex.data.db.dao.MovieDao

@Database(version = 1, entities = [MovieEntity :: class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}