package com.example.travelers_talks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelers_talks.MyFavorites
import com.example.travelers_talks.R
import com.example.travelers_talks.fragment_myfavorites

class MyFavoritesAdapter(private val myfavoritesList:List<MyFavorites>, private val fragment:fragment_myfavorites) : RecyclerView.Adapter<MyFavoritesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyFavoritesViewHolder(layoutInflater.inflate(R.layout.item_myfavorites, parent, false), parent.context, fragment)
    }

    override fun getItemCount(): Int = myfavoritesList.size

    override fun onBindViewHolder(holder: MyFavoritesViewHolder, position: Int) {
        val item = myfavoritesList[position]
        holder.render(item)
        holder.itemView.setOnClickListener{
            val clickedPosition = holder.adapterPosition
            Toast.makeText(holder.itemView.context, "Clic en posición: $clickedPosition", Toast.LENGTH_SHORT).show()
        }
    }
}