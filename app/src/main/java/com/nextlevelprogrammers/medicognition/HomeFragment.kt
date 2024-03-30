package com.nextlevelprogrammers.medicognition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import retrofit2.Call
import retrofit2.Callback


class HomeFragment : Fragment() {

    private lateinit var articlesGridLayout: GridLayout
    private var articlesList: List<Article>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        articlesGridLayout = view.findViewById(R.id.articlesGridLayout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (articlesList == null) {
            fetchArticles()
        } else {
            displayArticles(articlesList!!)
        }
    }

    private fun fetchArticles() {
        val apiService = ApiService.create()
        apiService.getTopHealthHeadlines("in", "health", "YOUR_API_KEY").enqueue(object :
            Callback<NewsResponse> {
            override fun onResponse(call: retrofit2.Call<NewsResponse>, response: retrofit2.Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    articles?.let {
                        articlesList = it
                        displayArticles(it)
                    }
                } else {
                    // Handle error response
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle network failure
            }
        })
    }

    private fun displayArticles(articles: List<Article>) {
        for (article in articles) {
            val articleView = LayoutInflater.from(context).inflate(R.layout.item_article, null)
            val articleTitleView: TextView = articleView.findViewById(R.id.articleTitleTextView)
            val articleDescriptionView: TextView = articleView.findViewById(R.id.articleDescriptionTextView)

            articleTitleView.text = article.title
            articleDescriptionView.text = article.description

            articlesGridLayout.addView(articleView)
        }
    }
}