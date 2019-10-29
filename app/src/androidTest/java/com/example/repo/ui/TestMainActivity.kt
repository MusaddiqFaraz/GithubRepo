package com.example.repo.ui

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.repo.R
import com.example.repo.db.RepoDao
import com.example.repo.githubapi.Resource
import com.example.repo.githubapi.TrendingRepo
import com.example.repo.repository.TrendingRepoRepository
import com.example.repo.utils.*
import com.example.repo.utils.RecyclerViewMatcher.Companion.hasItemCount
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.Mockito.mock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions



/** This should fail when run for the first time with no data,
 *  This should pass when app is loaded with data,
 *  Tested on Redmi note 7 pro */
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



    private val trendingRepo = MutableLiveData<Resource<List<TrendingRepo>>>()

    private var trendingRepoRepository = mock(TrendingRepoRepository::class.java)
    private var mainVM = MainVM(trendingRepoRepository)



    @Before
    fun setUp() {

    }

    @Test
    fun testLoadingResult() {
            val result = MutableLiveData<Resource<List<TrendingRepo>>>(Resource.loading())
            `when`(trendingRepoRepository.getTrendingRepo(false)).thenReturn(result)


            trendingRepo.postValue(Resource.loading())
            mActivityTestRule.activity.runOnUiThread {
                assertEquals(Resource.loading<TrendingRepo>(), getValue(mainVM.getTrendingRepos(false)!!))
            }

            onView(withId(R.id.loadingView)).check(matches(isDisplayed()))

    }


    @Test
    fun testSuccessResult() {

        val data = listOf(repoOne, repoTwo)
        val result = MutableLiveData<Resource<List<TrendingRepo>>>(Resource.success(data))
        `when`(trendingRepoRepository.getTrendingRepo(false)).thenReturn(result)

        mActivityTestRule.activity.runOnUiThread {
            assertEquals(Resource.success(data), getValue(mainVM.getTrendingRepos(false)!!))
        }

        /*wait for ui to get inflated*/
        Thread.sleep(200)
        onView(withId(R.id.srlList)).check(matches(isDisplayed()))
    }



    @Test
    fun testErrorResult() {

        val result = MutableLiveData<Resource<List<TrendingRepo>>>(Resource.error("Error occurred"))
        `when`(trendingRepoRepository.getTrendingRepo(false)).thenReturn(result)

        mActivityTestRule.activity.runOnUiThread {
            assertEquals(Resource.error<TrendingRepo>("Error occurred"), getValue(mainVM.getTrendingRepos(false)!!))
        }


    }



    @Test
    fun checkRecyclerViewAlignment() {
        onView(withId(R.id.rvList)).check(isCompletelyBelow(withId(R.id.toolbar)))
    }




    @Test
    fun checkRecyclerVH() {

        if( getRVcount() <= 0) {
            /*wait for ui to get inflated*/
            Thread.sleep(200)
        }

        onView(RecyclerViewMatcher(R.id.rvList)
            .atPositionOnView(0, R.id.rlRepo))
            .check(matches(isDisplayed()))

        /** Perform click on first item */
        onView(RecyclerViewMatcher(R.id.rvList)
            .atPositionOnView(0, R.id.rlRepo)).perform(ViewActions.click())

        /** Check if item is in expanded state*/
        RecyclerViewMatcher(R.id.rvList)
            .atPositionOnView(0, R.id.rlDetails).matches(isDisplayed())

    }

    /** The api provides 25 items in response, this test checks recyclerview count for that */
    @Test
    fun checkRecyclerViewCount() {

        if( getRVcount() <= 0) {
            /*wait for ui to get inflated*/
            Thread.sleep(200)
        }

        onView(withId(R.id.rvList)).check(matches(hasItemCount(25)))
    }

    private fun getRVcount(): Int {
        val recyclerView =
            mActivityTestRule.activity.findViewById(R.id.rvList) as RecyclerView
        return recyclerView.adapter!!.itemCount
    }




}