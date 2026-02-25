package com.example.myapplication
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.content.Context
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
data class Data(
    var tmphp: String = "",
    var maxhp: String = "",
    var hp: String = "",
    var name: String = "",
    var ac: String = "",
    var messages: List<String> = emptyList(),
    var spellscount: Spellscount = Spellscount()
)
@Serializable
data class Spellscount(
    var level1: String = "0",
    var level2: String = "0",
    var level3: String = "0",
    var level4: String = "0",
    var level5: String = "0",
    var level6: String = "0",
    var level7: String = "0",
    var level8: String = "0",
    var level9: String = "0"
)


val jsonParser = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}
fun getJsonContent(context: Context): String {
    val filename = "data.json"
    val file = File(context.filesDir, filename)

    return (
        if (file.exists()) file.readText()
        else context.assets.open(filename).bufferedReader().use { it.readText() }
    )
}

fun get_value(context: Context, name: String):String{
    val jsonContent = getJsonContent(context)
    val data = jsonParser.decodeFromString<List<Data>>(jsonContent).firstOrNull()
        ?: return "not found"
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
        else -> "0"
    }
}


fun set_value(context: Context, name: String, value: String){
    val filename = "data.json"
    val file = File(context.filesDir, filename)
    val jsonContent = getJsonContent(context)
    val datalist = jsonParser.decodeFromString<List<Data>>(jsonContent)
    val data = datalist.firstOrNull() ?: return
    when (name){
        "hp" -> data.hp = value
        "tmphp" -> data.tmphp = value
        "maxhp" -> data.maxhp =value
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

