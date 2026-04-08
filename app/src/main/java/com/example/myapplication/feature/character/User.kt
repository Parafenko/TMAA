package com.example.myapplication.feature.character

import com.example.myapplication.*
import com.example.myapplication.feature.spells.*
import com.example.myapplication.core.db.*
import com.example.myapplication.core.cloud.*

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "HP")         var HP        : Int,
    @ColumnInfo(name = "TmpHP")      var TmpHP     : Int,
    @ColumnInfo(name = "MaxHP")      var MaxHP     : Int,
    @ColumnInfo(name = "AC")         var AC        : Int,
    @ColumnInfo(name = "Level1")     var Level1    : Int,
    @ColumnInfo(name = "Level2")     var Level2    : Int,
    @ColumnInfo(name = "Level3")     var Level3    : Int,
    @ColumnInfo(name = "Level4")     var Level4    : Int,
    @ColumnInfo(name = "Level5")     var Level5    : Int,
    @ColumnInfo(name = "Level6")     var Level6    : Int,
    @ColumnInfo(name = "Level7")     var Level7    : Int,
    @ColumnInfo(name = "Level8")     var Level8    : Int,
    @ColumnInfo(name = "Level9")     var Level9    : Int,
    @ColumnInfo(name = "MaxLevel1")  var MaxLevel1 : Int,
    @ColumnInfo(name = "MaxLevel2")  var MaxLevel2 : Int,
    @ColumnInfo(name = "MaxLevel3")  var MaxLevel3 : Int,
    @ColumnInfo(name = "MaxLevel4")  var MaxLevel4 : Int,
    @ColumnInfo(name = "MaxLevel5")  var MaxLevel5 : Int,
    @ColumnInfo(name = "MaxLevel6")  var MaxLevel6 : Int,
    @ColumnInfo(name = "MaxLevel7")  var MaxLevel7 : Int,
    @ColumnInfo(name = "MaxLevel8")  var MaxLevel8 : Int,
    @ColumnInfo(name = "MaxLevel9")  var MaxLevel9 : Int,
)
