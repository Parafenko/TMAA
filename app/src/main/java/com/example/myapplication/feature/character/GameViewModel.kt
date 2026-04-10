package com.example.myapplication.feature.character

import com.example.myapplication.*
import com.example.myapplication.feature.spells.*
import com.example.myapplication.core.db.*
import com.example.myapplication.core.cloud.*

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db       = AppDatabase.getDatabase(application)
    private val userDao  = db.userDao()
    private val spellDao = db.spellsDao()

    private lateinit var user: User
    var spells = mutableStateListOf<Spells>()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (userDao.getAll().isEmpty()) {
                    userDao.insert(User(
                        HP = 0, TmpHP = 0, MaxHP = 0, AC = 0,
                        Level1 = 0, Level2 = 0, Level3 = 0, Level4 = 0, Level5 = 0,
                        Level6 = 0, Level7 = 0, Level8 = 0, Level9 = 0,
                        MaxLevel1 = 0, MaxLevel2 = 0, MaxLevel3 = 0, MaxLevel4 = 0,
                        MaxLevel5 = 0, MaxLevel6 = 0, MaxLevel7 = 0, MaxLevel8 = 0,
                        MaxLevel9 = 0
                    ))
                }
                user = userDao.getUser()
                val loadedSpells = spellDao.getAll()

                withContext(Dispatchers.Main) {
                    spells.clear()
                    spells.addAll(loadedSpells)

                    Stats.hp.current      = getValueByUser(user, Names.HP.value)
                    Stats.hp.maxvalue     = getValueByUser(user, Names.MaxHP.value)
                    Stats.tmphp.current   = getValueByUser(user, Names.TmpHP.value)
                    Stats.ac.current      = getValueByUser(user, Names.AC.value)

                    Levels.entries.forEach { level ->
                        Stats.levelAttribute(level).current  = getValueByUser(user, level.value)
                        Stats.levelAttribute(level).maxvalue = getValueByUser(user, level.maxvalue)
                    }
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "initializeData failed", e)
                _error.value = "Failed to load data: ${e.message}"
            }
        }
    }

    fun getSpellsbyLevel(level: Int): List<Spells> = spells.filter { it.level == level }

    fun setStats(name: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val capped = if (name == Names.HP.value) value.coerceAtMost(Stats.hp.maxvalue ?: 0) else value
                withContext(Dispatchers.Main) { Stats.setByName(name, capped) }
                setValueByUser(user, userDao, name, capped)
            } catch (e: Exception) {
                Log.e("GameViewModel", "setStats failed", e)
                _error.value = "Failed to save stat: ${e.message}"
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
                    setValueByUser(user, userDao, Names.TmpHP.value, newTmp)
                } else {
                    val newHp = (hp - (value - tmp)).coerceAtLeast(0)
                    withContext(Dispatchers.Main) { Stats.tmphp.current = 0; Stats.hp.current = newHp }
                    setValueByUser(user, userDao, Names.TmpHP.value, 0)
                    setValueByUser(user, userDao, Names.HP.value, newHp)
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "takeDamage failed", e)
                _error.value = "Failed to apply damage: ${e.message}"
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
                        setValueByUser(user, userDao, Names.HP.value, newHp)
                    }
                    Names.TmpHP.value -> {
                        val newTmp = Stats.tmphp.current + value
                        withContext(Dispatchers.Main) { Stats.tmphp.current = newTmp }
                        setValueByUser(user, userDao, Names.TmpHP.value, newTmp)
                    }
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "addHP failed", e)
                _error.value = "Failed to heal: ${e.message}"
            }
        }
    }

    fun spellAdder(name: String, level: Int, description: String, components: String, duration: Int, casttime: String, distance: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                spellDao.insert(Spells(name = name, level = level, description = description, components = components, duration = duration, casttime = casttime, distance = distance))
                val loaded = spellDao.getAll()
                withContext(Dispatchers.Main) { spells.clear(); spells.addAll(loaded) }
            } catch (e: Exception) {
                Log.e("GameViewModel", "spellAdder failed", e)
                _error.value = "Failed to add spell: ${e.message}"
            }
        }
    }

    fun valuesetter(name: String, newValue: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { Stats.setByName(name, newValue) }
                setValueByUser(user, userDao, name, newValue)
            } catch (e: Exception) {
                Log.e("GameViewModel", "valuesetter failed", e)
                _error.value = "Failed to save value: ${e.message}"
            }
        }
    }

    fun saveAllToCloud() { cloudSave(getApplication()) }

    fun loadAllFromCloud() {
        cloudLoad(getApplication()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Levels.entries.forEach { level ->
                        setValueByUser(user, userDao, level.value,    Stats.levelAttribute(level).current)
                        setValueByUser(user, userDao, level.maxvalue, Stats.levelAttribute(level).maxvalue ?: 0)
                    }
                    user.HP    = Stats.hp.current
                    user.MaxHP = Stats.hp.maxvalue ?: 0
                    user.TmpHP = Stats.tmphp.current
                    user.AC    = Stats.ac.current
                    userDao.updateUsers(user)
                } catch (e: Exception) {
                    Log.e("GameViewModel", "loadAllFromCloud sync failed", e)
                    _error.value = "Failed to sync cloud data: ${e.message}"
                }
            }
        }
    }

    fun sendNotification(context: Context, title: String, message: String) {
        val channelId = "game_notifications"
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(channelId, "Game Events", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }

        notificationManager.notify(1,
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.frog)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
        )
    }
    fun fetchRandomTip(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = java.net.URL("https://official-joke-api.appspot.com/random_joke")
                val connection = url.openConnection() as java.net.HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == java.net.HttpURLConnection.HTTP_OK) {
                    val stream = connection.inputStream
                    val reader = java.io.BufferedReader(java.io.InputStreamReader(stream))
                    val result = reader.readText()
                    reader.close()

                    val json = org.json.JSONObject(result)
                    val setup = json.optString("setup", "What did the API say?")
                    val punchline = json.optString("punchline", "200 OK")

                    withContext(Dispatchers.Main) {
                        sendNotification(context, "API Joke", "$setup - $punchline")
                    }
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "fetchRandomTip failed", e)
                withContext(Dispatchers.Main) {
                    sendNotification(context, "API Error", e.localizedMessage ?: "Failed")
                }
            }
        }
    }
}
