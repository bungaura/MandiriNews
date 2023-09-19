package com.example.newsapp.model

class NewsResponse (
    var status: String?,
    var totalResults: Int,
    var articles: List<Article>?
)