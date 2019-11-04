package com.example.repo.api

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.repo.R
import com.example.repo.RVAdapter
import com.example.repo.githubapi.Resource
import com.example.repo.githubapi.TrendingRepo
import com.example.repo.repository.TrendingRepoRepository
import com.example.repo.ui.MainActivity
import com.example.repo.ui.MainVM
import com.example.repo.ui.RepoVH
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController



@RunWith(RobolectricTestRunner::class)
class TestMainActivity {


    private var trendingRepoRepository = Mockito.mock(TrendingRepoRepository::class.java)
    private var mainVM = MainVM(trendingRepoRepository)

    @Mock
    private lateinit var mockLiveDaa: LiveData<Resource<List<TrendingRepo>>>

    @Captor
    private lateinit var repoObserverCaptor: ArgumentCaptor<Observer<Resource<List<TrendingRepo>>>>

    private lateinit var activityController: ActivityController<MainActivity>

    private lateinit var activity: MainActivity

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()


    @Before
    fun setUp() {
        Mockito.`when`(mainVM.getTrendingRepos(false)).thenReturn(mockLiveDaa)

        /** Build activity controller to drive between states of activity*/
        activityController = Robolectric.buildActivity(MainActivity::class.java)

        activity = activityController.get()

        /**
         * Drive our activity to create state
        * */
        activityController.create()

        /**
         * Set the mock view model as the source of our data
         * */
        activity.setTestViewModel(mainVM)

        /**
         * Drive our activity to resume state
         * */
        activityController.resume()



        Mockito.verify(mockLiveDaa)
            .observe(ArgumentMatchers.any(LifecycleOwner::class.java),repoObserverCaptor.capture())
    }

    /**
     * Check for loading state ,
     * Loading view's visibility should be VISIBLE,
     * Error layout should be GONE
     * RecyclerView's parent swiperefresh layout should be GONE
     * */
    @Test
    fun checkLoadingVisibility() {

        repoObserverCaptor.value.onChanged(Resource.loading())


        assertEquals(View.VISIBLE,activity.loadingView.visibility)
        assertEquals(View.GONE,activity.srlList.visibility)
        assertEquals(View.GONE,activity.layoutError.visibility)


    }

    /**
     * Check for Error state ,
     * Loading view's visibility should be GONE,
     * Error layout should be VISIBLE
     * RecyclerView's parent swiperefresh layout should be GONE
     * */
    @Test
    fun checkErrorVisibility() {
        repoObserverCaptor.value.onChanged(Resource.error("Test Error"))

        assertEquals(View.GONE,activity.loadingView.visibility)
        assertEquals(View.GONE,activity.srlList.visibility)
        assertEquals(View.VISIBLE,activity.layoutError.visibility)
    }

    /**
     * Check for Success state ,
     * Loading view's visibility should be GONE,
     * Error layout should be GONE
     * RecyclerView's parent swiperefresh layout should be VISIBLE
     * Check for recyclerview's adapter's item count
     * check if the data we have passed is same as inflated data
     * */
    @Test
    fun checkSuccessVisibility() {
        val data = listOf(repoOne, repoTwo)

        repoObserverCaptor.value.onChanged(Resource.success(data))

        assertEquals(View.VISIBLE,activity.srlList.visibility)
        assertEquals(View.GONE,activity.loadingView.visibility)
        assertEquals(View.GONE,activity.layoutError.visibility)

        assertEquals(2, (activity.rvList.adapter as? RVAdapter<TrendingRepo,RepoVH>)?.items?.size)


        assertEquals(data, (activity.rvList.adapter as? RVAdapter<TrendingRepo,RepoVH>)?.items)

    }

}