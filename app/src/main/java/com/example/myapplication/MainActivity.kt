package com.example.myapplication


import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.em

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.NotificationCompat
import androidx.room.Room



// some modification 2
class MainActivity : ComponentActivity() {

val buttonWidth = 150.dp
val actionbuttonWidth = 60.dp
val actionbuttonSpacer = 35.dp
val buttonSpacer = 5.dp
val addersseparator = 15.dp
val blocksSeparator = 25.dp
class MainActivity : ComponentActivity() {




    lateinit var userDao: UserDao
    lateinit var user: User
    lateinit var spellDao: SpellDao
    var spells = mutableStateListOf<Spells>()


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

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
        }
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-name"
        )
            .allowMainThreadQueries()
            .build()
        val ui = Composable()
        userDao = db.userDao()
        spellDao = db.spellsDao()
        val users = userDao.getAll()

        if (users.isEmpty()) {
            val initialUsers = (
                User(
                    HP = 0, TmpHP = 0, MaxHP = 0, AC = 0,
                    Level1 = 0, Level2 = 0, Level3 = 0,
                    Level4 = 0, Level5 = 0, Level6 = 0,
                    Level7 = 0, Level8 = 0, Level9 = 0,
                    MaxLevel1 = 10, MaxLevel2 = 10, MaxLevel3 = 10,
                    MaxLevel4 = 10, MaxLevel5 = 10, MaxLevel6 = 10,
                    MaxLevel7 = 10, MaxLevel8 = 10, MaxLevel9 = 10,
                )
            )

            userDao.insert(initialUsers)
        }
        spells.clear()
        spells.addAll(spellDao.getAll())
        user = userDao.getUser()

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize())
                {   innerPadding ->

                    LaunchedEffect(Unit) {
                        Stats.hp.current   = getValuebyUser(user, Names.HP.value)
                        Stats.hp.maxvalue  = getValuebyUser(user, Names.MaxHP.value)

                        Stats.tmphp.current  = getValuebyUser(user, Names.TmpHP.value)
                        Stats.ac.current     = getValuebyUser(user, Names.AC.value)

                        Stats.level1.current = getValuebyUser(user, Levels.Level1.value)
                        Stats.level2.current = getValuebyUser(user, Levels.Level2.value)
                        Stats.level3.current = getValuebyUser(user, Levels.Level3.value)
                        Stats.level4.current = getValuebyUser(user, Levels.Level4.value)
                        Stats.level5.current = getValuebyUser(user, Levels.Level5.value)
                        Stats.level6.current = getValuebyUser(user, Levels.Level6.value)
                        Stats.level7.current = getValuebyUser(user, Levels.Level7.value)
                        Stats.level8.current = getValuebyUser(user, Levels.Level8.value)
                        Stats.level9.current = getValuebyUser(user, Levels.Level9.value)

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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            ui.GreetingImage(
                                onTakeDamage = ::takeDamage,
                                onHeal = { number -> addHP(Names.HP.value, number) },
                                onSendNotification = ::sendNotification,
                                onSetTmpHp = { number -> setStats(Names.TmpHP.value, number) },
                                onSetMaxHp = { number -> setStats(Names.MaxHP.value, number) },
                                onSetAc = { number -> setStats(Names.AC.value, number) },
                                onSetHp = { number -> setStats(Names.HP.value, number) },
                                onvaluesetter = ::valuesetter,
                                cloudSaver = { cloudSave(this@MainActivity) },
                                cloudLoader = {
                                    cloudLoad(this@MainActivity) {
                                        saveAllLoadedDataToRoom()
                                    }
                                }
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            ui.MakeHealthLine(
                                name1 = Names.HP.value,
                                name2 = Names.TmpHP.value,
                                onTakeDamage = ::takeDamage,
                                onHeal = { number -> addHP(Names.HP.value, number) },
                                onSetTmpHp = { number -> setStats(Names.TmpHP.value, number) },
                                onSetMaxHp = { number -> setStats(Names.MaxHP.value, number) },
                                onSetAc = { number -> setStats(Names.AC.value, number) },
                                onSetHp = { number -> setStats(Names.HP.value, number) },
                                onSendNotification = ::sendNotification,
                                onvaluesetter = ::valuesetter
                            )
                            Spacer(modifier = Modifier.height(blocksSeparator))

                            ui.MakeParametersChangersLine(
                                name1 = Names.Heal.value,
                                name2 = Names.TmpHP.value,
                                onTakeDamage = ::takeDamage,
                                onHeal = { number -> addHP(Names.HP.value, number) },
                                onSetTmpHp = { number -> setStats(Names.TmpHP.value, number) },
                                onSetMaxHp = { number -> setStats(Names.MaxHP.value, number) },
                                onSetAc = { number -> setStats(Names.AC.value, number) },
                                onSetHp = { number -> setStats(Names.HP.value, number) },
                                buttonWidth = buttonWidth,
                                onSendNotification = ::sendNotification
                            )

                            Spacer(modifier = Modifier.height(blocksSeparator))

                            ui.MakeParametersChangersLine(
                                name1 = Names.Heal.value,
                                name2 = Names.TmpHP.value,
                                onTakeDamage = ::takeDamage,
                                onHeal = { number -> addHP(Names.HP.value, number) },
                                onSetTmpHp = { number -> setStats(Names.TmpHP.value, number) },
                                onSetMaxHp = { number -> setStats(Names.MaxHP.value, number) },
                                onSetAc = { number -> setStats(Names.AC.value, number) },
                                onSetHp = { number -> setStats(Names.HP.value, number) },
                                buttonWidth = buttonWidth,
                                onSendNotification = ::sendNotification
                            )

                            Spacer(modifier = Modifier.height(blocksSeparator))
                            Text("Spells", fontSize = 6.em)
                            Spacer(modifier = Modifier.height(blocksSeparator))

                            ui.MakeSpellsLine(
                                name1 = Levels.Level1.exp,
                                name2 = Levels.Level2.exp,
                                onvaluesetter = ::valuesetter,
                                getSpellsbyLevel = ::getSpellsbyLevel,
                                spellAdder = ::spellAdder
                            )
                            ui.MakeSpellsLine(
                                name1 = Levels.Level3.exp,
                                name2 = Levels.Level4.exp,
                                onvaluesetter = ::valuesetter,
                                getSpellsbyLevel = ::getSpellsbyLevel,
                                spellAdder = ::spellAdder
                            )
                            ui.MakeSpellsLine(
                                name1 = Levels.Level5.exp,
                                name2 = Levels.Level6.exp,
                                onvaluesetter = ::valuesetter,
                                getSpellsbyLevel = ::getSpellsbyLevel,
                                spellAdder = ::spellAdder
                            )
                            ui.MakeSpellsLine(
                                name1 = Levels.Level7.exp,
                                name2 = Levels.Level8.exp,
                                onvaluesetter = ::valuesetter,
                                getSpellsbyLevel = ::getSpellsbyLevel,
                                spellAdder = ::spellAdder
                            )
                            ui.MakeSpellsLine(
                                name1 = Levels.Level9.exp,
                                name2 = null,
                                onvaluesetter = ::valuesetter,
                                getSpellsbyLevel = ::getSpellsbyLevel,
                                spellAdder = ::spellAdder
                            )
                        }
                    }
                }
            }
        }
    }


    fun getSpellsbyLevel(level: Int):List<Spells>{
        val newlist = mutableListOf<Spells>()
        spells.forEach({
            if (it.level == level) {
                newlist.add(it)
            }
        })
        return newlist
    }


    fun saveAllLoadedDataToRoom() {
        user.HP = Stats.hp.current
        user.MaxHP = Stats.hp.maxvalue ?: 0
        user.TmpHP = Stats.tmphp.current
        user.AC = Stats.ac.current

        user.Level1 = Stats.level1.current
        user.Level2 = Stats.level2.current
        user.Level3 = Stats.level3.current
        user.Level4 = Stats.level4.current
        user.Level5 = Stats.level5.current
        user.Level6 = Stats.level6.current
        user.Level7 = Stats.level7.current
        user.Level8 = Stats.level8.current
        user.Level9 = Stats.level9.current

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
    }

    fun setStats(name: String, value: Int){
        when(name){
            Names.MaxHP.value -> {
                Stats.hp.maxvalue = value
                setValuebyUser(user, userDao, Names.MaxHP.value, value)
            }
            Names.AC.value -> {
                Stats.ac.current = value
                setValuebyUser(user, userDao, Names.AC.value, value)
            }
            Names.HP.value -> {
                if (value < (Stats.hp.maxvalue ?: 0)) {
                    Stats.hp.current = value
                    setValuebyUser(user, userDao, Names.HP.value, value)
                } else {
                    Stats.hp.current = Stats.hp.maxvalue?:0
                    setValuebyUser(user, userDao, Names.HP.value, Stats.hp.maxvalue?:0)
                }
            }
            Names.TmpHP.value -> {
                    Stats.tmphp.current = value
                    setValuebyUser(user, userDao, Names.TmpHP.value, value)
            }
            else -> return
        }

    }

    fun takeDamage(value: Int){
        val tmp = Stats.tmphp.current
        val hp = Stats.hp.current

        if (tmp >= value) {
            Stats.tmphp.current = tmp - value
        }
        else{
            val leftover = value - tmp
            Stats.tmphp.current = 0
            Stats.hp.current = hp - leftover.coerceAtLeast(0)
        }
        if (Stats.hp.current < 0){
            Stats.hp.current = 0
        }
        setValuebyUser(user, userDao, Names.TmpHP.value, Stats.tmphp.current)
        setValuebyUser(user, userDao, Names.HP.value, Stats.hp.current)
    }

    fun addHP( name: String, value: Int) {
        if(value>0)
        {
            when (name){
                Names.HP.value ->{
                    Stats.hp.current += value
                    setValuebyUser(user, userDao, Names.HP.value, Stats.hp.current)
                }
                Names.TmpHP.value -> {
                    Stats.tmphp.current += value
                    setValuebyUser(user, userDao, Names.TmpHP.value, Stats.tmphp.current)
                }
            }

        }
        if(Stats.hp.current > (Stats.hp.maxvalue ?: 0)){
            Stats.hp.current = Stats.hp.maxvalue?:0
            setValuebyUser(user, userDao, Names.HP.value, Stats.hp.current)
        }
    }

    fun spellAdder(name: String, level: Int, description: String, components: String, duration: Int, casttime: String, distance: Int){
        spellDao.insert(
            Spells(
                name = name,
                level = level,
                description = description,
                components = components,
                duration = duration,
                casttime = casttime,
                distance = distance
            )
        )
        spells.clear()
        spells.addAll(spellDao.getAll())
    }

    fun valuesetter(name: String, newValue: Int){
        setValuebyUser(user, userDao, name, newValue)
    }

    fun cloudSaver(){
        cloudSave(this@MainActivity)
    }

    fun cloudLoader(){
        cloudLoad(this@MainActivity)
        {
            saveAllLoadedDataToRoom()
        }
    }

    // Image + Notification


    fun sendNotification(context: Context, title: String, message: String) {
        val channelId = "game_notifications"
        val notificationId = 1

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Game Events",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.frog)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}

