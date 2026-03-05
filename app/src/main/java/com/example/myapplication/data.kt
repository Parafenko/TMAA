package com.example.myapplication
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.content.Context
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
data class Data(
    var tmphp: Int = 0,
    var maxhp: Int = 0,
    var hp:    Int = 0,
    var ac:    Int = 0,
    var spellscount: Spellscount = Spellscount()
)
@Serializable
data class Spellscount(
    var level1: Int = 0,
    var level2: Int = 0,
    var level3: Int = 0,
    var level4: Int = 0,
    var level5: Int = 0,
    var level6: Int = 0,
    var level7: Int = 0,
    var level8: Int = 0,
    var level9: Int = 0
)


val jsonParser = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}
fun GetJsonContent(context: Context): String {
    val filename = "data.json"
    val file = File(context.filesDir, filename)

    return (
        if (file.exists()) file.readText()
        else context.assets.open(filename).bufferedReader().use { it.readText() }
    )
}

fun GetValue(context: Context, name: String):Int{
    val jsonContent = GetJsonContent(context)
    val data = jsonParser.decodeFromString<List<Data>>(jsonContent).firstOrNull()
        ?: return 0
    return when(name){
        "hp" -> data.hp
        "tmphp" -> data.tmphp
        "maxhp" -> data.maxhp
        "ac" -> data.ac
        "level1" -> data.spellscount.level1
        "level2" -> data.spellscount.level2
        "level3" -> data.spellscount.level3
        "level4" -> data.spellscount.level4
        "level5" -> data.spellscount.level5
        "level6" -> data.spellscount.level6
        "level7" -> data.spellscount.level7
        "level8" -> data.spellscount.level8
        "level9" -> data.spellscount.level9
        else -> 0
    }
}


fun SetValue(context: Context, name: String, value: Int){
    val filename = "data.json"
    val file = File(context.filesDir, filename)
    val jsonContent = GetJsonContent(context)
    val datalist = jsonParser.decodeFromString<List<Data>>(jsonContent)


    val data = datalist.firstOrNull() ?: return
    when (name){
        "hp" -> data.hp = value
        "tmphp" -> data.tmphp = value
        "maxhp" -> data.maxhp = value
        "ac" -> data.ac = value
        "level1" -> data.spellscount.level1 = value
        "level2" -> data.spellscount.level2 = value
        "level3" -> data.spellscount.level3 = value
        "level4" -> data.spellscount.level4 = value
        "level5" -> data.spellscount.level5 = value
        "level6" -> data.spellscount.level6 = value
        "level7" -> data.spellscount.level7 = value
        "level8" -> data.spellscount.level8 = value
        "level9" -> data.spellscount.level9 = value
    }
    file.writeText(jsonParser.encodeToString(datalist))
}

