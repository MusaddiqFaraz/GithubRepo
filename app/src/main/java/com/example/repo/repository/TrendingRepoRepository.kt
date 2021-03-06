package com.example.repo.repository

import androidx.lifecycle.distinctUntilChanged
import com.example.repo.OpenForTesting
import com.example.repo.db.RepoDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class TrendingRepoRepository @Inject constructor(private val repoDao: RepoDao,
                                                 private val gitHubRepoDataSource: GitHubRepoDataSource) {


    /** Time to leave is set to 2 hours  as per requirement*/
    private val ttl = 2 * 60 * 60 * 1000

    fun getTrendingRepo(forceFetch: Boolean = false) = resultLiveData(
        databaseQuery = { repoDao.getTrendingRepo() },
        networkCall = { gitHubRepoDataSource.getTrendingRepo() },
        saveCallResult = {
            repoDao.deletePreviousData()
            repoDao.insertAll(it)
        },
        shouldFetch = {
            /**
             * Conditional check, this will return true
             * 1. if first time db is empty
             * 2. when db data is 2 hours old
             * 3. when user performs swipe to refresh
            * */
            it.isNullOrEmpty()  || (System.currentTimeMillis() -  it[0].timeStamp) > ttl || forceFetch
        }
    ).distinctUntilChanged()

}