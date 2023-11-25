package com.example.travelers_talks.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.travelers_talks.MyFavorites
import com.example.travelers_talks.R
import com.example.travelers_talks.fpostfavorites
import com.example.travelers_talks.fragment_myfavorites
import com.example.travelers_talks.fragment_myposts
import com.example.travelers_talks.fullPostActivity
import com.example.travelers_talks.popUpMyFDelete

class MyFavoritesViewHolder(view: View, private val context: Context, private val fragment: fragment_myfavorites) : ViewHolder(view) {

    val imageView: ImageView = view.findViewById(R.id.ivDelete)
    val textVerMas: TextView = view.findViewById(R.id.txtVerMas)

    init {
        // Configurar un OnClickListener para el ImageView
        imageView.setOnClickListener {
            val position = adapterPosition
            val showPopUp = popUpMyFDelete.newInstance(position)
            showPopUp.setTargetFragment(fragment, fragment_myposts.REQUEST_CODE_DELETE_POST)
            showPopUp.show((view.context as AppCompatActivity).supportFragmentManager, "showPopUp")
        }
        textVerMas.setOnClickListener {
            val position = adapterPosition
            val intent = Intent(view.context, fpostfavorites::class.java)
            intent.putExtra("opcionMain", 3)
            intent.putExtra("position",position)
            context.startActivity(intent)
        }
    }


    val bookmarkImg = view.findViewById<ImageView>(R.id.ivBookMark)
    val title = view.findViewById<TextView>(R.id.txtTitle)
    val date = view.findViewById<TextView>(R.id.txtDate)
    val deleteImg = view.findViewById<ImageView>(R.id.ivDelete)

    fun render(myfavorites: MyFavorites){
        title.text = myfavorites.titlePost
        date.text = myfavorites.datePost
        Glide.with(deleteImg.context).load(myfavorites.deleteImg).into(deleteImg)
        Glide.with(bookmarkImg.context).load(myfavorites.favoriteImg).into(bookmarkImg)
    }
}