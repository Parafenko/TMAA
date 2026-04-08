package com.example.myapplication.core.cloud

import com.example.myapplication.*
import com.example.myapplication.feature.character.*
import com.example.myapplication.feature.spells.*
import com.example.myapplication.core.db.*

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

fun cloudSave(context: Context) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val deviceId = prefs.getString("device_id", null) ?: UUID.randomUUID().toString().also {
        prefs.edit().putString("device_id", it).apply()
    }

    val data = hashMapOf(
        "HP"         to Stats.hp.current,
        "Max.HP"     to Stats.hp.maxvalue,
        "Tmp.HP"     to Stats.tmphp.current,
        "AC"         to Stats.ac.current,
        "Level1"     to Stats.level1.current,
        "Level2"     to Stats.level2.current,
        "Level3"     to Stats.level3.current,
        "Level4"     to Stats.level4.current,
        "Level5"     to Stats.level5.current,
        "Level6"     to Stats.level6.current,
        "Level7"     to Stats.level7.current,
        "Level8"     to Stats.level8.current,
        "Level9"     to Stats.level9.current,
        "MaxLevel1"  to Stats.level1.maxvalue,
        "MaxLevel2"  to Stats.level2.maxvalue,
        "MaxLevel3"  to Stats.level3.maxvalue,
        "MaxLevel4"  to Stats.level4.maxvalue,
        "MaxLevel5"  to Stats.level5.maxvalue,
        "MaxLevel6"  to Stats.level6.maxvalue,
        "MaxLevel7"  to Stats.level7.maxvalue,
        "MaxLevel8"  to Stats.level8.maxvalue,
        "MaxLevel9"  to Stats.level9.maxvalue,
    )

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(deviceId)
        .set(data)
}

// onLoaded викликається на Main thread (Firestore listener вже на Main)
fun cloudLoad(context: Context, onLoaded: () -> Unit) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val deviceId = prefs.getString("device_id", null) ?: return

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(deviceId)
        .get()
        .addOnSuccessListener { document ->
            if (!document.exists()) return@addOnSuccessListener

            Stats.hp.current      = document.getLong("HP")?.toInt()         ?: Stats.hp.current
            Stats.hp.maxvalue     = document.getLong("Max.HP")?.toInt()      ?: Stats.hp.maxvalue
            Stats.tmphp.current   = document.getLong("Tmp.HP")?.toInt()      ?: Stats.tmphp.current
            Stats.ac.current      = document.getLong("AC")?.toInt()          ?: Stats.ac.current
            Stats.level1.current  = document.getLong("Level1")?.toInt()      ?: Stats.level1.current
            Stats.level2.current  = document.getLong("Level2")?.toInt()      ?: Stats.level2.current
            Stats.level3.current  = document.getLong("Level3")?.toInt()      ?: Stats.level3.current
            Stats.level4.current  = document.getLong("Level4")?.toInt()      ?: Stats.level4.current
            Stats.level5.current  = document.getLong("Level5")?.toInt()      ?: Stats.level5.current
            Stats.level6.current  = document.getLong("Level6")?.toInt()      ?: Stats.level6.current
            Stats.level7.current  = document.getLong("Level7")?.toInt()      ?: Stats.level7.current
            Stats.level8.current  = document.getLong("Level8")?.toInt()      ?: Stats.level8.current
            Stats.level9.current  = document.getLong("Level9")?.toInt()      ?: Stats.level9.current
            Stats.level1.maxvalue = document.getLong("MaxLevel1")?.toInt()   ?: Stats.level1.maxvalue
            Stats.level2.maxvalue = document.getLong("MaxLevel2")?.toInt()   ?: Stats.level2.maxvalue
            Stats.level3.maxvalue = document.getLong("MaxLevel3")?.toInt()   ?: Stats.level3.maxvalue
            Stats.level4.maxvalue = document.getLong("MaxLevel4")?.toInt()   ?: Stats.level4.maxvalue
            Stats.level5.maxvalue = document.getLong("MaxLevel5")?.toInt()   ?: Stats.level5.maxvalue
            Stats.level6.maxvalue = document.getLong("MaxLevel6")?.toInt()   ?: Stats.level6.maxvalue
            Stats.level7.maxvalue = document.getLong("MaxLevel7")?.toInt()   ?: Stats.level7.maxvalue
            Stats.level8.maxvalue = document.getLong("MaxLevel8")?.toInt()   ?: Stats.level8.maxvalue
            Stats.level9.maxvalue = document.getLong("MaxLevel9")?.toInt()   ?: Stats.level9.maxvalue

            onLoaded()
        }
}
