package com.example.travelers_talks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelers_talks.Posts
import com.example.travelers_talks.R

class PostsAdapter(private val postList:List<Posts> ) : RecyclerView.Adapter<PostsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PostsViewHolder(layoutInflater.inflate(R.layout.item_posts, parent, false), parent.context)
    }

    override fun getItemCount(): Int = postList.size


    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val item = postList[position]
        holder.render(item)
        holder.itemView.setOnClickListener{
            val clickedPosition = holder.adapterPosition
            Toast.makeText(holder.itemView.context, "Clic en posición: $clickedPosition", Toast.LENGTH_SHORT).show()
        }
    }


}