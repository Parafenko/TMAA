package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.myapplication.MainActivity.Names

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Dp

import com.example.myapplication.MainActivity.Stats
import com.example.myapplication.MainActivity.Levels

class Composable {


    @Composable
    fun MakeParametersChangersLine(
        buttonWidth: Dp,
        name1: String? = null,
        name2: String? = null,
        onTakeDamage: (Int) -> Unit,
        onHeal: (Int) -> Unit,
        onSetTmpHp: (Int) -> Unit,
        onSetMaxHp: (Int) -> Unit,
        onSetAc: (Int) -> Unit,
        onSetHp: (Int) -> Unit,
        onSendNotification: (Context, String, String) -> Unit
    )
    {
        Row{
            ParameterChanger(
                name = name1,
                buttonWidth = buttonWidth,
                onTakeDamage = onTakeDamage,
                onHeal = onHeal,
                onSetTmpHp = onSetTmpHp,
                onSetMaxHp = onSetMaxHp,
                onSetAc = onSetAc,
                onSetHp = onSetHp,
                onsendNotification = onSendNotification
            )
            Spacer(modifier = Modifier.width(15.dp))
            ParameterChanger(
                name = name2,
                buttonWidth = buttonWidth,
                onTakeDamage = onTakeDamage,
                onHeal = onHeal,
                onSetTmpHp = onSetTmpHp,
                onSetMaxHp = onSetMaxHp,
                onSetAc = onSetAc,
                onSetHp = onSetHp,
                onsendNotification = onSendNotification
            )
        }
    }

    @Composable
    fun ParameterChanger(
        name: String? = null,
        buttonWidth: Dp,
        onTakeDamage: (Int) -> Unit,
        onHeal: (Int) -> Unit,
        onSetTmpHp: (Int) -> Unit,
        onSetMaxHp: (Int) -> Unit,
        onSetAc: (Int) -> Unit,
        onSetHp: (Int) -> Unit,
        onsendNotification: (context: Context, title: String, message: String) -> Unit
    ){
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != null) {
            if (isOverlayVisible) {
                ChangerWindow(
                    onClose = { isOverlayVisible = false },
                    name = name,
                    onTakeDamage = onTakeDamage,
                    onHeal = onHeal,
                    onSetTmpHp = onSetTmpHp,
                    onSetMaxHp = onSetMaxHp,
                    onSetAc = onSetAc,
                    onSetHp = onSetHp,
                    onsendNotification = onsendNotification
                )
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
    fun ChangerWindow(
        onClose: () -> Unit,
        name: String? = null,
        onTakeDamage: (Int) -> Unit,
        onHeal: (Int) -> Unit,
        onSetTmpHp: (Int) -> Unit,
        onSetMaxHp: (Int) -> Unit,
        onSetAc: (Int) -> Unit,
        onSetHp: (Int) -> Unit,
        onsendNotification: (context: Context, title: String, message: String) -> Unit
    ) {
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
                            val number = text.trim().toInt()
                            when (name) {
                                Names.TakeDamage.value -> onTakeDamage(number)
                                Names.Heal.value -> onHeal(number)
                                Names.TmpHP.value -> onSetTmpHp(number)
                                Names.MaxHP.value -> onSetMaxHp(number)
                                Names.AC.value -> onSetAc(number)
                                Names.HP.value -> onSetHp(number)
                            }
                            onClose()
                        }
                        else
                        {
                            onsendNotification(context, "Error", "Numbers only")
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
    fun AddSpell(
        onClose: () -> Unit,
        level: Int,
        getSpellsbyLevel: (Int) -> List<Spells>,
        spellAdder: (String, Int, String, String, Int, String, Int) -> Unit
    ){
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false))
        {
            Surface(
                modifier = Modifier.fillMaxSize())
            {
                var name by rememberSaveable { mutableStateOf("") }
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
//                        TextField(
//                            value = level ,
//                            //onValueChange = {level = it},
//                            singleLine = false,
//                            modifier = Modifier.width(300.dp),
//                            placeholder = {Text("Level (number)")}
//                        )   //level
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

                                    spellAdder(
                                        name.trim(),
                                        level,
                                        description.trim(),
                                        components.trim(),
                                        duration.trim().toInt(),
                                        casttime.trim(),
                                        distance.trim().toInt()
                                    )
                                }
                                onClose()
                            }
                    )
                    {
                        Text("Enter")
                    }
                }
            }
        }
    }

    @Composable
    fun SpellsDialog(
            onClose: () -> Unit,
            level: Int,
            getSpellsbyLevel: (Int) -> List<Spells>,
            spellAdder: (String, Int, String, String, Int, String, Int) -> Unit
        ) {
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
                        AddSpell(
                            onClose = { isOverlayVisible = false },
                            level = level,
                            getSpellsbyLevel = getSpellsbyLevel,
                            spellAdder = spellAdder

                        )
                    }
                }
            }
        }
    }


    @Composable
    fun MakeHealthLine(
        name1: String? = null,
        name2: String? = null,
        onTakeDamage: (Int) -> Unit,
        onHeal: (Int) -> Unit,
        onSetTmpHp: (Int) -> Unit,
        onSetMaxHp: (Int) -> Unit,
        onSetAc: (Int) -> Unit,
        onSetHp: (Int) -> Unit,
        onSendNotification: (Context, String, String) -> Unit,
        onvaluesetter: (String, Int) -> Unit
    ){
        Row{
            HealthCounter(
                name = name1,
                buttonWidth = buttonWidth,
                buttonSpacer = buttonSpacer,
                actionbuttonWidth = actionbuttonWidth,
                actionbuttonSpacer = actionbuttonSpacer,
                onTakeDamage = onTakeDamage,
                onHeal = onHeal,
                onSetTmpHp = onSetTmpHp,
                onSetMaxHp = onSetMaxHp,
                onSetAc = onSetAc,
                onSetHp = onSetHp,
                onSendNotification = onSendNotification,
                onvaluesetter = onvaluesetter
            )
            Spacer(modifier = Modifier.width(15.dp))
            HealthCounter(
                name = name2,
                buttonWidth = buttonWidth,
                buttonSpacer = buttonSpacer,
                actionbuttonWidth = actionbuttonWidth,
                actionbuttonSpacer = actionbuttonSpacer,
                onTakeDamage = onTakeDamage,
                onHeal = onHeal,
                onSetTmpHp = onSetTmpHp,
                onSetMaxHp = onSetMaxHp,
                onSetAc = onSetAc,
                onSetHp = onSetHp,
                onSendNotification = onSendNotification,
                onvaluesetter = onvaluesetter
            )
        }
    }

    @Composable
    fun HealthCounter(
        name: String? = null,
        buttonWidth: Dp,
        buttonSpacer: Dp,
        actionbuttonWidth: Dp,
        actionbuttonSpacer: Dp,
        onTakeDamage: (Int) -> Unit,
        onHeal: (Int) -> Unit,
        onSetTmpHp: (Int) -> Unit,
        onSetMaxHp: (Int) -> Unit,
        onSetAc: (Int) -> Unit,
        onSetHp: (Int) -> Unit,
        onSendNotification: (Context, String, String) -> Unit,
        onvaluesetter: (String, Int) -> Unit
    ){
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (name != null) {
            if (isOverlayVisible) {
                ChangerWindow(
                    onClose = { isOverlayVisible = false },
                    name = name,
                    onTakeDamage = onTakeDamage,
                    onHeal = onHeal,
                    onSetTmpHp = onSetTmpHp,
                    onSetMaxHp = onSetMaxHp,
                    onSetAc = onSetAc,
                    onSetHp = onSetHp,
                    onsendNotification = onSendNotification,
                )
            } else
            {
                var hp = false
                var value = 0
                when (name){
                    Names.HP.value -> {  value = Stats.hp.current; hp = true}
                    Names.TmpHP.value -> value = Stats.tmphp.current
                }
                fun setCurrent(newValue: Int) {
                    when (name) {
                        Names.HP.value -> {
                            Stats.hp.current = newValue
                            onvaluesetter (Names.HP.value, newValue)
                        }
                        Names.TmpHP.value -> {
                            Stats.tmphp.current = newValue
                            onvaluesetter (Names.TmpHP.value, newValue)
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
    fun SpellsCounter(
        modifier: Modifier = Modifier,
        name: String? = null,
        onvaluesetter: (String, Int) -> Unit,
        getSpellsbyLevel: (Int) -> List<Spells>,
        spellAdder: (String, Int, String, String, Int, String, Int) -> Unit
    ) {
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
                    onvaluesetter(Levels.Level1.value, newValue)
                }
                Levels.Level2.exp -> {
                    Stats.level2.current = newValue
                    onvaluesetter( Levels.Level2.value, newValue)
                }
                Levels.Level3.exp -> {
                    Stats.level3.current = newValue
                    onvaluesetter( Levels.Level3.value, newValue)
                }
                Levels.Level4.exp -> {
                    Stats.level4.current = newValue
                    onvaluesetter( Levels.Level4.value, newValue)
                }
                Levels.Level5.exp -> {
                    Stats.level5.current = newValue
                    onvaluesetter(Levels.Level5.value, newValue)
                }
                Levels.Level6.exp -> {
                    Stats.level6.current = newValue
                    onvaluesetter(Levels.Level6.value, newValue)
                }
                Levels.Level7.exp -> {
                    Stats.level7.current = newValue
                    onvaluesetter(Levels.Level7.value, newValue)
                }
                Levels.Level8.exp -> {
                    Stats.level8.current = newValue
                    onvaluesetter(Levels.Level8.value, newValue)
                }
                Levels.Level9.exp -> {
                    Stats.level9.current = newValue
                    onvaluesetter(Levels.Level9.value, newValue)
                }
            }
        }
        if (name != null){
            if (isOverlayVisible) {
                SpellsDialog(
                    onClose = { isOverlayVisible = false },
                    level = openedLevel,
                    getSpellsbyLevel = getSpellsbyLevel,
                    spellAdder = spellAdder
                )
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
                            modifier = Modifier.width(actionbuttonWidth)
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
    fun MakeSpellsLine(
        name1: String? = null,
        name2: String? = null,
        onvaluesetter: (String, Int) -> Unit,
        getSpellsbyLevel: (Int) -> List<Spells>,
        spellAdder: (String, Int, String, String, Int, String, Int) -> Unit){
        Row{
            SpellsCounter(
                modifier = Modifier,
                name = name1,
                onvaluesetter = onvaluesetter,
                getSpellsbyLevel = getSpellsbyLevel,
                spellAdder = spellAdder
            )
            Spacer(modifier = Modifier.width(buttonSpacer))
            Spacer(modifier = Modifier.width(buttonSpacer))
            SpellsCounter(
                modifier = Modifier,
                name = name2,
                onvaluesetter = onvaluesetter,
                getSpellsbyLevel = getSpellsbyLevel,
                spellAdder = spellAdder
            )
        }
        Spacer(modifier = Modifier.height(buttonSpacer))
    }

    @Composable
    fun ShowStats(
        onClose: () -> Unit,
        onTakeDamage: (Int) -> Unit,
        onHeal: (Int) -> Unit,
        onSetTmpHp: (Int) -> Unit,
        onSetMaxHp: (Int) -> Unit,
        onSetAc: (Int) -> Unit,
        onSetHp: (Int) -> Unit,
        onSendNotification: (Context, String, String) -> Unit,
        onvaluesetter: (String, Int) -> Unit,
        cloudSaver: () -> Unit,
        cloudLoader: () -> Unit
    ){
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (isOverlayVisible) {
            ChangerWindow(
                onClose = { isOverlayVisible = false }, name = Names.MaxHP.value,
                onTakeDamage,
                onHeal,
                onSetTmpHp,
                onSetMaxHp,
                onSetAc,
                onSetHp,
                onSendNotification

            )
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

                        MakeParametersChangersLine(
                            buttonWidth = buttonWidth,
                            name1 = Names.MaxHP.value,
                            name2 = Names.AC.value,
                            onTakeDamage = onTakeDamage,
                            onHeal = onHeal,
                            onSetTmpHp = onSetTmpHp,
                            onSetMaxHp = onSetMaxHp,
                            onSetAc = onSetAc,
                            onSetHp = onSetHp,
                            onSendNotification = onSendNotification
                        )

                        Spacer(modifier = Modifier.height(buttonSpacer))

                        MakeMaxSpellChanger(
                            name = "Levels value",
                            onvaluesetter = onvaluesetter
                        )
                        Spacer(modifier = Modifier.height(buttonSpacer))

                        MakeCloudLine(
                            cloudSaver = cloudSaver,
                            cloudLoader = cloudLoader
                        )

                        Spacer(modifier = Modifier.height(buttonSpacer))

                        Button(onClick = onClose){Text("Exit")}

                    }
                }
            }
        }
    }

    @Composable
    fun MakeMaxSpellChanger(
        name: String? = null,
        onvaluesetter: (String, Int) -> Unit) {
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
                onClose = { isOverlayVisible = false },
                onvaluesetter = onvaluesetter
            )
        }
    }

    @Composable
    fun MaxSpellListChanger(onClose: () -> Unit, onvaluesetter: (String, Int) -> Unit) {
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

                    MakeMaxSpellsLine(Levels.Level1.maxvalue, Levels.Level2.maxvalue, onvaluesetter)
                    MakeMaxSpellsLine(Levels.Level3.maxvalue, Levels.Level4.maxvalue, onvaluesetter)
                    MakeMaxSpellsLine(Levels.Level5.maxvalue, Levels.Level6.maxvalue, onvaluesetter)
                    MakeMaxSpellsLine(Levels.Level7.maxvalue, Levels.Level8.maxvalue, onvaluesetter)
                    MakeMaxSpellsLine(Levels.Level9.maxvalue, null, onvaluesetter)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = onClose) {
                        Text("Close")
                    }
                }
            }
        }
    }

    @Composable
    fun MakeMaxSpellsLine(
        name1: String? = null,
        name2: String? = null,
        onvaluesetter: (String, Int) -> Unit){
        Row{
            MaxSpellChanger( modifier = Modifier,name1, onvaluesetter)
            Spacer(modifier = Modifier.width(buttonSpacer))
            MaxSpellChanger(modifier = Modifier,name2, onvaluesetter)
        }
        Spacer(modifier = Modifier.height(buttonSpacer))
    }

    @Composable
    fun MaxSpellChanger(
        modifier: Modifier = Modifier,
        name:String? = null,
        onvaluesetter: (String, Int) -> Unit){
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
                    onvaluesetter(Levels.Level1.maxvalue, newValue)
                }
                Levels.Level2.maxvalue-> {
                    Stats.level2.maxvalue = newValue
                    onvaluesetter(Levels.Level2.maxvalue, newValue)
                }
                Levels.Level3.maxvalue -> {
                    Stats.level3.maxvalue = newValue
                    onvaluesetter( Levels.Level3.maxvalue, newValue)
                }
                Levels.Level4.maxvalue -> {
                    Stats.level4.maxvalue = newValue
                    onvaluesetter( Levels.Level4.maxvalue, newValue)
                }
                Levels.Level5.maxvalue -> {
                    Stats.level5.maxvalue = newValue
                    onvaluesetter( Levels.Level5.maxvalue, newValue)
                }
                Levels.Level6.maxvalue -> {
                    Stats.level6.maxvalue = newValue
                    onvaluesetter( Levels.Level6.maxvalue, newValue)
                }
                Levels.Level7.maxvalue -> {
                    Stats.level7.maxvalue = newValue
                    onvaluesetter(Levels.Level7.maxvalue, newValue)
                }
                Levels.Level8.maxvalue -> {
                    Stats.level8.maxvalue = newValue
                    onvaluesetter( Levels.Level8.maxvalue, newValue)
                }
                Levels.Level9.maxvalue -> {
                    Stats.level9.maxvalue = newValue
                    onvaluesetter( Levels.Level9.maxvalue, newValue)
                }
            }

        }
        if (name != null){
            if (!isOverlayVisible) {
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
                            modifier = Modifier.width(actionbuttonWidth)
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
    fun MakeCloudLine(cloudSaver: () -> Unit, cloudLoader: () -> Unit){
        Row{
            Button(
                onClick = {cloudSaver()},
                modifier = Modifier.width(buttonWidth)
            ) {
                Text("Cloud save")
            }
            Spacer(modifier = Modifier.width(15.dp))

            Button( onClick=
                {
                cloudLoader()},
                modifier = Modifier.width(buttonWidth)
            ) {
                Text("Cloud load")
            }
        }
    }

    @Composable
    fun GreetingImage(
        onTakeDamage: (Int) -> Unit,
        onHeal: (Int) -> Unit,
        onSendNotification: (Context, String, String) -> Unit,
        onSetTmpHp: (Int) -> Unit,
        onSetMaxHp: (Int) -> Unit,
        onSetAc: (Int) -> Unit,
        onSetHp: (Int) -> Unit,
        onvaluesetter: (String, Int) -> Unit,
        cloudSaver: () -> Unit,
        cloudLoader: () -> Unit
    ) {
        val image = painterResource(R.drawable.frog)
        var isOverlayVisible by remember { mutableStateOf(false) }
        if (isOverlayVisible)
        {
            ShowStats(
                onClose = { isOverlayVisible = false },
                onTakeDamage,
                onHeal,
                onSetTmpHp,
                onSetMaxHp,
                onSetAc,
                onSetHp,
                onSendNotification,
                onvaluesetter,
                cloudSaver,
                cloudLoader
            )
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

}