package com.example.myapplication

fun getValueByUser(user: User, name: String): Int = when (name) {
    "HP"         -> user.HP
    "Tmp.HP"     -> user.TmpHP
    "AC"         -> user.AC
    "Max.HP"     -> user.MaxHP
    "Level1"     -> user.Level1
    "Level2"     -> user.Level2
    "Level3"     -> user.Level3
    "Level4"     -> user.Level4
    "Level5"     -> user.Level5
    "Level6"     -> user.Level6
    "Level7"     -> user.Level7
    "Level8"     -> user.Level8
    "Level9"     -> user.Level9
    "MaxLevel1"  -> user.MaxLevel1
    "MaxLevel2"  -> user.MaxLevel2
    "MaxLevel3"  -> user.MaxLevel3
    "MaxLevel4"  -> user.MaxLevel4
    "MaxLevel5"  -> user.MaxLevel5
    "MaxLevel6"  -> user.MaxLevel6
    "MaxLevel7"  -> user.MaxLevel7
    "MaxLevel8"  -> user.MaxLevel8
    "MaxLevel9"  -> user.MaxLevel9
    else         -> 0
}

suspend fun setValueByUser(user: User, userDao: UserDao, name: String, value: Int) {
    when (name) {
        "HP"         -> user.HP         = value
        "Tmp.HP"     -> user.TmpHP      = value
        "AC"         -> user.AC         = value
        "Max.HP"     -> user.MaxHP      = value
        "Level1"     -> user.Level1     = value
        "Level2"     -> user.Level2     = value
        "Level3"     -> user.Level3     = value
        "Level4"     -> user.Level4     = value
        "Level5"     -> user.Level5     = value
        "Level6"     -> user.Level6     = value
        "Level7"     -> user.Level7     = value
        "Level8"     -> user.Level8     = value
        "Level9"     -> user.Level9     = value
        "MaxLevel1"  -> user.MaxLevel1  = value
        "MaxLevel2"  -> user.MaxLevel2  = value
        "MaxLevel3"  -> user.MaxLevel3  = value
        "MaxLevel4"  -> user.MaxLevel4  = value
        "MaxLevel5"  -> user.MaxLevel5  = value
        "MaxLevel6"  -> user.MaxLevel6  = value
        "MaxLevel7"  -> user.MaxLevel7  = value
        "MaxLevel8"  -> user.MaxLevel8  = value
        "MaxLevel9"  -> user.MaxLevel9  = value
    }
    Stats.setByName(name, value)
    userDao.updateUsers(user)
}
