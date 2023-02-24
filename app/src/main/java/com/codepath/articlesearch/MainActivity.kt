package com.codepath.articlesearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
        val articleMV = ViewModelProvider(this).get(ArticleModelView::class.java)
        articlesRecyclerView = findViewById(R.id.articles)
        val articleAdapter = ArticleAdapter{
            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra("title",it.articleTitle)
            intent.putExtra("content",it.articleContent)
            intent.putExtra("image","https://www.nytimes.com/${it.articleImage}")
            intent.putExtra("author",it.articleAuthor)
            startActivity(intent)
        }

        articlesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }
        val articleList = ArrayList<ArticleEntity>()
        val articleEntityList = ArrayList<ArticleEntity>()
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

                    //update the db and the UI
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
                       articleList.add(ArticleEntity(0,articleImage,articleTitle,articleContent,articleAuthor))
                    }
                    if(articleJsonArray.length() > 0){
                        articleMV.deleteAll()
                        for(i in articleList){
                            articleMV.addArticle(i)
                        }
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })

        articleMV.allArticles.observe(this){ articles->
            articleAdapter.setData(articles)
        }
        articlesRecyclerView.adapter = articleAdapter
    }
}