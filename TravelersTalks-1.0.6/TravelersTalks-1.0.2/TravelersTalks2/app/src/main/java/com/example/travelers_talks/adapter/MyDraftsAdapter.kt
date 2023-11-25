package com.example.travelers_talks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelers_talks.MyDrafts
import com.example.travelers_talks.R
import com.example.travelers_talks.fragment_mydrafts
import com.example.travelers_talks.fragment_myposts

class MyDraftsAdapter(private val mydraftsList:List<MyDrafts>, private val fragment: fragment_mydrafts) :RecyclerView.Adapter<MyDraftsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDraftsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyDraftsViewHolder(layoutInflater.inflate(R.layout.item_mydrafts, parent, false), parent.context,fragment)
    }

    override fun getItemCount(): Int = mydraftsList.size

    override fun onBindViewHolder(holder: MyDraftsViewHolder, position: Int) {
        val item = mydraftsList[position]
        holder.render(item)
        holder.itemView.setOnClickListener{
            val clickedPosition = holder.adapterPosition
            Toast.makeText(holder.itemView.context, "Clic en posición: $clickedPosition", Toast.LENGTH_SHORT).show()
        }
    }


}