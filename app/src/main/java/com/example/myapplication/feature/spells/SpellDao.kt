package com.example.myapplication.feature.spells

import com.example.myapplication.*
import com.example.myapplication.feature.character.*
import com.example.myapplication.core.db.*
import com.example.myapplication.core.cloud.*

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SpellDao {
    @Query("SELECT * FROM spells")
    suspend fun getAll(): List<Spells>

    @Insert
    suspend fun insert(spell: Spells)

    @Delete
    suspend fun delete(spell: Spells)

    @Update
    suspend fun update(spell: Spells)
}
