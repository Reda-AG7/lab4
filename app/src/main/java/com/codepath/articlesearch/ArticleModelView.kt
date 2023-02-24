package com.codepath.articlesearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ArticleModelView(application: Application) : AndroidViewModel(application) {
    val allArticles : LiveData<List<ArticleEntity>>
    private var articleRepository : ArticleRepository

    init {
        val articleDao = ArticleDatabase.getDatabase(application).articleDao()
        articleRepository  = ArticleRepository(articleDao)
        allArticles = articleRepository.allArticles
    }

    fun addArticle(article:ArticleEntity){
        viewModelScope.launch(Dispatchers.IO){
            articleRepository.addArticle(article)
        }
    }
    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO){
            articleRepository.deleteAll()
        }
    }
}