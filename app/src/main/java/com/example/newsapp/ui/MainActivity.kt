package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.NewsApiClient
import com.example.newsapp.api.NewsApiInterface
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var headlineNewsAdapter: NewsAdapter
    private lateinit var allNewsAdapter: NewsAdapter
    val apiInterface: NewsApiInterface = NewsApiClient.api!!

    private var topHeadlines: MutableList<Article> = mutableListOf()
    private var allNews: MutableList<Article> = mutableListOf()

    private var topHeadlinesPage = 1
    private var allNewsPage = 1

    private val rvItemToLoadPerPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViewScrollListenersHeadlineNews()
        setupRecyclerViewScrollListenersAllNews()
    }

    private fun setupRecyclerViewScrollListenersHeadlineNews() {
        setupHeadlineNewsRecyclerView()
        binding.rvHeadlineNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == topHeadlines.size - 1) {
                    if (topHeadlines.size >= rvItemToLoadPerPage * topHeadlinesPage) {
                        fetchTopHeadlineNews()
                    } else {
                        recyclerView.scrollToPosition(0)
                        topHeadlinesPage = 1
                    }
                }
            }
        })
    }

    private fun setupRecyclerViewScrollListenersAllNews(){
        setupAllNewsRecyclerView()
        binding.rvAllNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == allNews.size - 1) {
                    if (allNews.size >= rvItemToLoadPerPage * allNewsPage) {
                        fetchAllNews()
                    } else {
                        recyclerView.scrollToPosition(0)
                        allNewsPage = 1
                    }
                }
            }
        })
    }

    private fun setupHeadlineNewsRecyclerView() {
        headlineNewsAdapter = NewsAdapter(this, ArrayList(topHeadlines), R.layout.headline_news)
        binding.rvHeadlineNews.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvHeadlineNews.adapter = headlineNewsAdapter
        fetchTopHeadlineNews()
    }

    private fun setupAllNewsRecyclerView() {
        allNewsAdapter = NewsAdapter(this, ArrayList(allNews), R.layout.item_news)
        binding.rvAllNews.layoutManager = LinearLayoutManager(this)
        binding.rvAllNews.adapter = allNewsAdapter
        fetchAllNews()
    }

    private fun fetchTopHeadlineNews(){
        val topHeadlinesCall: Call<NewsResponse> = apiInterface.getTopHeadlines()
        topHeadlinesCall.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("HIT_NEWS", "Failed to fetch top headlines news: ${t.message}")
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse: NewsResponse? = response.body()
                    topHeadlines = (newsResponse?.articles?.toList() ?: emptyList()) as MutableList<Article>
                    headlineNewsAdapter.setData(topHeadlines)
                } else {
                    Log.d("HIT_NEWS", "API call for top headlines failed with code: ${response.code()}")
                }
            }
        })
    }

    private fun fetchAllNews(){
        val allNewsCall: Call<NewsResponse> = apiInterface.getAllNews()
        allNewsCall.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("HIT_NEWS", "Failed to fetch all news: ${t.message}")
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse: NewsResponse? = response.body()
                    allNews = (newsResponse?.articles?.toList() ?: emptyList()) as MutableList<Article>
                    allNewsAdapter.setData(allNews)
                } else {
                    Log.d("HIT_NEWS", "API call for all news failed with code: ${response.code()}")
                }
            }
        })
    }
}

