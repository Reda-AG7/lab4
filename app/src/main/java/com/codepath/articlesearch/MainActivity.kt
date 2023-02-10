package com.codepath.articlesearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException


private const val TAG = "MainActivity/"
private const val SEARCH_API_KEY = BuildConfig.API_KEY
private const val ARTICLE_SEARCH_URL =
    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"

class MainActivity : AppCompatActivity() {
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        articlesRecyclerView = findViewById(R.id.articles)
        val articleList = ArrayList<Article>()


        articlesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        val client = AsyncHttpClient()
        client.get(ARTICLE_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {
                    val articleJSON= json.jsonObject.getJSONObject("response")
                    val articleJsonArray = articleJSON.getJSONArray("docs")
                    Log.d("articleJSONARRAY",articleJsonArray.length().toString())
                    for(i in 0 until articleJsonArray.length()){
                        val articleImage = articleJsonArray.getJSONObject(i)
                                        .getJSONArray("multimedia").getJSONObject(0)
                                        .getString("url")
                        val articleTitle = articleJsonArray.getJSONObject(i)
                                            .getJSONObject("headline").getString("main")
                        val articleContent = articleJsonArray.getJSONObject(i)
                            .getString("abstract")
                        val articleAuthor = articleJsonArray.getJSONObject(i)
                                            .getJSONObject("byline").getString("original")
                        articleList.add(Article(articleImage,articleTitle,articleContent,articleAuthor))
                    }
                    val articleAdapter = ArticleAdapter(articleList){
                        val intent = Intent(this@MainActivity,DetailActivity::class.java)
                        intent.putExtra("title",it.articleTitle)
                        intent.putExtra("content",it.articleContent)
                        intent.putExtra("image","https://www.nytimes.com/${it.articleImage}")
                        intent.putExtra("author",it.articleAuthor)
                        startActivity(intent)
                    }
                    articlesRecyclerView.adapter = articleAdapter
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })

    }
}