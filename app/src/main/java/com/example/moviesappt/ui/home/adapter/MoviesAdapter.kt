package com.example.moviesappt.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.moviesappt.databinding.MovieItemBinding
import com.example.moviesappt.domain.model.ResultsItem
import com.example.moviesappt.ui.home.viewmodel.MovieState
import kotlinx.coroutines.flow.StateFlow

class MoviesAdapter(
    private var list: List<ResultsItem>?,
    private val context: Context
) : Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = MovieItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list?.get(position)
        holder.item.movieItemText.text = item?.originalTitle
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500${item?.posterPath}")
            .into(holder.item.movieItemImage)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    fun changeList(list: List<ResultsItem>?) {
        this.list = list
    }


    class ViewHolder(
        var item: MovieItemBinding
    ) : RecyclerView.ViewHolder(item.root)
}