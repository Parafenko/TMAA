package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Names(val value: String){
    HP("HP"),
    MaxHP("Max.HP"),
    TmpHP("Tmp.HP"),
    AC("AC"),
    TakeDamage("Take Dmg."),
    Heal("Heal"),
}
enum class Levels(val exp: String, val value: String, val maxvalue: String){
    Level1("Level 1", value = "Level1","MaxLevel1"),
    Level2("Level 2", value = "Level2","MaxLevel2"),
    Level3("Level 3", value = "Level3","MaxLevel3"),
    Level4("Level 4", value = "Level4","MaxLevel4"),
    Level5("Level 5", value = "Level5","MaxLevel5"),
    Level6("Level 6", value = "Level6","MaxLevel6"),
    Level7("Level 7", value = "Level7","MaxLevel7"),
    Level8("Level 8", value = "Level8","MaxLevel8"),
    Level9("Level 9", value = "Level9","MaxLevel9")

}

class Attribute(val value: Int, val max: Int?){
    var current by mutableIntStateOf(value)
    var maxvalue by mutableStateOf(max)
}

object Stats {
    var hp = Attribute(0, 0)
    var tmphp = Attribute(0, null)
    var ac = Attribute(0, null)
    var level1 = Attribute(0, 0)
    var level2 = Attribute(0, 0)
    var level3 = Attribute(0, 0)
    var level4 = Attribute(0, 0)
    var level5 = Attribute(0, 0)
    var level6 = Attribute(0, 0)
    var level7 = Attribute(0, 0)
    var level8 = Attribute(0, 0)
    var level9 = Attribute(0, 0)
}