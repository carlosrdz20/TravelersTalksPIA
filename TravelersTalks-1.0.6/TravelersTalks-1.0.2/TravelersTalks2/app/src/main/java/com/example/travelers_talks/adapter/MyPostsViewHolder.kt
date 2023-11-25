package com.example.travelers_talks.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.travelers_talks.MyPosts
import com.example.travelers_talks.R
import com.example.travelers_talks.editPost
import com.example.travelers_talks.editpostdraft
import com.example.travelers_talks.fragment_myposts
import com.example.travelers_talks.fragment_myposts.Companion.REQUEST_CODE_DELETE_POST
import com.example.travelers_talks.fullPostActivity

import com.example.travelers_talks.popUpDeletePost

class MyPostsViewHolder(view : View, private val context: Context,private val fragment: fragment_myposts): ViewHolder(view) {

    val imageView: ImageView = view.findViewById(R.id.ivDelete)
    val ivEditPost: ImageView = view.findViewById(R.id.ivEdit)

    init {
        // Configurar un OnClickListener para el ImageView
        val position = adapterPosition
        imageView.setOnClickListener {
            val position = adapterPosition
            val showPopUp = popUpDeletePost.newInstance(position)
            showPopUp.setTargetFragment(fragment, REQUEST_CODE_DELETE_POST)
            showPopUp.show((view.context as AppCompatActivity).supportFragmentManager, "showPopUp")
        }
        ivEditPost.setOnClickListener {
            val position = adapterPosition
            val intent = Intent(view.context, editPost::class.java)
            intent.putExtra("opcionMain", 1)
            intent.putExtra("position",position)
            context.startActivity(intent)
        }
    }

    val arrowImg = view.findViewById<ImageView>(R.id.ivArrow)
    val title = view.findViewById<TextView>(R.id.txtTitle)
    val date = view.findViewById<TextView>(R.id.txtDate)
    val editImg = view.findViewById<ImageView>(R.id.ivEdit)
    val deleteImg = view.findViewById<ImageView>(R.id.ivDelete)

    fun render(myposts: MyPosts){
        Glide.with(arrowImg.context).load(myposts.arrowImg).into(arrowImg)
        title.text = myposts.Title
        date.text = myposts.Date
        Glide.with(editImg.context).load(myposts.editImg).into(editImg)
        Glide.with(deleteImg.context).load(myposts.deleteImg).into(deleteImg)
    }


}