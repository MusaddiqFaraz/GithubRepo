package com.example.repo.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.repo.R
import com.example.repo.utils.RecyclerViewMatcher
import com.example.repo.utils.RecyclerViewMatcher.Companion.hasItemCount
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test



@RunWith(AndroidJUnit4::class)
class TestMainActivity {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.repo", appContext.packageName)
    }

    @get:Rule
    public val  mActivityTestRule =  ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
    }






    @Test
    fun checkRecyclerViewAlignment() {
        onView(withId(R.id.rvList)).check(isCompletelyBelow(withId(R.id.toolbar)))
    }




}