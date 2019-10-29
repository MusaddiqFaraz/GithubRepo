package com.example.repo.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.repo.db.GithubAppDB
import com.example.repo.db.RepoDao
import com.example.repo.githubapi.GithubApi
import com.example.repo.repository.TrendingRepoRepository
import com.example.repo.ui.MainVM
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class MainVMTest  {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private  var trendingRepoRepository = mock(TrendingRepoRepository::class.java)

    private val repoDao = mock(RepoDao::class.java)
    private val githubApi = mock(GithubApi::class.java)



    private lateinit var viewModel: MainVM

    @Before
    fun init() {
        val db = mock(GithubAppDB::class.java)
        `when`(db.getRepoDao()).thenReturn(repoDao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()


        viewModel = MainVM(trendingRepoRepository)
    }

    @Test
    fun getTrendingRepo() {

        val forceFetch = true
        viewModel.getTrendingRepos(forceFetch)
        verify(trendingRepoRepository, atLeastOnce()).getTrendingRepo(true)
        verifyZeroInteractions(trendingRepoRepository)

    }
}