package com.codepath.articlesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticleAdapter(private val articleList : ArrayList<Article>, private val listener : (Article) -> Unit) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentArticle = articleList[position]
        holder.titleTextView.text = currentArticle.articleTitle
        holder.abstractTextView.text = currentArticle.articleContent
        val articleImageURL = "https://www.nytimes.com/${currentArticle.articleImage}"
        Glide.with(holder.itemView.context)
            .load(articleImageURL)
            .placeholder(R.drawable.loading_image)
            .centerInside()
            .into(holder.mediaImageView)
        holder.itemView.setOnClickListener{
            listener(currentArticle)
        }
    }

    override fun getItemCount() = articleList.size

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         val mediaImageView = itemView.findViewById<ImageView>(R.id.mediaImage)
         val titleTextView = itemView.findViewById<TextView>(R.id.mediaTitle)
         val abstractTextView = itemView.findViewById<TextView>(R.id.mediaAbstract)
    }


}