package com.example.repo.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.repo.githubapi.GithubApi
import com.example.repo.githubapi.TrendingRepo
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class GithubApiTest  {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: GithubApi

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }


    @Test
    fun getTrendingRepos() {
        runBlocking {
            enqueueResponse("trending-repo-response.json")
            val resultResponse = service.getTrendingRepos().body()

            /*check request type and it's end point*/
            val request = mockWebServer.takeRequest()
            Assert.assertThat(request.path,`is`("/repositories"))

            /* assert response */
            Assert.assertNotNull(resultResponse)
            Assert.assertThat(resultResponse!!.size,`is`(4))

            val trendingRepo = resultResponse[0]

            Assert.assertThat(trendingRepo.author,`is`("xingshaocheng"))
            Assert.assertThat(trendingRepo.name,`is`("architect-awesome"))
            Assert.assertThat(trendingRepo.url,`is`("https://github.com/xingshaocheng/architect-awesome"))
            Assert.assertThat(trendingRepo.avatar,`is`("https://github.com/xingshaocheng.png"))
            Assert.assertThat(trendingRepo.stars,`is`(7333))
            Assert.assertThat(trendingRepo.forks,`is`(1546))
            Assert.assertThat(trendingRepo.currentPeriodStars,`is`(1528))
            Assert.assertThat(trendingRepo.builtBy!!.size,`is`(1))

        }
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            .getResourceAsStream("response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(mockResponse.setBody(
            source.readString(Charsets.UTF_8))
        )
    }
}