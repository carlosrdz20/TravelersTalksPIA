package com.example.travelers_talks.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travelers_talks.ApiRespone
import com.example.travelers_talks.Posts
import com.example.travelers_talks.PostsForeign
import com.example.travelers_talks.PostsProvider
import com.example.travelers_talks.R
import com.example.travelers_talks.RetrofitInstance
import com.example.travelers_talks.UFProvider
import com.example.travelers_talks.data.UserSingleton
import com.example.travelers_talks.fullPostActivity
import com.example.travelers_talks.fullpostuf
import com.example.travelers_talks.otherprofile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserForeignViewHolder(view: View, private val context: Context,  private val miValor: Int): RecyclerView.ViewHolder(view) {

    val postNickName = view.findViewById<TextView>(R.id.tvUserName)
    val postImgPfp = view.findViewById<ImageView>(R.id.ivProfilePicture)
    val postDate = view.findViewById<TextView>(R.id.tvDate)
    val postTtl = view.findViewById<TextView>(R.id.tvTituloPost)
    val postRating = view.findViewById<TextView>(R.id.tvRating)
    val postRatingImg = view.findViewById<ImageView>(R.id.ivStars)
    val postFlagImg = view.findViewById<ImageView>(R.id.ivFlag)
    val postImg = view.findViewById<ImageView>(R.id.ivPicturePost)
    val postBttn = view.findViewById<Button>(R.id.bttnFollow)
    val textview: TextView = view.findViewById(R.id.txtVerMas)
    val postHeartImg = view.findViewById<ImageView>(R.id.ivBlankHeart)

    init {

        textview.setOnClickListener {
            val position = adapterPosition
            val intent = Intent(view.context,fullpostuf::class.java)
            intent.putExtra("opcionMain", 0)
            intent.putExtra("temporal",miValor)
            intent.putExtra("position",position)
            context.startActivity(intent)
        }

        val btnHeart: ImageView = view.findViewById(R.id.ivBlankHeart)
        btnHeart.setOnClickListener {
            val position = adapterPosition
            if(UFProvider.ufPosts[position].HeartImg === "https://static.thenounproject.com/png/4590951-200.png"){
                UFProvider.ufPosts[position].HeartImg = "https://pixsector.com/cache/5f5275bf/av83bcb11eee42d2ca6cd.png"
                Glide.with(postHeartImg.context).load(UFProvider.ufPosts[position].HeartImg).into(postHeartImg)
                Log.e("Position: ", position.toString())
                Log.e("Heart", UFProvider.ufPosts[position].HeartImg)
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiResponseInsertSave(UserSingleton.currentUserId,UFProvider.ufPosts[position].idPost)
                call.enqueue(object : Callback<ApiRespone> {
                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiRespone? = response.body()
                            Log.e("Exito", "Que pro en 4K")
                        } else {
                            Log.e("Cosas Error: ", "No jaló")
                        }
                    }
                    override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                        Log.e("Error en la solicitud:", t.message.toString())
                    }
                })
            }else{
                UFProvider.ufPosts[position].HeartImg = "https://static.thenounproject.com/png/4590951-200.png"
                Glide.with(postHeartImg.context).load(UFProvider.ufPosts[position].HeartImg).into(postHeartImg)
                Log.e("Position: ", position.toString())
                Log.e("Heart", UFProvider.ufPosts[position].HeartImg)
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiResponseInsertSave(UserSingleton.currentUserId,UFProvider.ufPosts[position].idPost)
                call.enqueue(object : Callback<ApiRespone> {
                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiRespone? = response.body()
                            Log.e("Exito", "Que pro en 4K")
                        } else {
                            Log.e("Cosas Error: ", "No jaló")
                        }
                    }
                    override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                        Log.e("Error en la solicitud:", t.message.toString())
                    }
                })
            }
        }


    }




    fun render(foreignpostsModel: PostsForeign){
        postNickName.text = foreignpostsModel.NickName
        Glide.with(postImgPfp.context).load(foreignpostsModel.pfpImg).into(postImgPfp)
        postDate.text = foreignpostsModel.Date
        postTtl.text = foreignpostsModel.Title
        postRating.text = foreignpostsModel.Rating.toString()
        Glide.with(postRatingImg.context).load(foreignpostsModel.RatingImg).into(postRatingImg)
        Glide.with(postFlagImg.context).load(foreignpostsModel.FlagImg).into(postFlagImg)
        Glide.with(postHeartImg.context).load(foreignpostsModel.HeartImg).into(postHeartImg)
        Glide.with(postImg.context).load(foreignpostsModel.PostImg?.get(0)).into(postImg)
    }
}