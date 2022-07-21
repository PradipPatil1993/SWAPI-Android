package com.example.startwars.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.startwars.api.model.Film
import com.example.startwars.databinding.FilmItemBinding

class FilmAdapter (private val filmList: List<Film>) : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            FilmItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(filmList[position])

    override fun getItemCount(): Int = filmList.size

    inner class ViewHolder(private val binding: FilmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            binding.apply {
                tvFilmName.text = film.title
                tvFilmNameSynopsis.text = film.opening_crawl
            }
        }
    }
}