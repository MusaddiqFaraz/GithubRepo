package com.example.repo.utils

import com.example.repo.githubapi.BuiltBy
import com.example.repo.githubapi.TrendingRepo
import java.util.concurrent.TimeoutException

val builtByOne = BuiltBy("https://avatars2.githubusercontent.com/u/5653902","https://github.com/melody-li","melody-li")


val repoOne = TrendingRepo("algorithm004-0","https://github.com/algorithm004-01.png", listOf(
    builtByOne),92,"",0,"Java","#b07219","algorithm004-0",0,
    "https://github.com/algorithm004-01/algorithm004-01")

val repoTwo = TrendingRepo("algorithm0012","https://github.com/algorithm004-01.png", listOf(
    builtByOne),92,"",0,"Java","#b07219","algorithm004-0",0,
    "https://github.com/algorithm004-01/algorithm004-01")




