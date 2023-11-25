package com.example.travelers_talks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelers_talks.Posts
import com.example.travelers_talks.PostsForeign
import com.example.travelers_talks.R

class UserForeignAdapter(private val foreignPostsList:List<PostsForeign>, private val miValor: Int ) : RecyclerView.Adapter<UserForeignViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserForeignViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserForeignViewHolder(layoutInflater.inflate(R.layout.item_foreignposts, parent, false), parent.context, miValor)
    }

    override fun getItemCount(): Int = foreignPostsList.size

    override fun onBindViewHolder(holder: UserForeignViewHolder, position: Int) {
        val item = foreignPostsList[position]
        holder.render(item)
        holder.itemView.setOnClickListener{
            val clickedPosition = holder.adapterPosition
            Toast.makeText(holder.itemView.context, "Clic en posición: $clickedPosition", Toast.LENGTH_SHORT).show()
        }
    }


}