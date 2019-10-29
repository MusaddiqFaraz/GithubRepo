package com.example.repo.ui

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.repo.R
import com.example.repo.githubapi.Resource
import com.example.repo.githubapi.TrendingRepo
import com.example.repo.utils.waitUntilVisible
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/** This is a special case which needs to be tested only first time
 *  when there is no data in db and device is not connected to internet,
 *  this should fail if tried in presence of internet or data is already fetched beforehand */
@RunWith(AndroidJUnit4::class)
class MainActivityError {


    @get:Rule
    public val mActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)


    private val trendingRepo = MutableLiveData<Resource<List<TrendingRepo>>>()

    private var mainVM = Mockito.mock(MainVM::class.java)


    @Before
    fun setUp() {

        Mockito.`when`(mainVM.getTrendingRepos(true)).thenReturn(trendingRepo)

    }

    @Test
    fun testErrorState() {

        Thread.sleep(200)
        Espresso.onView(ViewMatchers.withId(R.id.layoutError))

            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.btnRetry)).perform(ViewActions.click())


    }

    @Test
    fun checkLoadingViewAlignment() {
        Espresso.onView(ViewMatchers.withId(R.id.loadingView))
            .check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.toolbar)))
    }

}