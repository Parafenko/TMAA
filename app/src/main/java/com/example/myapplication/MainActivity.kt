package com.example.myapplication

//05.03 16.17
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.text.isDigitsOnly
import androidx.compose.foundation.clickable
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.mutableIntStateOf
import androidx.core.app.NotificationCompat

class MainActivity : ComponentActivity() {

    val buttonWidth = 150.dp
    val actionbuttonWidth = 60.dp
    val actionbuttonSpacer = 30.dp
    val buttonSpacer = 15.dp
    val blocksSeparator = 25.dp

    enum class Names(val exp: String, val value: String){
        HP("HP", value = "hp"),
        MaxHP("Max HP", value = "maxhp"),
        TmpHP("Tmp.HP", value = "tmphp"),
        AC("AC", value = "ac"),
        TakeDamage("Take Dmg.", value = ""),
        Heal("Heal", value = ""),

    }
    enum class Levels(val exp: String){
        Level1("Level 1"),
        Level2("Level 2"),
        Level3("Level 3"),
        Level4("Level 4"),
        Level5("Level 5"),
        Level6("Level 6"),
        Level7("Level 7"),
        Level8("Level 8"),
        Level9("Level 9")

    }

    object Stats {
        var hp     by mutableIntStateOf(0 )
        var tmphp  by mutableIntStateOf(0 )
        var maxhp  by mutableIntStateOf(0 )
        var ac     by mutableIntStateOf(0 )
        var level1 by mutableIntStateOf(0 )
        var level2 by mutableIntStateOf(0 )
        var level3 by mutableIntStateOf(0 )
        var level4 by mutableIntStateOf(0 )
        var level5 by mutableIntStateOf(0 )
        var level6 by mutableIntStateOf(0 )
        var level7 by mutableIntStateOf(0 )
        var level8 by mutableIntStateOf(0 )
        var level9 by mutableIntStateOf(0 )
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
                            GetValue(context, "hp").also { Stats.hp = it }
                            Stats.tmphp  = GetValue(context, "tmphp")
                            Stats.maxhp  = GetValue(context, "maxhp")
                            Stats.ac     = GetValue(context, "ac")
                            Stats.level1 = GetValue(context, "level1")
                            Stats.level2 = GetValue(context, "level2")
                            Stats.level3 = GetValue(context, "level3")
                            Stats.level4 = GetValue(context, "level4")
                            Stats.level5 = GetValue(context, "level5")
                            Stats.level6 = GetValue(context, "level6")
                            Stats.level7 = GetValue(context, "level7")
                            Stats.level8 = GetValue(context, "level8")
                            Stats.level9 = GetValue(context, "level9")
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
                                MakeHealthLine(Names.HP.exp, Names.TmpHP.exp)
                                Spacer(modifier = Modifier.height(blocksSeparator))
                                MakeParametrsChangersLine(Names.Heal.exp, Names.TmpHP.exp)
                                Spacer(modifier = Modifier.height(blocksSeparator))
                                MakeParametrsChangersLine(Names.TakeDamage.exp)
                                Spacer(modifier = Modifier.height(blocksSeparator))
                                Text("Spells", fontSize = 6.em)
                                Spacer(modifier = Modifier.height(blocksSeparator))

                                MakeSpellsline(Levels.Level1.exp, Levels.Level2.exp)
                                MakeSpellsline(Levels.Level3.exp, Levels.Level4.exp)
                                MakeSpellsline(Levels.Level5.exp, Levels.Level6.exp)
                                MakeSpellsline(Levels.Level7.exp, Levels.Level8.exp)
                                MakeSpellsline(Levels.Level9.exp)
                            }
                        }
                    }
                }
            }
        }

    @Composable
    fun MakeParametrsChangersLine(name1: String? = null, name2: String? = null){
        Row{
            ParametrChanger(name1)
            Spacer(modifier = Modifier.width(15.dp))
            ParametrChanger(name2)
        }
    }

    @Composable
    fun ParametrChanger(name: String? = null ){
        //val context = LocalContext.current
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != null) {
            if (isOverlayVisible) {
                when (name){

                    Names.Heal.exp -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    Names.TakeDamage.exp -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)

                    Names.TmpHP.exp -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    Names.MaxHP.exp -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    Names.AC.exp -> ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
                    Names.HP.exp -> ChangerWindow(onClose = {isOverlayVisible = false}, name = name)
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
    fun ChangerWindow(onClose: () -> Unit, name: String? = null){
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
                                    Names.TakeDamage.exp -> takeDamage(text.toInt())
                                    Names.Heal.exp -> addHP(Names.HP.value, text.toInt())
                                    Names.TmpHP.exp -> setStats(Names.TmpHP.value, text.toInt())
                                    Names.MaxHP.exp -> setStats(Names.MaxHP.value, text.toInt())
                                    Names.AC.exp -> setStats(Names.AC.value, text.toInt())
                                    Names.HP.exp -> setStats(Names.HP.value, text.toInt())
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
    fun MakeHealthLine(name1: String? = null, name2: String? = null){
        Row{
            StableCounter(name1)
            Spacer(modifier = Modifier.width(15.dp))
            StableCounter(name2)
        }
    }

    @Composable
    fun StableCounter(name: String? = null) {
        val context = LocalContext.current
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != null) {
            if (isOverlayVisible) {
                ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
            } else
            {
                val value = when (name){
                    Names.HP.exp -> Stats.hp
                    Names.TmpHP.exp -> Stats.tmphp
                    else -> 0
                }
                fun setCurrent(newValue: Int) {
                    when (name) {
                        Names.HP.exp -> {
                            Stats.hp = newValue
                            SetValue(context, Names.HP.value, newValue)
                        }
                        Names.TmpHP.exp -> {
                            Stats.tmphp = newValue
                            SetValue(context, Names.TmpHP.value, newValue)
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
                        Text(value.toString())
                    }
                    Row {
                        Button(
                            onClick = { if (value > 0)  setCurrent(value-1)  },
                            modifier = Modifier.width(actionbuttonWidth)
                        ) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(actionbuttonSpacer))
                        Button(
                            onClick = {
                                if(name == Names.HP.exp && (value < Stats.maxhp)
                                ){
                                    setCurrent(value + 1)
                                }
                                if (name == Names.TmpHP.exp)
                                {
                                    setCurrent(value + 1)
                                }
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
    fun SpellsCounter(modifier: Modifier = Modifier, name: String? = null) {
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
                else -> 0
            }
       fun setCurrent(newValue: Int) {
            when (name) {
                "Level 1" -> {
                    Stats.level1 = newValue
                    SetValue(context, "level1", newValue)
                }
                "Level 2" -> {
                    Stats.level2 = newValue
                    SetValue(context, "level2", newValue)
                }
                "Level 3" -> {
                    Stats.level3 = newValue
                    SetValue(context, "level3", newValue)
                }
                "Level 4" -> {
                    Stats.level4 = newValue
                    SetValue(context, "level4", newValue)
                }
                "Level 5" -> {
                    Stats.level5 = newValue
                    SetValue(context, "level5", newValue)
                }
                "Level 6" -> {
                    Stats.level6 = newValue
                    SetValue(context, "level6", newValue)
                }
                "Level 7" -> {
                    Stats.level7 = newValue
                    SetValue(context, "level7", newValue)
                }
                "Level 8" -> {
                    Stats.level8 = newValue
                    SetValue(context, "level8", newValue)
                }
                "Level 9" -> {
                    Stats.level9 = newValue
                    SetValue(context, "level9", newValue)
                }
            }

        }
        if (name != null){
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
                        Text(value.toString())
                    }

                    Row{
                        Button(
                            onClick = { if (value > 0) setCurrent(value - 1) },
                            modifier = modifier.width(actionbuttonWidth)
                        ) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(actionbuttonSpacer))
                        Button(
                            onClick = { setCurrent(value + 1) },
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
    fun MakeSpellsline(name1: String? = null, name2: String? = null){
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
            ChangerWindow(onClose = { isOverlayVisible = false }, name = Names.MaxHP.exp)
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
                    Text("Max HP: ${Stats.maxhp}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Text("HP: ${Stats.hp}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Text("Tmp.HP: ${Stats.tmphp}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Text("AC: ${Stats.ac}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    MakeParametrsChangersLine(Names.MaxHP.exp, Names.HP.exp)

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    MakeParametrsChangersLine(Names.AC.exp, Names.TmpHP.exp)

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Button(onClick = onClose){Text("Exit")}
                }
            }
        }
    }}

    // Non @Composable

    fun setStats(name: String, value: Int){
        when(name){
            Names.MaxHP.value -> {
                Stats.maxhp = value
                SetValue(this@MainActivity, Names.MaxHP.value, value)
            }
            Names.AC.value -> {
                Stats.ac = value
                SetValue(this@MainActivity, Names.AC.value, value)
            }
            Names.HP.value -> {
                if (value < Stats.maxhp) {
                    Stats.hp = value
                    SetValue(this@MainActivity, Names.HP.value, value)
                } else {
                    Stats.hp = Stats.maxhp
                    SetValue(this@MainActivity, Names.HP.value, Stats.maxhp)
                }
            }
            Names.TmpHP.value -> {
                    Stats.tmphp = value
                    SetValue(this@MainActivity, Names.TmpHP.value, value)
            }
            else -> return
        }

    }

    fun takeDamage(value: Int){
        val damage = value
        val tmp = Stats.tmphp //?: 0
        val hp = Stats.hp

        if (tmp >= damage) {
            Stats.tmphp = tmp - damage
        }
        else{
            val leftover = damage - tmp
            Stats.tmphp = 0
            Stats.hp = hp - leftover.coerceAtLeast(0)
        }
        if (Stats.hp < 0){
            Stats.hp = 0
        }
        SetValue(this@MainActivity, Names.TmpHP.value, Stats.tmphp)
        SetValue(this@MainActivity, Names.HP.value, Stats.hp)
    }

    fun addHP( name: String, value: Int) {
        if(value>0)
        {
            when (name){
                Names.HP.value ->{
                    Stats.hp += value
                    SetValue(this@MainActivity, Names.HP.value, Stats.hp)
                }
                Names.TmpHP.value -> {
                    Stats.tmphp += value
                    SetValue(this@MainActivity, Names.TmpHP.value, Stats.tmphp)
                }
            }

        }
        if(Stats.hp > Stats.maxhp){
            Stats.hp = Stats.maxhp
            SetValue(this@MainActivity, Names.HP.value, Stats.hp)
        //????? //SetValue(this@MainActivity, Names.HP.value, Stats.hp)
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

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

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

