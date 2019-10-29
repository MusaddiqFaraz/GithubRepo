package com.example.repo.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.repo.db.GithubDBTest
import com.example.repo.db.RepoDao
import com.example.repo.utils.getValue
import com.example.repo.utils.repoOne
import com.example.repo.utils.repoTwo
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoDaoTest : GithubDBTest() {

    private lateinit var repoDao: RepoDao
    private val setA = repoOne.copy()
    private val setB = repoTwo.copy()


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        repoDao = db.getRepoDao()


        runBlocking {
            repoDao.insertAll(listOf(setA, setB))
        }
    }


    /** Test fetching of data from db using repo dao */
    @Test
    fun testGetRepos() {
        val list = getValue(repoDao.getTrendingRepo())
        Assert.assertThat(list.size, equalTo(2))


        Assert.assertThat(list[0], equalTo(setA))
        Assert.assertThat(list[1], equalTo(setB))
    }


}