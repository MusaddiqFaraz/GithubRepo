package com.example.repo.ui

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.repo.RVAdapter
import com.example.repo.githubapi.BuiltBy
import com.example.repo.githubapi.Resource
import com.example.repo.githubapi.TrendingRepo
import com.example.repo.repository.TrendingRepoRepository
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.runner.RunWith
import org.mockito.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.repo.R
import com.example.repo.utils.InjectableActivityScenario
import com.example.repo.utils.RecyclerViewMatcher
import com.example.repo.utils.RecyclerViewMatcher.Companion.hasItemCount
import com.example.repo.utils.injectableActivityScenario
import com.example.repo.utils.mock
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner


val builtByOne = BuiltBy("https://avatars2.githubusercontent.com/u/5653902","https://github.com/melody-li","melody-li")
val repoOne = TrendingRepo("algorithm004-0","https://github.com/algorithm004-01.png", listOf(
    builtByOne
),92,"",0,"Java","#b07219","algorithm004-0",0,
    "https://github.com/algorithm004-01/algorithm004-01")

val repoTwo = TrendingRepo("algorithm0012","https://github.com/algorithm004-01.png", listOf(
    builtByOne
),92,"",0,"Java","#b07219","algorithm004-0",0,
    "https://github.com/algorithm004-01/algorithm004-01")


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private var trendingRepoRepository = Mockito.mock(TrendingRepoRepository::class.java)
    private  var mainVM  = MainVM(trendingRepoRepository)


    private  var mockLiveDaa = MutableLiveData<Resource<List<TrendingRepo>>>()



    lateinit var scenario: InjectableActivityScenario<MainActivity>

    @Before
    fun setUp() {

        scenario  = injectableActivityScenario<MainActivity> {
            injectActivity {
                setTestViewModel(mainVM)
                Mockito.`when`(mainVM.getTrendingRepos(false)).thenReturn(mockLiveDaa)
            }
        }.launch()


    }



    /**
     * Check for loading state ,
     * Loading view's visibility should be VISIBLE,
     * Error layout should be GONE
     * RecyclerView's parent swiperefresh layout should be GONE
     * */
    @Test
    fun checkLoadingVisibility() {



        mockLiveDaa.postValue(Resource.loading())

        onView(withId(R.id.loadingView)).check(matches(isDisplayed()))
        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.srlList)).check(matches(not(isDisplayed())))

        onView(withId(R.id.loadingView)).check(
            PositionAssertions.isCompletelyBelow(
                withId(R.id.toolbar)
            )
        )




    }

    /**
     * Check for Error state ,
     * Loading view's visibility should be GONE,
     * Error layout should be VISIBLE
     * RecyclerView's parent swiperefresh layout should be GONE
     * */
    @Test
    fun checkErrorVisibility() {


        mockLiveDaa.postValue(Resource.error("Test Error"))


        onView(withId(R.id.loadingView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))
        onView(withId(R.id.srlList)).check(matches(not(isDisplayed())))


        onView(withId(R.id.layoutError)).check(
            PositionAssertions.isCompletelyBelow(
                withId(R.id.toolbar)
            )
        )


    }

    /**
     * Check for Success state ,
     * 1. Loading view's visibility should be GONE,
     * 2. Error layout should be GONE
     * 3. RecyclerView's parent swiperefresh layout should be VISIBLE
     * 4. check alignment of recyclerview
     * 5. Check for recyclerview's adapter's item count
     * 6. check if first item of list is displayed
     * 7. perform click on first item and check if it is in expanded state
     * */
    @Test
    fun checkSuccessVisibility() {
        val data = listOf(repoOne, repoTwo)

        mockLiveDaa.postValue(Resource.success(data))





        onView(withId(R.id.srlList)).check(matches(isDisplayed()))
        onView(withId(R.id.loadingView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())))


        onView(withId(R.id.srlList)).check(
            PositionAssertions.isCompletelyBelow(
                withId(R.id.toolbar)
            )
        )

        onView(withId(R.id.rvList)).check(matches(hasItemCount(2)))




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

    @After
    fun close() {
        scenario.close()
    }




}

