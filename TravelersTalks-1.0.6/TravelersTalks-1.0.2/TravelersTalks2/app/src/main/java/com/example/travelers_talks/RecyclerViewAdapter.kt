package com.example.travelers_talks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    private val itemTitles = arrayOf("Titulo 1","Titulo 2", "Titulo 3")


    inner class ViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView){


        var pfpImage : ImageView
        var nickNameText : TextView
        var dateText : TextView
        var followText : TextView
        var likeImage : ImageView
        var flagImage : ImageView
        var tituloText : TextView
        var postImage : ImageView
        var seemoreText : TextView
        var arrowImage : ImageView

        init {
            pfpImage = itemView.findViewById(R.id.item_img)
            nickNameText = itemView.findViewById(R.id.item_nickname)
            dateText = itemView.findViewById(R.id.item_date)
            followText = itemView.findViewById(R.id.item_follow)
            likeImage = itemView.findViewById(R.id.item_like)
            flagImage = itemView.findViewById(R.id.item_flag)
            tituloText = itemView.findViewById(R.id.item_titulo)
            postImage = itemView.findViewById(R.id.item_img)
            seemoreText = itemView.findViewById(R.id.item_seemore)
            arrowImage = itemView.findViewById(R.id.item_seemorearrow)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_model,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemTitles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tituloText.text = itemTitles[position]
    }
}