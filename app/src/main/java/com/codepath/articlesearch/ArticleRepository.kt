package com.codepath.articlesearch

import androidx.lifecycle.LiveData

class ArticleRepository(private val articleDao : ArticleDao) {
    val allArticles : LiveData<List<ArticleEntity>> = articleDao.getAllArticles()

    fun addArticle(article: ArticleEntity){
        articleDao.addArticle(article)
    }
    fun deleteAll(){
        articleDao.deleteAll()
    }
}