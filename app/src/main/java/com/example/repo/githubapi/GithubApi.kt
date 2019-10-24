package com.example.repo.githubapi

import androidx.lifecycle.LiveData
import retrofit2.Response
import retrofit2.http.GET

interface GithubApi {

    companion object {
        const val BASE_URL = "https://github-trending-api.now.sh/"
    }


    @GET("repositories")
    suspend fun getTrendingRepos() : Response<List<TrendingRepo>>

}