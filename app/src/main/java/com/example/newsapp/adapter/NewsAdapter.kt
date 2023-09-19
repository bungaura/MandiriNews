package com.example.newsapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.ui.WebViewActivity
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val context: Context,
    private val news: ArrayList<Article>,
    private val itemLayoutResourceId: Int // The XML layout resource ID for each item
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(itemLayoutResourceId, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = news[position]
        holder.bind(article)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", article.url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = news.size

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvAuthor: TextView = itemView.findViewById(R.id.tv_author)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val ivImage: ImageView = itemView.findViewById(R.id.iv_image)

        fun bind(article: Article) {
            tvTitle.text = article.title
            tvAuthor.text = article.author ?: "Anonymous"
            tvDate.text = formatDate(article.publishedAt)
            Picasso.get()
                .load(article.urlToImage)
                .error(R.drawable.ic_no_picture)
                .into(ivImage)

        }

        private fun formatDate(dateString: String?): String {
            dateString?.let {
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                    val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
                    val date = inputFormat.parse(dateString)
                    return outputFormat.format(date)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return ""
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<Article>) {
        news.clear()
        news.addAll(newData)
        notifyDataSetChanged()
    }
}
