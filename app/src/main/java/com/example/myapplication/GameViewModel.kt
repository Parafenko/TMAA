package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val userDao = db.userDao()
    private val spellDao = db.spellsDao()

    private lateinit var user: User
    var spells = mutableStateListOf<Spells>()

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val users = userDao.getAll()
                if (users.isEmpty()) {
                    val initialUser = User(
                        HP = 0, TmpHP = 0, MaxHP = 0, AC = 0,
                        Level1 = 0, Level2 = 0, Level3 = 0, Level4 = 0, Level5 = 0,
                        Level6 = 0, Level7 = 0, Level8 = 0, Level9 = 0,
                        MaxLevel1 = 0, MaxLevel2 = 0, MaxLevel3 = 0, MaxLevel4 = 0,
                        MaxLevel5 = 0, MaxLevel6 = 0, MaxLevel7 = 0, MaxLevel8 = 0,
                        MaxLevel9 = 0
                    )
                    userDao.insert(initialUser)
                }
                user = userDao.getUser()
                val loadedSpells = spellDao.getAll()

                // Compose snapshot state must only be written on the main thread
                withContext(Dispatchers.Main) {
                    spells.clear()
                    spells.addAll(loadedSpells)

                    Stats.hp.current      = getValuebyUser(user, Names.HP.value)
                    Stats.hp.maxvalue     = getValuebyUser(user, Names.MaxHP.value)
                    Stats.tmphp.current   = getValuebyUser(user, Names.TmpHP.value)
                    Stats.ac.current      = getValuebyUser(user, Names.AC.value)

                    Stats.level1.current  = getValuebyUser(user, Levels.Level1.value)
                    Stats.level2.current  = getValuebyUser(user, Levels.Level2.value)
                    Stats.level3.current  = getValuebyUser(user, Levels.Level3.value)
                    Stats.level4.current  = getValuebyUser(user, Levels.Level4.value)
                    Stats.level5.current  = getValuebyUser(user, Levels.Level5.value)
                    Stats.level6.current  = getValuebyUser(user, Levels.Level6.value)
                    Stats.level7.current  = getValuebyUser(user, Levels.Level7.value)
                    Stats.level8.current  = getValuebyUser(user, Levels.Level8.value)
                    Stats.level9.current  = getValuebyUser(user, Levels.Level9.value)

                    Stats.level1.maxvalue = getValuebyUser(user, Levels.Level1.maxvalue)
                    Stats.level2.maxvalue = getValuebyUser(user, Levels.Level2.maxvalue)
                    Stats.level3.maxvalue = getValuebyUser(user, Levels.Level3.maxvalue)
                    Stats.level4.maxvalue = getValuebyUser(user, Levels.Level4.maxvalue)
                    Stats.level5.maxvalue = getValuebyUser(user, Levels.Level5.maxvalue)
                    Stats.level6.maxvalue = getValuebyUser(user, Levels.Level6.maxvalue)
                    Stats.level7.maxvalue = getValuebyUser(user, Levels.Level7.maxvalue)
                    Stats.level8.maxvalue = getValuebyUser(user, Levels.Level8.maxvalue)
                    Stats.level9.maxvalue = getValuebyUser(user, Levels.Level9.maxvalue)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSpellsbyLevel(level: Int): List<Spells> {
        return spells.filter { it.level == level }
    }

    fun setStats(name: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (name) {
                    Names.MaxHP.value -> {
                        withContext(Dispatchers.Main) { Stats.hp.maxvalue = value }
                        setValuebyUser(user, userDao, Names.MaxHP.value, value)
                    }
                    Names.AC.value -> {
                        withContext(Dispatchers.Main) { Stats.ac.current = value }
                        setValuebyUser(user, userDao, Names.AC.value, value)
                    }
                    Names.HP.value -> {
                        val capped = value.coerceAtMost(Stats.hp.maxvalue ?: 0)
                        withContext(Dispatchers.Main) { Stats.hp.current = capped }
                        setValuebyUser(user, userDao, Names.HP.value, capped)
                    }
                    Names.TmpHP.value -> {
                        withContext(Dispatchers.Main) { Stats.tmphp.current = value }
                        setValuebyUser(user, userDao, Names.TmpHP.value, value)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun takeDamage(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tmp = Stats.tmphp.current
                val hp  = Stats.hp.current

                if (tmp >= value) {
                    val newTmp = tmp - value
                    withContext(Dispatchers.Main) { Stats.tmphp.current = newTmp }
                    setValuebyUser(user, userDao, Names.TmpHP.value, newTmp)
                } else {
                    val leftover = value - tmp
                    val newHp = (hp - leftover).coerceAtLeast(0)
                    withContext(Dispatchers.Main) {
                        Stats.tmphp.current = 0
                        Stats.hp.current = newHp
                    }
                    setValuebyUser(user, userDao, Names.TmpHP.value, 0)
                    setValuebyUser(user, userDao, Names.HP.value, newHp)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addHP(name: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (value <= 0) return@launch
                when (name) {
                    Names.HP.value -> {
                        val newHp = (Stats.hp.current + value).coerceAtMost(Stats.hp.maxvalue ?: 0)
                        withContext(Dispatchers.Main) { Stats.hp.current = newHp }
                        setValuebyUser(user, userDao, Names.HP.value, newHp)
                    }
                    Names.TmpHP.value -> {
                        val newTmp = Stats.tmphp.current + value
                        withContext(Dispatchers.Main) { Stats.tmphp.current = newTmp }
                        setValuebyUser(user, userDao, Names.TmpHP.value, newTmp)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun spellAdder(
        name: String, level: Int, description: String,
        components: String, duration: Int, casttime: String, distance: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                spellDao.insert(
                    Spells(
                        name = name, level = level, description = description,
                        components = components, duration = duration,
                        casttime = casttime, distance = distance
                    )
                )
                val loaded = spellDao.getAll()
                withContext(Dispatchers.Main) {
                    spells.clear()
                    spells.addAll(loaded)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun valuesetter(name: String, newValue: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setValuebyUser(user, userDao, name, newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveAllToCloud() {
        cloudSave(getApplication())
    }

    fun loadAllFromCloud() {
        cloudLoad(getApplication()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // Sync cloud-loaded Stats back into Room
                    user.HP        = Stats.hp.current
                    user.MaxHP     = Stats.hp.maxvalue ?: 0
                    user.TmpHP     = Stats.tmphp.current
                    user.AC        = Stats.ac.current
                    user.Level1    = Stats.level1.current
                    user.Level2    = Stats.level2.current
                    user.Level3    = Stats.level3.current
                    user.Level4    = Stats.level4.current
                    user.Level5    = Stats.level5.current
                    user.Level6    = Stats.level6.current
                    user.Level7    = Stats.level7.current
                    user.Level8    = Stats.level8.current
                    user.Level9    = Stats.level9.current
                    user.MaxLevel1 = Stats.level1.maxvalue ?: 0
                    user.MaxLevel2 = Stats.level2.maxvalue ?: 0
                    user.MaxLevel3 = Stats.level3.maxvalue ?: 0
                    user.MaxLevel4 = Stats.level4.maxvalue ?: 0
                    user.MaxLevel5 = Stats.level5.maxvalue ?: 0
                    user.MaxLevel6 = Stats.level6.maxvalue ?: 0
                    user.MaxLevel7 = Stats.level7.maxvalue ?: 0
                    user.MaxLevel8 = Stats.level8.maxvalue ?: 0
                    user.MaxLevel9 = Stats.level9.maxvalue ?: 0
                    userDao.updateUsers(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun sendNotification(context: Context, title: String, message: String) {
        val channelId = "game_notifications"
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Game Events", NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.frog)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}