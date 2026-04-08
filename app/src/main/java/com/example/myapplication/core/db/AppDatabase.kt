package com.example.myapplication.core.db

import com.example.myapplication.*
import com.example.myapplication.feature.character.*
import com.example.myapplication.feature.spells.*
import com.example.myapplication.core.cloud.*

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Spells::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun spellsDao(): SpellDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-name"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
