package com.codepath.articlesearch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_table")
data class ArticleEntity(
        @PrimaryKey(autoGenerate = true)
        val id : Int,
        val articleImage:String,
        val articleTitle:String,
        val articleContent:String,
        val articleAuthor:String
)