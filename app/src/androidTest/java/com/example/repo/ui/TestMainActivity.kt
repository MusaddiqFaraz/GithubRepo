package com.example.repo.ui

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.repo.R
import com.example.repo.db.RepoDao
import com.example.repo.githubapi.Resource
import com.example.repo.githubapi.TrendingRepo
import com.example.repo.repo.GitHubRepoDataSource
import com.example.repo.repo.TrendingRepoRepository
import com.example.repo.utils.*
import com.example.repo.utils.RecyclerViewMatcher.Companion.hasItemCount
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Mockito.mock


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

    private val repoDao = mock<RepoDao>()
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


    }

    @Test
    fun testErrorResult() {

        val result = MutableLiveData<Resource<List<TrendingRepo>>>(Resource.error("Error occured"))
        `when`(trendingRepoRepository.getTrendingRepo(false)).thenReturn(result)

        mActivityTestRule.activity.runOnUiThread {
            assertEquals(Resource.error<TrendingRepo>("Error occured"), getValue(mainVM.getTrendingRepos(false)!!))
        }


    }





    @Test
    fun checkRecyclerViewAlignment() {
        onView(withId(R.id.rvList)).check(isCompletelyBelow(withId(R.id.toolbar)))
    }




}