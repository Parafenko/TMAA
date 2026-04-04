package com.example.myapplication

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

