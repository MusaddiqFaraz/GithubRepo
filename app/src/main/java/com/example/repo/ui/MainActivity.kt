package com.example.repo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repo.R
import com.example.repo.RVAdapter
import com.example.repo.extensions.injectViewModel
import com.example.repo.githubapi.Resource
import com.example.repo.githubapi.TrendingRepo
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading_error.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
          val TAG = "MainActivity"
    }

    lateinit var repoAdapter: RVAdapter<TrendingRepo,RepoVH>
    private var trendingRepos = ArrayList<TrendingRepo>()

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var coroutineScope: CoroutineScope

    private lateinit var mainVM: MainVM

    private var selectedItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainVM = injectViewModel(viewModelFactory)


        updateRepoList()
        getTrendingRepo()

        srlList.setOnRefreshListener {
            srlList.isRefreshing = true
            getTrendingRepo(true)
        }

    }

    private fun getTrendingRepo(forceFetch: Boolean = false) {
        mainVM.getTrendingRepos(forceFetch)?.observe(this, Observer {
            result ->
            when(result.status) {
                Resource.Status.SUCCESS -> {
                    Log.e(TAG,"success  ${result.data?.size}  ")


                        result.data?.let {
                            if (it.isNotEmpty()) {
                                showUI(result.status)
                                trendingRepos.clear()
                                trendingRepos.addAll(it)
                                repoAdapter.notifyDataSetChanged()
                            }
                        }

                }
                Resource.Status.ERROR -> {
                    Log.e(TAG,"error  ${result.message}  ")
                    if (trendingRepos.isEmpty()) {
                        showUI(result.status)
                        btnRetry.setOnClickListener {
                            getTrendingRepo(true)
                        }
                    }
                }
                Resource.Status.LOADING ->  {
                    Log.e(TAG,"loading ${result.message}  ")
                    showUI(result.status)
                }
            }
        })
    }


    private fun updateRepoList() {
        repoAdapter = RVAdapter(this,
            R.layout.rv_repo_item,
            {
                RepoVH(it)
            },trendingRepos, {
                holder, repo, position ->
                    holder.bind(repo,selectedItem) {
                        val oldPosition = selectedItem

                        if(selectedItem == position) {
                            selectedItem = -1
                        } else {
                            selectedItem = position
                            repoAdapter.notifyItemChanged(selectedItem)
                        }
                        repoAdapter.notifyItemChanged(oldPosition)



                    }
                }
            )

        rvList.adapter = repoAdapter
        repoAdapter.notifyDataSetChanged()
    }


    private fun showUI(status: Resource.Status) {
        layoutError.visibility = if(status == Resource.Status.ERROR) View.VISIBLE else View.GONE
        srlList.visibility = if(status == Resource.Status.SUCCESS) View.VISIBLE else View.GONE
        loadingView.visibility = if(status == Resource.Status.LOADING) View.VISIBLE else View.GONE
        if(status == Resource.Status.LOADING)
            loadingView.startShimmerAnimation()
        else {
            loadingView.stopShimmerAnimation()
            srlList.isRefreshing = false
        }
    }


}