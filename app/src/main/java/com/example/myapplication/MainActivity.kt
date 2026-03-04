package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.TextField
import androidx.compose.ui.unit.em
import java.util.Scanner
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.core.text.isDigitsOnly
import kotlin.text.compareTo
import kotlin.toString
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.clickable
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

// some modification
class MainActivity : ComponentActivity() {

    val buttonWidth = 150.dp
    val actionbuttonWidth = 60.dp
    val actionbuttonSpacer = 30.dp
    val buttonSpacer = 15.dp
    val blocksSeparator = 25.dp
    val something = 5

    object Stats {
        var hp by mutableStateOf("")
        var tmphp by mutableStateOf("")
        var maxhp by mutableStateOf("")
        var ac by mutableStateOf("")
        var level1 by mutableStateOf("")
        var level2 by mutableStateOf("")
        var level3 by mutableStateOf("")
        var level4 by mutableStateOf("")
        var level5 by mutableStateOf("")
        var level6 by mutableStateOf("")
        var level7 by mutableStateOf("")
        var level8 by mutableStateOf("")
        var level9 by mutableStateOf("")
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                MyApplicationTheme {
                    Scaffold(modifier = Modifier.fillMaxSize())
                    {   innerPadding ->
                        val context = LocalContext.current

                        LaunchedEffect(Unit) {
                            Stats.hp = get_value(context, "hp")
                            Stats.tmphp = get_value(context, "tmphp")
                            Stats.maxhp = get_value(context, "maxhp")
                            Stats.ac = get_value(context, "ac")
                            Stats.level1 = get_value(context, "level1")
                            Stats.level2 = get_value(context, "level2")
                            Stats.level3 = get_value(context, "level3")
                            Stats.level4 = get_value(context, "level4")
                            Stats.level5 = get_value(context, "level5")
                            Stats.level6 = get_value(context, "level6")
                            Stats.level7 = get_value(context, "level7")
                            Stats.level8 = get_value(context, "level8")
                            Stats.level9 = get_value(context, "level9")
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

                                GreetingImage()
                            }
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MakeHealthLine("HP", "Tmp.HP")
                                Spacer(modifier = Modifier.height(blocksSeparator))
                                MakeParametrsChangersLine("Heal", "Tmp.HP")
                                Spacer(modifier = Modifier.height(blocksSeparator))
                                MakeParametrsChangersLine("Take Dmg.")
                                Spacer(modifier = Modifier.height(blocksSeparator))
                                Text("Spells", fontSize = 6.em)
                                Spacer(modifier = Modifier.height(blocksSeparator))

                                MakeSpellsline("Level 1", "Level 2")
                                MakeSpellsline("Level 3", "Level 4")
                                MakeSpellsline("Level 5", "Level 6")
                                MakeSpellsline("Level 7", "Level 8")
                                MakeSpellsline("Level 9")
                            }
                        }
                    }
                }
            }
        }

    @Composable
    fun MakeParametrsChangersLine(name1: String = "None", name2: String = "None"){
        Row{
            ParametrChanger(name1)
            Spacer(modifier = Modifier.width(15.dp))
            ParametrChanger(name2)
        }
    }

    @Composable
    fun ParametrChanger(name: String){
        val context = LocalContext.current
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != "None") {
            if (isOverlayVisible) {
                when (name){
                    "Heal" -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    "Take Dmg." -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)

                    "Tmp.HP" -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    "Max HP" -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    "AC" -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    "HP" -> ChangerWindow(onClose = {isOverlayVisible = false}, name = name)
                }
            } else
            {
                Column {
                    Button(
                        onClick = {isOverlayVisible = true },
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text(name)
                    }
                }
            }
        }
    }
    @Composable
    fun ChangerWindow(onClose: () -> Unit, name: String = "None"){
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
            usePlatformDefaultWidth = false))
        {
            Surface(
                modifier = Modifier.fillMaxSize())
            {
                val context = LocalContext.current
                var text by rememberSaveable { mutableStateOf("") }

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick ={if (text.isDigitsOnly() && text.isNotEmpty()) {
                                // Видалено зайві дужки, які блокували виконання
                                when (name) {
                                    "Take Dmg." -> TakeDamage(text)
                                    "Heal" -> AddHP("hp", text)
                                    "Tmp.HP" -> SetStats("tmphp", text)
                                    "Max HP" -> SetStats("maxhp", text)
                                    "AC" -> SetStats("ac", text)
                                    "HP" -> SetStats("hp", text)
                                }
                                onClose()
                            } else {
                                sendNotification(context, "Error", "Numbers only")
                                onClose()
                            }
                            })
                        {
                            Text("Enter")
                        }
                    }
                }

            }
        }



    @Composable
    fun StableCounter(name: String = "None") {
        val context = LocalContext.current
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != "None") {
            if (isOverlayVisible) {
                ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
            } else
            {
            val value = when (name){
                "HP" -> Stats.hp
                "Tmp.HP" -> Stats.tmphp
                else -> "0"
            }
            fun setCurrent(newValue: String) {
                when (name) {
                    "HP" -> {
                        Stats.hp = newValue
                        set_value(context, "hp", newValue)
                    }
                    "Tmp.HP" -> {
                        Stats.tmphp = newValue
                        set_value(context, "tmphp", newValue)
                    }
                }
            }
            Column {
                Button(
                    onClick = {},
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text(name)
                    Spacer(modifier = Modifier.width(buttonSpacer))
                    Text(value)
                }
                Row {
                    Button(
                        onClick = { if (value.toInt() > 0)  setCurrent((value.toInt()-1).toString())  },
                        modifier = Modifier.width(actionbuttonWidth)
                    ) {
                        Text("-")
                    }
                    Spacer(modifier = Modifier.width(actionbuttonSpacer))
                    Button(
                        onClick = {if(
                            name == "HP" && (value.toInt() < Stats.maxhp.toInt())
                        )
                        {
                            setCurrent((value.toInt() + 1).toString()) }
                        },
                        modifier = Modifier.width(actionbuttonWidth)
                    ) {
                        Text("+")
                    }
                }
            }
        }}
    }

    @Composable
    fun Spells(onClose: () -> Unit) {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text("Not ready")
            }
        }
    }

    @Composable
    fun MakeHealthLine(name1: String = "None", name2: String = "None"){
        Row{
            StableCounter(name1)
            Spacer(modifier = Modifier.width(15.dp))
            StableCounter(name2)
        }
    }

    @Composable
    fun SpellsCounter(modifier: Modifier = Modifier, name: String = "None",) {
        val context = LocalContext.current
        var isOverlayVisible by remember { mutableStateOf(false) }
            val value = when (name){
                "Level 1" -> Stats.level1
                "Level 2" -> Stats.level2
                "Level 3" -> Stats.level3
                "Level 4" -> Stats.level4
                "Level 5" -> Stats.level5
                "Level 6" -> Stats.level6
                "Level 7" -> Stats.level7
                "Level 8" -> Stats.level8
                "Level 9" -> Stats.level9
                else -> "0"
            }
        fun setCurrent(newValue: String) {
            var tmp = ""
            when (name) {
                "Level 1" -> {
                    Stats.level1 = newValue
                    set_value(context, "level1", newValue)
                }
                "Level 2" -> {
                    Stats.level2 = newValue
                    set_value(context, "level2", newValue)
                }
                "Level 3" -> {
                    Stats.level3 = newValue
                    set_value(context, "level3", newValue)
                }
                "Level 4" -> {
                    Stats.level4 = newValue
                    set_value(context, "level4", newValue)
                }
                "Level 5" -> {
                    Stats.level5 = newValue
                    set_value(context, "level5", newValue)
                }
                "Level 6" -> {
                    Stats.level6 = newValue
                    set_value(context, "level6", newValue)
                }
                "Level 7" -> {
                    Stats.level7 = newValue
                    set_value(context, "level7", newValue)
                }
                "Level 8" -> {
                    Stats.level8 = newValue
                    set_value(context, "level8", newValue)
                }
                "Level 9" -> {
                    Stats.level9 = newValue
                    set_value(context, "level9", newValue)
                }
            }

        }
        if (name != "None"){
            if (isOverlayVisible) {
                Spells (onClose = { isOverlayVisible = false })
            } else
            {
                Column{
                    Button(
                        onClick = { isOverlayVisible = true },
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text(name)
                        Spacer(modifier = Modifier.width(buttonSpacer))
                        Text(value)
                    }

                    Row{
                        Button(
                            onClick = { if (value.toInt() > 0) setCurrent((value.toInt() - 1).toString())  },
                            modifier = modifier.width(actionbuttonWidth)
                        ) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(actionbuttonSpacer))
                        Button(
                            onClick = { setCurrent((value.toInt() + 1).toString()) },
                            modifier = Modifier.width(actionbuttonWidth)
                        ) {
                            Text("+")
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun MakeSpellsline(name1: String = "None", name2: String = "None"){
        Row{
            SpellsCounter( modifier = Modifier,name1)
            Spacer(modifier = Modifier.width(buttonSpacer))
            SpellsCounter(modifier = Modifier, name2)
        }
        Spacer(modifier = Modifier.height(buttonSpacer))
    }

    @Composable
    fun ShowStats(onClose: () -> Unit){
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (isOverlayVisible) {
            ChangerWindow(onClose = { isOverlayVisible = false }, name = "Max HP")
        }
        else {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        )   
        {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) 
                {
                    Text("Max HP: " + Stats.maxhp)
                    Spacer(modifier = Modifier.height(buttonSpacer))
                    Text("HP: " + Stats.hp)
                    Spacer(modifier = Modifier.height(buttonSpacer))
                    Text("Tmp.HP: " + Stats.tmphp)
                    Spacer(modifier = Modifier.height(buttonSpacer))
                    Text("AC: " + Stats.ac)
                    Spacer(modifier = Modifier.height(buttonSpacer))
                    MakeParametrsChangersLine("Max HP", "HP")
                    Spacer(modifier = Modifier.height(buttonSpacer))
                    MakeParametrsChangersLine("AC", "Tmp.HP")
                    Spacer(modifier = Modifier.height(buttonSpacer))
                    Button(onClick = onClose){Text("Exit")}
                }
            }
        }
    }}

    // Non @Composable

    fun SetStats(name: String, value: String){
        when(name){
            "maxhp" -> {
                Stats.maxhp = value
                set_value(this@MainActivity, "maxhp", value)
            }
            "ac" -> {
                Stats.ac = value
                set_value(this@MainActivity, "ac", value)
            }
            "hp" -> {
                if (value.toInt() < Stats.maxhp.toInt()) {
                    Stats.hp = value
                    set_value(this@MainActivity, "hp", value)
                } else {
                    Stats.hp = Stats.maxhp
                    set_value(this@MainActivity, "hp", Stats.maxhp)
                }
            }
            "tmphp" -> {
                    Stats.tmphp = value
                    set_value(this@MainActivity, "tmphp", value)
            }
            else -> return
        }

    }

    fun TakeDamage(value: String){
        if (!value.isDigitsOnly() || value.isBlank()) return
        var damage = value.toInt()
        val tmp = Stats.tmphp.toIntOrNull() ?: 0
        val hp = Stats.hp.toIntOrNull() ?: 0

        if (tmp >= damage) {
            Stats.tmphp = (tmp - damage).toString()
        }
        else{
            val leftover = damage - tmp
            Stats.tmphp = 0.toString()
            Stats.hp = (hp - leftover).coerceAtLeast(0).toString()
        }
        if (Stats.hp.toInt() < 0){
            Stats.hp = "0"
        }
        set_value(this@MainActivity, "tmphp", Stats.tmphp)
        set_value(this@MainActivity, "hp", Stats.hp)
    }

    fun AddHP( name: String, value: String) {
        if(value.toInt()>0)
        {
            when (name){
                "hp" ->{
                    Stats.hp = (Stats.hp.toInt() + value.toInt()).toString()
                    set_value(this@MainActivity, "hp", Stats.hp)
                }
                "tmphp" -> {
                    Stats.tmphp = (Stats.tmphp.toInt() + value.toInt()).toString()
                    set_value(this@MainActivity, "tmphp", Stats.tmphp)
                }
            }

        }
        if(Stats.hp.toInt() > Stats.maxhp.toInt()){
            Stats.hp = Stats.maxhp
            set_value(this@MainActivity, "hp", Stats.hp)
        }
    }

    // Siders

    @Composable
    fun GreetingImage() {
        val image = painterResource(R.drawable.frog)
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (isOverlayVisible)
        {
            ShowStats(onClose = { isOverlayVisible = false })
        }
        else
        { 
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(400.dp)
                .padding(bottom = 20.dp)
                .clickable 
                {
                    isOverlayVisible = true
                }
            )
        }
    }

    fun sendNotification(context: Context, title: String, message: String) {
        val channelId = "game_notifications"
        val notificationId = 1

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Створюємо канал для нових версій Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Game Events",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Сповіщення про події в грі"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Будуємо сповіщення
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.frog) // Можна замінити на свій frog icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}

