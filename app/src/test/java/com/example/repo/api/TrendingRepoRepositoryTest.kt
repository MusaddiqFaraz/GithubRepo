package com.example.repo.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.repo.db.GithubAppDB
import com.example.repo.db.RepoDao
import com.example.repo.githubapi.GithubApi
import com.example.repo.repository.GitHubRepoDataSource
import com.example.repo.repository.TrendingRepoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class TrendingRepoRepositoryTest {
    private lateinit var trendingRepoRepository: TrendingRepoRepository

    private val repoDao = mock(RepoDao::class.java)
    private val githubApi = mock(GithubApi::class.java)

    private val gitHubRepoDataSource = GitHubRepoDataSource(githubApi)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @Before
    fun init() {
        val db = mock(GithubAppDB::class.java)
        `when`(db.getRepoDao()).thenReturn(repoDao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        trendingRepoRepository = TrendingRepoRepository(repoDao, gitHubRepoDataSource)

    }


    @Test
    fun getTrendingRepo() {
        runBlocking {
            trendingRepoRepository.getTrendingRepo()

            verify(repoDao, never()).getTrendingRepo()
            verifyZeroInteractions(repoDao)
        }
    }


}