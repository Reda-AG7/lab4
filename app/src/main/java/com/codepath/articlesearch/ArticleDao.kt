package com.codepath.articlesearch

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleDao {

    @Insert
    fun addArticle(article:ArticleEntity)

    @Query("SELECT * FROM article_table ORDER BY id")
    fun getAllArticles() : LiveData<List<ArticleEntity>>

    @Query("DELETE FROM article_table")
    fun deleteAll()
}