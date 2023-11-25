package com.example.travelers_talks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelers_talks.MyPosts
import com.example.travelers_talks.R
import com.example.travelers_talks.fragment_myposts

class MyPostsAdapter(private val mypostsList:List<MyPosts>, private val fragment:fragment_myposts) : RecyclerView.Adapter<MyPostsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyPostsViewHolder(layoutInflater.inflate(R.layout.item_myposts, parent, false), parent.context,fragment)
    }

    override fun getItemCount(): Int = mypostsList.size

    override fun onBindViewHolder(holder: MyPostsViewHolder, position: Int) {
        val item = mypostsList[position]
        holder.render(item)
        holder.itemView.setOnClickListener{
            val clickedPosition = holder.adapterPosition
            Toast.makeText(holder.itemView.context, "Clic en posición: $clickedPosition", Toast.LENGTH_SHORT).show()
        }
    }

}