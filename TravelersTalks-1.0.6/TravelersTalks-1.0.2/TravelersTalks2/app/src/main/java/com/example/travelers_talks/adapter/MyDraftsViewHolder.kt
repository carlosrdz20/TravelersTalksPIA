package com.example.travelers_talks.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.travelers_talks.MyDrafts
import com.example.travelers_talks.R
import com.example.travelers_talks.editPost
import com.example.travelers_talks.editdraftpost
import com.example.travelers_talks.editpostdraft
import com.example.travelers_talks.fragment_mydrafts
import com.example.travelers_talks.fragment_mydrafts.Companion.REQUEST_CODE_SEND_POST
import com.example.travelers_talks.fragment_myposts
import com.example.travelers_talks.popUpMyDDelete
import com.example.travelers_talks.popUpMyDPost
import com.example.travelers_talks.popUpMyFDelete

class MyDraftsViewHolder(view:View, private val context: Context,private val fragment: fragment_mydrafts): ViewHolder(view) {

    val imageView: ImageView = view.findViewById(R.id.ivDelete)

    init {
        // Configurar un OnClickListener para el ImageView
        imageView.setOnClickListener {
            val position = adapterPosition
            val showPopUp = popUpMyDDelete.newInstance(position)
            showPopUp.setTargetFragment(fragment, REQUEST_CODE_SEND_POST)
            showPopUp.show((view.context as AppCompatActivity).supportFragmentManager, "showPopUp")
        }
    }

    val imageViewSend: ImageView = view.findViewById(R.id.ivSend)

    init {
        // Configurar un OnClickListener para el ImageView
        imageViewSend.setOnClickListener {
            val position = adapterPosition
            val showPopUp = popUpMyDPost.newInstance(position)
            showPopUp.setTargetFragment(fragment, REQUEST_CODE_SEND_POST)
            showPopUp.show((view.context as AppCompatActivity).supportFragmentManager, "showPopUp")
        }
    }

    val imageEdit: ImageView = view.findViewById(R.id.ivEdit)

    init {
        // Configurar un OnClickListener para el ImageView
        imageEdit.setOnClickListener {
            val position = adapterPosition
            val intent = Intent(view.context, editpostdraft::class.java)
            intent.putExtra("position",position)
            intent.putExtra("opcionMain", 2)
            context.startActivity(intent)
        }
    }


    val arrowImg = view.findViewById<ImageView>(R.id.ivArrow)
    val title = view.findViewById<TextView>(R.id.txtTitle)
    val date = view.findViewById<TextView>(R.id.txtDate)
    val editImg = view.findViewById<ImageView>(R.id.ivEdit)
    val deleteImg = view.findViewById<ImageView>(R.id.ivDelete)
    val sendImg = view.findViewById<ImageView>(R.id.ivSend)

    fun render(mydrafts: MyDrafts){
        Glide.with(arrowImg.context).load(mydrafts.arrowimg).into(arrowImg)
        title.text = mydrafts.Title
        date.text = mydrafts.Date
        Glide.with(editImg.context).load(mydrafts.editarBttn).into(editImg)
        Glide.with(deleteImg.context).load(mydrafts.borrarBttn).into(deleteImg)
        Glide.with(sendImg.context).load(mydrafts.sendBttn).into(sendImg)
    }


}