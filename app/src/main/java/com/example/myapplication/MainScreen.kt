package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.em

@Composable
fun MainScreen(viewModel: GameViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GreetingImage(
                    onTakeDamage       = viewModel::takeDamage,
                    onSendNotification = viewModel::sendNotification,
                    onHeal             = { viewModel.addHP(Names.HP.value, value  = it) },
                    onSetTmpHp         = { viewModel.setStats(Names.TmpHP.value, it) },
                    onSetMaxHp         = { viewModel.setStats(Names.MaxHP.value, it) },
                    onSetAc            = { viewModel.setStats(Names.AC.value, it) },
                    onSetHp            = { viewModel.setStats(Names.HP.value, it) },
                    onvaluesetter      = viewModel::valuesetter,
                    cloudSaver         = { viewModel.saveAllToCloud() },
                    cloudLoader        = { viewModel.loadAllFromCloud() }
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MakeHealthLine(
                    name1              = Names.HP.value,
                    name2              = Names.TmpHP.value,
                    onTakeDamage       = viewModel::takeDamage,
                    onHeal             = { viewModel.addHP(Names.HP.value, it) },
                    onSetTmpHp         = { viewModel.setStats(Names.TmpHP.value, it) },
                    onSetMaxHp         = { viewModel.setStats(Names.MaxHP.value, it) },
                    onSetAc            = { viewModel.setStats(Names.AC.value, it) },
                    onSetHp            = { viewModel.setStats(Names.HP.value, it) },
                    onSendNotification = viewModel::sendNotification,
                    onvaluesetter      = viewModel::valuesetter
                )

                Spacer(modifier = Modifier.height(blocksSeparator))

                MakeParametersChangersLine(
                    name1              = Names.Heal.value,
                    name2              = Names.TmpHP.value,
                    onTakeDamage       = viewModel::takeDamage,
                    onHeal             = { viewModel.addHP(Names.HP.value, it) },
                    onSetTmpHp         = { viewModel.setStats(Names.TmpHP.value, it) },
                    onSetMaxHp         = { viewModel.setStats(Names.MaxHP.value, it) },
                    onSetAc            = { viewModel.setStats(Names.AC.value, it) },
                    onSetHp            = { viewModel.setStats(Names.HP.value, it) },
                    buttonWidth        = buttonWidth,
                    onSendNotification = viewModel::sendNotification,
                )

                Spacer(modifier = Modifier.height(blocksSeparator))

                MakeParametersChangersLine(
                    name1              = Names.TakeDamage.value,
                    name2              = Names.AC.value,
                    onTakeDamage       = viewModel::takeDamage,
                    onHeal             = { viewModel.addHP(Names.HP.value, it) },
                    onSetTmpHp         = { viewModel.setStats(Names.TmpHP.value, it) },
                    onSetMaxHp         = { viewModel.setStats(Names.MaxHP.value, it) },
                    onSetAc            = { viewModel.setStats(Names.AC.value, it) },
                    onSetHp            = { viewModel.setStats(Names.HP.value, it) },
                    buttonWidth        = buttonWidth,
                    onSendNotification = viewModel::sendNotification
                )

                Spacer(modifier = Modifier.height(blocksSeparator))
                Text("Spells", fontSize = 6.em)
                Spacer(modifier = Modifier.height(blocksSeparator))

                MakeSpellsLine(name1 = Levels.Level1.exp, name2 = Levels.Level2.exp, onvaluesetter = viewModel::valuesetter, getSpellsbyLevel = viewModel::getSpellsbyLevel, spellAdder = viewModel::spellAdder)
                MakeSpellsLine(name1 = Levels.Level3.exp, name2 = Levels.Level4.exp, onvaluesetter = viewModel::valuesetter, getSpellsbyLevel = viewModel::getSpellsbyLevel, spellAdder = viewModel::spellAdder)
                MakeSpellsLine(name1 = Levels.Level5.exp, name2 = Levels.Level6.exp, onvaluesetter = viewModel::valuesetter, getSpellsbyLevel = viewModel::getSpellsbyLevel, spellAdder = viewModel::spellAdder)
                MakeSpellsLine(name1 = Levels.Level7.exp, name2 = Levels.Level8.exp, onvaluesetter = viewModel::valuesetter, getSpellsbyLevel = viewModel::getSpellsbyLevel, spellAdder = viewModel::spellAdder)
                MakeSpellsLine(name1 = Levels.Level9.exp, name2 = null,              onvaluesetter = viewModel::valuesetter, getSpellsbyLevel = viewModel::getSpellsbyLevel, spellAdder = viewModel::spellAdder)
            }
        }
    }
}
