package com.example.repo.utils

import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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



/**
 * Wait for view to be visible
 */
fun ViewInteraction.waitUntilVisible(timeout: Long): ViewInteraction {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeout

    do {
        try {
            check(matches(isDisplayed()))
            return this
        } catch (e: NoMatchingViewException) {
            Thread.sleep(50)
        }
    } while (System.currentTimeMillis() < endTime)

    throw TimeoutException()
}
