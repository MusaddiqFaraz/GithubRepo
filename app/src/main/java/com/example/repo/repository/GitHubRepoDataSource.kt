package com.example.repo.repository

import com.example.repo.OpenForTesting
import com.example.repo.githubapi.DataSource
import com.example.repo.githubapi.GithubApi
import javax.inject.Inject

@OpenForTesting
class GitHubRepoDataSource @Inject constructor(val githubApi: GithubApi) : DataSource() {

    suspend fun getTrendingRepo() = getResult { githubApi.getTrendingRepos() }

}