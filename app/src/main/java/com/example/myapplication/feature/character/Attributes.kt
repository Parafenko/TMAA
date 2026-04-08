package com.example.myapplication.feature.character

import com.example.myapplication.*
import com.example.myapplication.feature.spells.*
import com.example.myapplication.core.db.*
import com.example.myapplication.core.cloud.*

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Names(val value: String) {
    HP("HP"),
    MaxHP("Max.HP"),
    TmpHP("Tmp.HP"),
    AC("AC"),
    TakeDamage("Take Dmg."),
    Heal("Heal"),
}

enum class Levels(val exp: String, val value: String, val maxvalue: String) {
    Level1("Level 1", "Level1", "MaxLevel1"),
    Level2("Level 2", "Level2", "MaxLevel2"),
    Level3("Level 3", "Level3", "MaxLevel3"),
    Level4("Level 4", "Level4", "MaxLevel4"),
    Level5("Level 5", "Level5", "MaxLevel5"),
    Level6("Level 6", "Level6", "MaxLevel6"),
    Level7("Level 7", "Level7", "MaxLevel7"),
    Level8("Level 8", "Level8", "MaxLevel8"),
    Level9("Level 9", "Level9", "MaxLevel9"),
}

class Attribute(value: Int, max: Int?) {
    var current  by mutableIntStateOf(value)
    var maxvalue by mutableStateOf(max)
}

object Stats {
    val hp     = Attribute(0, 0)
    val tmphp  = Attribute(0, null)
    val ac     = Attribute(0, null)
    val level1 = Attribute(0, 0)
    val level2 = Attribute(0, 0)
    val level3 = Attribute(0, 0)
    val level4 = Attribute(0, 0)
    val level5 = Attribute(0, 0)
    val level6 = Attribute(0, 0)
    val level7 = Attribute(0, 0)
    val level8 = Attribute(0, 0)
    val level9 = Attribute(0, 0)

    fun levelAttribute(level: Levels) = when (level) {
        Levels.Level1 -> level1
        Levels.Level2 -> level2
        Levels.Level3 -> level3
        Levels.Level4 -> level4
        Levels.Level5 -> level5
        Levels.Level6 -> level6
        Levels.Level7 -> level7
        Levels.Level8 -> level8
        Levels.Level9 -> level9
    }

    fun getByName(name: String): Int = when (name) {
        Names.HP.value     -> hp.current
        Names.MaxHP.value  -> hp.maxvalue ?: 0
        Names.TmpHP.value  -> tmphp.current
        Names.AC.value     -> ac.current
        else -> Levels.entries.firstOrNull { it.value == name }?.let { levelAttribute(it).current }
            ?: Levels.entries.firstOrNull { it.maxvalue == name }?.let { levelAttribute(it).maxvalue ?: 0 }
            ?: 0
    }

    fun setByName(name: String, value: Int) {
        when (name) {
            Names.HP.value    -> hp.current      = value
            Names.MaxHP.value -> hp.maxvalue      = value
            Names.TmpHP.value -> tmphp.current    = value
            Names.AC.value    -> ac.current       = value
            else -> {
                Levels.entries.firstOrNull { it.value == name }?.let    { levelAttribute(it).current  = value }
                Levels.entries.firstOrNull { it.maxvalue == name }?.let { levelAttribute(it).maxvalue = value }
            }
        }
    }
}
