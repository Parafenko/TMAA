package com.example.myapplication.feature.character

import com.example.myapplication.*
import com.example.myapplication.feature.spells.*
import com.example.myapplication.core.db.*
import com.example.myapplication.core.cloud.*

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun updateUsers(user: User)
}
