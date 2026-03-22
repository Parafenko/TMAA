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
import android.graphics.drawable.Icon
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults

class MainActivity : ComponentActivity() {

    val buttonWidth = 150.dp
    val actionbuttonWidth = 60.dp
    val actionbuttonSpacer = 35.dp
    val buttonSpacer = 5.dp
    val blocksSeparator = 25.dp
    val addersseparator = 15.dp

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
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-name"
        )
            .allowMainThreadQueries()
            .build()

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

                            GreetingImage()
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            MakeHealthLine(Names.HP.value, Names.TmpHP.value)
                            Spacer(modifier = Modifier.height(blocksSeparator))
                            MakeParametersChangersLine(Names.Heal.value, Names.TmpHP.value)
                            Spacer(modifier = Modifier.height(blocksSeparator))
                            MakeParametersChangersLine(Names.TakeDamage.value)
                            Spacer(modifier = Modifier.height(blocksSeparator))
                            Text("Spells", fontSize = 6.em)
                            Spacer(modifier = Modifier.height(blocksSeparator))

                            MakeSpellsLine(Levels.Level1.exp, Levels.Level2.exp)
                            MakeSpellsLine(Levels.Level3.exp, Levels.Level4.exp)
                            MakeSpellsLine(Levels.Level5.exp, Levels.Level6.exp)
                            MakeSpellsLine(Levels.Level7.exp, Levels.Level8.exp)
                            MakeSpellsLine(Levels.Level9.exp)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MakeParametersChangersLine(name1: String? = null, name2: String? = null){
        Row{
            ParameterChanger(name1)
            Spacer(modifier = Modifier.width(15.dp))
            ParameterChanger(name2)
        }
    }

    @Composable
    fun ParameterChanger(name: String? = null ){
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != null) {
            if (isOverlayVisible) {
                ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
            }
            else
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
                        onClick ={if (text.isDigitsOnly() && text.isNotEmpty())
                            {
                                when (name) {
                                    Names.TakeDamage.value -> takeDamage(text.trim().toInt())
                                    Names.Heal.value -> addHP(Names.HP.value, text.trim().toInt())
                                    Names.TmpHP.value -> setStats(Names.TmpHP.value, text.trim().toInt())
                                    Names.MaxHP.value -> setStats(Names.MaxHP.value, text.trim().toInt())
                                    Names.AC.value -> setStats(Names.AC.value, text.trim().toInt())
                                    Names.HP.value -> setStats(Names.HP.value, text.trim().toInt())
                                }
                                onClose()
                            }
                            else
                            {
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
    fun AddSpell(onClose: () -> Unit, level: Int){
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false))
        {
            Surface(
                modifier = Modifier.fillMaxSize())
            {
                var name by rememberSaveable { mutableStateOf("") }
                var level by rememberSaveable { mutableStateOf("") }
                var description by rememberSaveable { mutableStateOf("") }
                var components by rememberSaveable { mutableStateOf("") }
                var duration  by rememberSaveable { mutableStateOf("") }
                var casttime by rememberSaveable { mutableStateOf("") }
                var distance by rememberSaveable { mutableStateOf("") }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Column{
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            singleLine = false,
                            modifier = Modifier.width(300.dp),
                            placeholder = { Text("Name") }
                        )   //name
                        Spacer(modifier = Modifier.height(addersseparator))
                        TextField(
                            value = level ,
                            onValueChange = {level = it},
                            singleLine = false,
                            modifier = Modifier.width(300.dp),
                            placeholder = {Text("Level (number)")}
                        )   //level
                        Spacer(modifier = Modifier.height(addersseparator))
                        TextField(
                            value = description ,
                            onValueChange = {description = it},
                            singleLine = false,
                            modifier = Modifier.height(140.dp).width(300.dp),
                            placeholder = {Text("Description")}
                        )   //description
                        Spacer(modifier = Modifier.height(addersseparator))
                        TextField(
                            value = components ,
                            onValueChange = {components = it},
                            singleLine = false,
                            modifier = Modifier.width(300.dp),
                            placeholder = {Text("Components")}
                        )   //components
                        Spacer(modifier = Modifier.height(addersseparator))
                        TextField(
                            value = duration,
                            onValueChange = {duration = it},
                            singleLine = false,
                            modifier = Modifier.width(300.dp),
                            placeholder = {Text("Duration (number)")}
                        )   //duration
                        Spacer(modifier = Modifier.height(addersseparator))
                        TextField(
                            value = casttime,
                            onValueChange = {casttime = it},
                            singleLine = false,
                            modifier = Modifier.width(300.dp),
                            placeholder = {Text("Time to cast (number)")}
                        )   //time
                        Spacer(modifier = Modifier.height(addersseparator))
                        TextField(
                            value = distance,
                            onValueChange = {distance = it},
                            singleLine = false,
                            modifier = Modifier.width(300.dp),
                            placeholder = {Text("Distance (number)")}
                        )   //distance
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick =
                        {
                            if (name.isNotBlank()) {
                                spellDao.insert(
                                    Spells(
                                        name = name.trim(),
                                        level = level.trim().toInt(),
                                        description = description.trim(),
                                        components = components.trim(),
                                        duration = duration.trim().toInt(),
                                        casttime = casttime.trim().toInt(),
                                        distance = distance.trim().toInt()
                                    )
                                )
                                spells.clear()
                                spells.addAll(spellDao.getAll())
                            }
                            onClose()
                        }
                    )
                {
                    Text("Enter")
                }
            }}
        }
    }

    @Composable
    fun SpellsDialog(onClose: () -> Unit, level: Int) {
        var isOverlayVisible by remember { mutableStateOf(false) }
        val leveledSpells: List<Spells> = getSpellsbyLevel(level)
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                leveledSpells.forEach({ spell ->
                    Text("${spell.uid}, ${spell.name}, ${spell.level}")
                })
                Spacer(modifier = Modifier.height(blocksSeparator))
                Button(
                    onClick = { isOverlayVisible = true },
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Add")
                }
                if (isOverlayVisible) {
                    AddSpell(onClose = { isOverlayVisible = false }, level)
                }
                }
            }
        }
    }



    fun getSpellsbyLevel(level: Int):List<Spells>{
        var newlist = mutableListOf<Spells>()
        spells.forEach({
            if (it.level == level) {
                newlist.add(it)
            }
        })
        return newlist
    }


    @Composable
    fun MakeHealthLine(name1: String? = null, name2: String? = null){
        Row{
            HealthCounter(name1)
            Spacer(modifier = Modifier.width(15.dp))
            HealthCounter(name2)
        }
    }

    @Composable
    fun HealthCounter(name: String? = null) {
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != null) {
            if (isOverlayVisible) {
                ChangerWindow(onClose = { isOverlayVisible = false }, name = name)
            } else
            {
                var hp = false
                var value = 0
                when (name){
                    Names.HP.value -> {value = Stats.hp.current; hp = true}
                    Names.TmpHP.value -> value = Stats.tmphp.current
                }
                fun setCurrent(newValue: Int) {
                    when (name) {
                        Names.HP.value -> {
                            Stats.hp.current = newValue
                            setValuebyUser(user, userDao, Names.HP.value, newValue)
                        }
                        Names.TmpHP.value -> {
                            Stats.tmphp.current = newValue
                            setValuebyUser(user, userDao, Names.TmpHP.value, newValue)
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

                        Text(if(hp){"${value}/${(Stats.hp.maxvalue ?: 0)}"}
                            else {value.toString()})
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
                                if(name == Names.HP.value && (value < (Stats.hp.maxvalue ?: 0) )
                                ){
                                    setCurrent(value + 1)
                                }
                                if (name == Names.TmpHP.value)
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
            }
        }
    }

    @Composable
    fun SpellsCounter(modifier: Modifier = Modifier, name: String? = null) {
        var isOverlayVisible by remember { mutableStateOf(false) }
        var maxvalue = 0
        var value = 0
        var openedLevel =0
        when (name){
            Levels.Level1.exp -> {value = Stats.level1.current; maxvalue = Stats.level1.maxvalue?:0; openedLevel = 1}
            Levels.Level2.exp -> {value = Stats.level2.current; maxvalue = Stats.level2.maxvalue?:0; openedLevel = 2}
            Levels.Level3.exp -> {value = Stats.level3.current; maxvalue = Stats.level3.maxvalue?:0; openedLevel = 3}
            Levels.Level4.exp -> {value = Stats.level4.current; maxvalue = Stats.level4.maxvalue?:0; openedLevel = 4}
            Levels.Level5.exp -> {value = Stats.level5.current; maxvalue = Stats.level5.maxvalue?:0; openedLevel = 5}
            Levels.Level6.exp -> {value = Stats.level6.current; maxvalue = Stats.level6.maxvalue?:0; openedLevel = 6}
            Levels.Level7.exp -> {value = Stats.level7.current; maxvalue = Stats.level7.maxvalue?:0; openedLevel = 7}
            Levels.Level8.exp -> {value = Stats.level8.current; maxvalue = Stats.level8.maxvalue?:0; openedLevel = 8}
            Levels.Level9.exp -> {value = Stats.level9.current; maxvalue = Stats.level9.maxvalue?:0; openedLevel = 9}
        }
        fun setCurrent(newValue: Int) {
            when (name) {
                Levels.Level1.exp -> {
                    Stats.level1.current = newValue
                    setValuebyUser(user, userDao, Levels.Level1.value, newValue)
                }
                Levels.Level2.exp -> {
                    Stats.level2.current = newValue
                    setValuebyUser(user, userDao, Levels.Level2.value, newValue)
                }
                Levels.Level3.exp -> {
                    Stats.level3.current = newValue
                    setValuebyUser(user, userDao, Levels.Level3.value, newValue)
                }
                Levels.Level4.exp -> {
                    Stats.level4.current = newValue
                    setValuebyUser(user, userDao, Levels.Level4.value, newValue)
                }
                Levels.Level5.exp -> {
                    Stats.level5.current = newValue
                    setValuebyUser(user, userDao, Levels.Level5.value, newValue)
                }
                Levels.Level6.exp -> {
                    Stats.level6.current = newValue
                    setValuebyUser(user, userDao, Levels.Level6.value, newValue)
                }
                Levels.Level7.exp -> {
                    Stats.level7.current = newValue
                    setValuebyUser(user, userDao, Levels.Level7.value, newValue)
                }
                Levels.Level8.exp -> {
                    Stats.level8.current = newValue
                    setValuebyUser(user, userDao, Levels.Level8.value, newValue)
                }
                Levels.Level9.exp -> {
                    Stats.level9.current = newValue
                    setValuebyUser(user, userDao, Levels.Level9.value, newValue)
                }
            }
        }
        if (name != null){
            if (isOverlayVisible) {
                SpellsDialog (onClose = { isOverlayVisible = false }, level = openedLevel)
            } else
            {
                Column{
                    Button(
                        onClick = { isOverlayVisible = true },
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text(name)
                        Spacer(modifier = Modifier.width(buttonSpacer))
                        Text("${value}/${maxvalue}")
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
                            onClick = {if (value < maxvalue) setCurrent(value + 1) },
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
    fun MakeSpellsLine(name1: String? = null, name2: String? = null){
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
            ChangerWindow(onClose = { isOverlayVisible = false }, name = Names.MaxHP.value)
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
                    Text("${Names.MaxHP.value}: ${Stats.hp.maxvalue}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Text("${Names.HP.value}: ${Stats.hp.current}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Text("${Names.TmpHP.value}: ${Stats.tmphp.current}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Text("${Names.AC.value}: ${Stats.ac.current}")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    MakeParametersChangersLine(Names.MaxHP.value, Names.AC.value)

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    MakeMaxSpellChanger("Levels value")

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    MakeCloudLine()

                    Spacer(modifier = Modifier.height(buttonSpacer))

                    Button(onClick = onClose){Text("Exit")}

                }
            }
        }
    }
    }

    @Composable
    fun MakeMaxSpellChanger(name: String? = null) {
        var isOverlayVisible by remember { mutableStateOf(false) }

        if (name != null) {
            Column {
                Button(
                    onClick = { isOverlayVisible = true },
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text(name)
                }
            }
        }

        if (isOverlayVisible) {
            MaxSpellListChanger(
                onClose = { isOverlayVisible = false }
            )
        }
    }

    @Composable
    fun MaxSpellListChanger(onClose: () -> Unit) {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier

                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    MakeMaxSpellsLine(Levels.Level1.maxvalue, Levels.Level2.maxvalue)
                    MakeMaxSpellsLine(Levels.Level3.maxvalue, Levels.Level4.maxvalue)
                    MakeMaxSpellsLine(Levels.Level5.maxvalue, Levels.Level6.maxvalue)
                    MakeMaxSpellsLine(Levels.Level7.maxvalue, Levels.Level8.maxvalue)
                    MakeMaxSpellsLine(Levels.Level9.maxvalue)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = onClose) {
                        Text("Close")
                    }
                }
            }
        }
    }
    @Composable
    fun MakeMaxSpellsLine(name1: String? = null, name2: String? = null){
        Row{
            MaxSpellChanger( modifier = Modifier,name1)
            Spacer(modifier = Modifier.width(buttonSpacer))
            MaxSpellChanger(modifier = Modifier, name2)
        }
        Spacer(modifier = Modifier.height(buttonSpacer))
    }
    @Composable
    fun MaxSpellChanger(modifier: Modifier = Modifier, name:String? = null){
        var isOverlayVisible by remember { mutableStateOf(false) }
        var maxvalue = 0
        var level = Levels.Level1
        when (name){
            Levels.Level1.maxvalue -> {maxvalue = Stats.level1.maxvalue?:0; level = Levels.Level1}
            Levels.Level2.maxvalue -> {maxvalue = Stats.level2.maxvalue?:0; level = Levels.Level2}
            Levels.Level3.maxvalue -> {maxvalue = Stats.level3.maxvalue?:0; level = Levels.Level3}
            Levels.Level4.maxvalue -> {maxvalue = Stats.level4.maxvalue?:0; level = Levels.Level4}
            Levels.Level5.maxvalue -> {maxvalue = Stats.level5.maxvalue?:0; level = Levels.Level5}
            Levels.Level6.maxvalue -> {maxvalue = Stats.level6.maxvalue?:0; level = Levels.Level6}
            Levels.Level7.maxvalue -> {maxvalue = Stats.level7.maxvalue?:0; level = Levels.Level7}
            Levels.Level8.maxvalue -> {maxvalue = Stats.level8.maxvalue?:0; level = Levels.Level8}
            Levels.Level9.maxvalue -> {maxvalue = Stats.level9.maxvalue?:0; level = Levels.Level9}
        }
        fun setCurrent(newValue: Int) {
            when (name) {
                Levels.Level1.maxvalue -> {
                    Stats.level1.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level1.maxvalue, newValue)
                }
                Levels.Level2.maxvalue-> {
                    Stats.level2.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level2.maxvalue, newValue)
                }
                Levels.Level3.maxvalue -> {
                    Stats.level3.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level3.maxvalue, newValue)
                }
                Levels.Level4.maxvalue -> {
                    Stats.level4.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level4.maxvalue, newValue)
                }
                Levels.Level5.maxvalue -> {
                    Stats.level5.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level5.maxvalue, newValue)
                }
                Levels.Level6.maxvalue -> {
                    Stats.level6.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level6.maxvalue, newValue)
                }
                Levels.Level7.maxvalue -> {
                    Stats.level7.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level7.maxvalue, newValue)
                }
                Levels.Level8.maxvalue -> {
                    Stats.level8.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level8.maxvalue, newValue)
                }
                Levels.Level9.maxvalue -> {
                    Stats.level9.maxvalue = newValue
                    setValuebyUser(user, userDao, Levels.Level9.maxvalue, newValue)
                }
            }

        }
        if (name != null){
            if (isOverlayVisible) {
                //Spells (onClose = { isOverlayVisible = false })
            } else
            {
                Column{
                    Button(
                        onClick = { isOverlayVisible = true },
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text(level.exp)
                        Spacer(modifier = Modifier.width(buttonSpacer))
                        Text(maxvalue.toString())
                    }

                    Row{
                        Button(
                            onClick = { if (maxvalue > 0) setCurrent(maxvalue - 1) },
                            modifier = modifier.width(actionbuttonWidth)
                        ) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(actionbuttonSpacer))
                        Button(
                            onClick = {setCurrent(maxvalue + 1) },
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
    fun MakeCloudLine(){
        Row{
            Button(
                onClick = {cloudSave(this@MainActivity)},
                modifier = Modifier.width(buttonWidth)
            ) {
                Text("Cloud save")
            }
            Spacer(modifier = Modifier.width(15.dp))

            Button( onClick={
                cloudLoad(this@MainActivity){
                saveAllLoadedDataToRoom()}},
                modifier = Modifier.width(buttonWidth)
            ) {
                Text("Cloud load")
            }
        }
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

    // Image + Notification

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

