package com.example.fcnewsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fcnewsapp.databinding.ActivityMainBinding
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://news.google.com/")
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
            )
        )
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        newsAdapter = NewsAdapter()
        binding.newsRecyclerView.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter

        }

        val newsService =  retrofit.create(NewsService::class.java)
        newsService.mainFeed().enqueue(object: Callback<NewsRss> {
            override fun onResponse(p0: Call<NewsRss>, p1: Response<NewsRss>) {
                Log.e("MainActivity", "${p1.body()?.channel?.items}")
                newsAdapter.submitList(p1.body()?.channel?.items.orEmpty())
            }

            override fun onFailure(p0: Call<NewsRss>, p1: Throwable) {
                p1.printStackTrace()
            }
        })

    }
}