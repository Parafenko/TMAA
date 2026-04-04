package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "spells")
data class Spells(
    @PrimaryKey (autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "level") var level: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "Components") var components: String,
    @ColumnInfo(name = "duration") var duration: Int,
    @ColumnInfo(name = "cast_time") var casttime: String,
    @ColumnInfo(name = "distance") var distance: Int

)