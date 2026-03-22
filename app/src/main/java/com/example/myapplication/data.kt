package com.example.myapplication
import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.myapplication.MainActivity.Stats
import com.google.firebase.firestore.FirebaseFirestore


@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "HP")     var HP    : Int,
    @ColumnInfo(name = "TmpHP") var TmpHP : Int,
    @ColumnInfo(name = "MaxHP")  var MaxHP : Int,
    @ColumnInfo(name = "AC")     var AC    : Int,
    @ColumnInfo(name = "Level1") var Level1: Int,
    @ColumnInfo(name = "Level2") var Level2: Int,
    @ColumnInfo(name = "Level3") var Level3: Int,
    @ColumnInfo(name = "Level4") var Level4: Int,
    @ColumnInfo(name = "Level5") var Level5: Int,
    @ColumnInfo(name = "Level6") var Level6: Int,
    @ColumnInfo(name = "Level7") var Level7: Int,
    @ColumnInfo(name = "Level8") var Level8: Int,
    @ColumnInfo(name = "Level9") var Level9: Int,

    @ColumnInfo(name = "MaxLevel1") var MaxLevel1: Int,
    @ColumnInfo(name = "MaxLevel2") var MaxLevel2: Int,
    @ColumnInfo(name = "MaxLevel3") var MaxLevel3: Int,
    @ColumnInfo(name = "MaxLevel4") var MaxLevel4: Int,
    @ColumnInfo(name = "MaxLevel5") var MaxLevel5: Int,
    @ColumnInfo(name = "MaxLevel6") var MaxLevel6: Int,
    @ColumnInfo(name = "MaxLevel7") var MaxLevel7: Int,
    @ColumnInfo(name = "MaxLevel8") var MaxLevel8: Int,
    @ColumnInfo(name = "MaxLevel9") var MaxLevel9: Int

)

@Entity (tableName = "spells")
data class Spells(
    @PrimaryKey (autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "level") var level: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "Components") var components: String,
    @ColumnInfo(name = "duration") var duration: Int,
    @ColumnInfo(name = "cast_time") var casttime: Int,
    @ColumnInfo(name = "distance") var distance: Int

)

@Dao
interface SpellDao {
    @Query("SELECT * FROM spells")
    fun getAll(): List<Spells>

    @Insert
    fun insert(spell: Spells)

    @Delete
    fun delete(spell: Spells)

    @Update
    fun update(spell: Spells)
}


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun updateUsers(user: User)

    @Query("SELECT * FROM user LIMIT 1 ")
    fun getUser(): User


}

fun getValuebyUser(user: User, name: String): Int {
    return when(name){
        "HP"     -> user.HP
        "Tmp.HP" -> user.TmpHP
        "AC"     -> user.AC
        "Max.HP"  -> user.MaxHP
        "Level1" -> user.Level1
        "Level2" -> user.Level2
        "Level3" -> user.Level3
        "Level4" -> user.Level4
        "Level5" -> user.Level5
        "Level6" -> user.Level6
        "Level7" -> user.Level7
        "Level8" -> user.Level8
        "Level9" -> user.Level9

        "MaxLevel1" -> user.MaxLevel1
        "MaxLevel2" -> user.MaxLevel2
        "MaxLevel3" -> user.MaxLevel3
        "MaxLevel4" -> user.MaxLevel4
        "MaxLevel5" -> user.MaxLevel5
        "MaxLevel6" -> user.MaxLevel6
        "MaxLevel7" -> user.MaxLevel7
        "MaxLevel8" -> user.MaxLevel8
        "MaxLevel9" -> user.MaxLevel9
        else -> 0
    }
}

fun incByOnebyUser(user: User,UserDao: UserDao, name: String){
    val currentValue = getValuebyUser(user, name) ?: 0
    setValuebyUser(user,UserDao, name, currentValue + 1)
}
fun setValuebyUser(user: User,userDao: UserDao, name: String, value: Int){
    when (name){
        "HP" -> {user.HP = value}
        "Tmp.HP" -> {user.TmpHP = value }
        "AC" -> {user.AC = value}
        "Max.HP" -> {user.MaxHP = value}
        "Level1" -> {user.Level1 = value }
        "Level2" -> {user.Level2 = value }
        "Level3" -> {user.Level3 = value }
        "Level4" -> {user.Level4 = value }
        "Level5" -> {user.Level5 = value }
        "Level6" -> {user.Level6 = value }
        "Level7" -> {user.Level7 = value }
        "Level8" -> {user.Level8 = value }
        "Level9" -> {user.Level9 = value }

        "MaxLevel1" -> {user.MaxLevel1 = value }
        "MaxLevel2" -> {user.MaxLevel2 = value }
        "MaxLevel3" -> {user.MaxLevel3 = value }
        "MaxLevel4" -> {user.MaxLevel4 = value }
        "MaxLevel5" -> {user.MaxLevel5 = value }
        "MaxLevel6" -> {user.MaxLevel6 = value }
        "MaxLevel7" -> {user.MaxLevel7 = value }
        "MaxLevel8" -> {user.MaxLevel8 = value }
        "MaxLevel9" -> {user.MaxLevel9 = value }
    }

    userDao.updateUsers(user)

}


@Database(entities = [User::class, Spells::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun spellsDao(): SpellDao
}


    // Online database

fun cloudSave(context: Context) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var deviceId = prefs.getString("device_id", null)

    if (deviceId == null) {
        deviceId = java.util.UUID.randomUUID().toString()
        prefs.edit().putString("device_id", deviceId).apply()
    }

    val data = hashMapOf(
        "HP" to Stats.hp.current,
        "Max.HP" to Stats.hp.maxvalue,
        "Tmp.HP" to Stats.tmphp.current,

        "AC" to Stats.ac.current,
        "Level1" to Stats.level1.current,
        "Level2" to Stats.level2.current,
        "Level3" to Stats.level3.current,
        "Level4" to Stats.level4.current,
        "Level5" to Stats.level5.current,
        "Level6" to Stats.level6.current,
        "Level7" to Stats.level7.current,
        "Level8" to Stats.level8.current,
        "Level9" to Stats.level9.current,
        "MaxLevel1" to Stats.level1.maxvalue,
        "MaxLevel2" to Stats.level2.maxvalue,
        "MaxLevel3" to Stats.level3.maxvalue,
        "MaxLevel4" to Stats.level4.maxvalue,
        "MaxLevel5" to Stats.level5.maxvalue,
        "MaxLevel6" to Stats.level6.maxvalue,
        "MaxLevel7" to Stats.level7.maxvalue,
        "MaxLevel8" to Stats.level8.maxvalue,
        "MaxLevel9" to Stats.level9.maxvalue

    )

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(deviceId)
        .set(data)
}


fun cloudLoad(context: Context, onLoaded: () -> Unit) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val deviceId = prefs.getString("device_id", null) ?: return

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(deviceId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                Stats.hp.current = (document.getLong("HP") ?: 0).toInt()
                Stats.hp.maxvalue = (document.getLong("Max.HP") ?: 0).toInt()

                Stats.tmphp.current = (document.getLong("Tmp.HP") ?: 0).toInt()
                Stats.ac.current = (document.getLong("AC") ?: 0).toInt()

                Stats.level1.current = (document.getLong("Level1") ?: 0).toInt()
                Stats.level2.current = (document.getLong("Level2") ?: 0).toInt()
                Stats.level3.current = (document.getLong("Level3") ?: 0).toInt()
                Stats.level4.current = (document.getLong("Level4") ?: 0).toInt()
                Stats.level5.current = (document.getLong("Level5") ?: 0).toInt()
                Stats.level6.current = (document.getLong("Level6") ?: 0).toInt()
                Stats.level7.current = (document.getLong("Level7") ?: 0).toInt()
                Stats.level8.current = (document.getLong("Level8") ?: 0).toInt()
                Stats.level9.current = (document.getLong("Level9") ?: 0).toInt()

                Stats.level1.maxvalue = (document.getLong("MaxLevel1") ?: 0).toInt()
                Stats.level2.maxvalue = (document.getLong("MaxLevel2") ?: 0).toInt()
                Stats.level3.maxvalue = (document.getLong("MaxLevel3") ?: 0).toInt()
                Stats.level4.maxvalue = (document.getLong("MaxLevel4") ?: 0).toInt()
                Stats.level5.maxvalue = (document.getLong("MaxLevel5") ?: 0).toInt()
                Stats.level6.maxvalue = (document.getLong("MaxLevel6") ?: 0).toInt()
                Stats.level7.maxvalue = (document.getLong("MaxLevel7") ?: 0).toInt()
                Stats.level8.maxvalue = (document.getLong("MaxLevel8") ?: 0).toInt()
                Stats.level9.maxvalue = (document.getLong("MaxLevel9") ?: 0).toInt()

                onLoaded()
            }
        }
    }



