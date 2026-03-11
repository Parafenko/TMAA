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
    @ColumnInfo(name = "HP") var HP: Int,
    @ColumnInfo(name = "Tmp.HP") var TmpHP: Int,
    @ColumnInfo(name = "MaxHP")  var MaxHP: Int,
    @ColumnInfo(name = "AC")     var AC: Int,
    @ColumnInfo(name = "Level1") var Level1: Int,
    @ColumnInfo(name = "Level2") var Level2: Int,
    @ColumnInfo(name = "Level3") var Level3: Int,
    @ColumnInfo(name = "Level4") var Level4: Int,
    @ColumnInfo(name = "Level5") var Level5: Int,
    @ColumnInfo(name = "Level6") var Level6: Int,
    @ColumnInfo(name = "Level7") var Level7: Int,
    @ColumnInfo(name = "Level8") var Level8: Int,
    @ColumnInfo(name = "Level9") var Level9: Int
)

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
        "MaxHP"  -> user.MaxHP
        "Level1" -> user.Level1
        "Level2" -> user.Level2
        "Level3" -> user.Level3
        "Level4" -> user.Level4
        "Level5" -> user.Level5
        "Level6" -> user.Level6
        "Level7" -> user.Level7
        "Level8" -> user.Level8
        "Level9" -> user.Level9
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
        "MaxHP" -> {user.MaxHP = value}
        "Level1" -> {user.Level1 = value }
        "Level2" -> {user.Level2 = value }
        "Level3" -> {user.Level3 = value }
        "Level4" -> {user.Level4 = value }
        "Level5" -> {user.Level5 = value }
        "Level6" -> {user.Level6 = value }
        "Level7" -> {user.Level7 = value }
        "Level8" -> {user.Level8 = value }
        "Level9" -> {user.Level9 = value }
    }

    userDao.updateUsers(user)

}
@Database(entities = [com.example.myapplication.User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): com.example.myapplication.UserDao
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
        "HP" to Stats.hp,
        "Tmp.HP" to Stats.tmphp,
        "MaxHP" to Stats.maxhp,
        "AC" to Stats.ac,
        "Level1" to Stats.level1,
        "Level2" to Stats.level2,
        "Level3" to Stats.level3,
        "Level4" to Stats.level4,
        "Level5" to Stats.level5,
        "Level6" to Stats.level6,
        "Level7" to Stats.level7,
        "Level8" to Stats.level8,
        "Level9" to Stats.level9
    )

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(deviceId)
        .set(data)
}

fun cloudLoad(context: Context) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val deviceId = prefs.getString("device_id", null) ?: return

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(deviceId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                Stats.hp = (document.getLong("HP") ?: 0).toInt()
                Stats.tmphp = (document.getLong("Tmp.HP") ?: 0).toInt()
                Stats.maxhp = (document.getLong("MaxHP") ?: 0).toInt()
                Stats.ac = (document.getLong("AC") ?: 0).toInt()
                Stats.level1 = (document.getLong("Level1") ?: 0).toInt()
                Stats.level2 = (document.getLong("Level2") ?: 0).toInt()
                Stats.level3 = (document.getLong("Level3") ?: 0).toInt()
                Stats.level4 = (document.getLong("Level4") ?: 0).toInt()
                Stats.level5 = (document.getLong("Level5") ?: 0).toInt()
                Stats.level6 = (document.getLong("Level6") ?: 0).toInt()
                Stats.level7 = (document.getLong("Level7") ?: 0).toInt()
                Stats.level8 = (document.getLong("Level8") ?: 0).toInt()
                Stats.level9 = (document.getLong("Level9") ?: 0).toInt()
            }
        }
}