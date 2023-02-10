package com.codepath.articlesearch

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val mediaImage = findViewById<ImageView>(R.id.mediaImage)
        val title = findViewById<TextView>(R.id.mediaTitle)
        val author = findViewById<TextView>(R.id.mediaByline)
        val content = findViewById<TextView>(R.id.mediaAbstract)
        title.text = intent.getStringExtra("title")
        author.text = intent.getStringExtra("author")
        content.text = intent.getStringExtra("content")

        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .placeholder(R.drawable.loading_image)
            .centerInside()
            .into(mediaImage)
    }
}