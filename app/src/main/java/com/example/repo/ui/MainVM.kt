package com.example.repo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.repo.OpenForTesting
import com.example.repo.githubapi.Resource
import com.example.repo.githubapi.TrendingRepo
import com.example.repo.repository.TrendingRepoRepository
import javax.inject.Inject

@OpenForTesting
class MainVM  @Inject constructor(private val trendingRepoRepository: TrendingRepoRepository) : ViewModel() {

    var trendingRepos: LiveData<Resource<List<TrendingRepo>>>? = null

    fun getTrendingRepos(forceFetch: Boolean): LiveData<Resource<List<TrendingRepo>>>? {
        if (trendingRepos == null || forceFetch) {
            trendingRepos = trendingRepoRepository.getTrendingRepo(forceFetch)
        }

        return  trendingRepos
    }

}