package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import com.example.newsapp.utility.Constants.Companion.ANY_QUERY
import com.example.newsapp.utility.Constants.Companion.API_KEY
import com.example.newsapp.utility.Constants.Companion.COUNTRY_CODE
import com.example.newsapp.utility.Constants.Companion.LATEST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiInterface {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String = COUNTRY_CODE,
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<NewsResponse>

    @GET("v2/everything")
    fun getAllNews(
        @Query("q") query: String = ANY_QUERY,
        @Query("sortBy") sortBy: String = LATEST,
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<NewsResponse>
}