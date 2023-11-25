package com.example.travelers_talks

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.travelers_talks.data.UserSingleton
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class fullPostActivity : AppCompatActivity(){

    val list = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullpost)
        val carousel: ImageCarousel = findViewById(R.id.carouselPost)
        val ivHeart = findViewById<ImageView>(R.id.imgBlankHeart)
        val followingButton = findViewById<Button>(R.id.bttnFollow)
        carousel.addData(list)
        val tabPosition = intent.getIntExtra("tabPosition", 2)
        println("tabPosition: $tabPosition")

        val position = intent.getIntExtra("position", -1)
        if (position != -1) {
            val textUser = findViewById<TextView>(R.id.txtuserName)
            val titlePost = findViewById<TextView>(R.id.txtTituloPost)
            val textRating = findViewById<TextView>(R.id.txtRating)
            val textDescription = findViewById<TextView>(R.id.txtDescripcionPost)
            val ivFlag = findViewById<ImageView>(R.id.ivFlag)
            val ivPfp = findViewById<ImageView>(R.id.imgProfilePicture)
            val bitmap = PostsProvider.PostList[position].pfpImg?.let { BitmapFactory.decodeByteArray(PostsProvider.PostList[position].pfpImg, 0, it.size) }
            ivPfp.setImageBitmap(bitmap)
            textUser.text = PostsProvider.PostList[position].NickName.toString()
            titlePost.text = PostsProvider.PostList[position].Title
            textRating.text = PostsProvider.PostList[position].Rating.toString()
            textDescription.text = PostsProvider.PostList[position].description
            Glide.with(ivFlag.context).load(PostsProvider.PostList[position].FlagImg).into(ivFlag)
            Glide.with(ivHeart.context).load(PostsProvider.PostList[position].HeartImg).into(ivHeart)
            PostsProvider.PostList[position].PostImg?.forEach { elemento->
                // Guarda el byteArray como un archivo temporal
                val filename = "${System.currentTimeMillis()}.jpg"
                val file = File(this.cacheDir, filename)
                file.writeBytes(elemento ?: byteArrayOf())

                // Guarda la ruta del archivo para usarla en el ImageCarousel
                val filePath = file.absolutePath

                val tempList = ArrayList(list)
                val newCarouselItem = CarouselItem(filePath)
                if (!tempList.contains(newCarouselItem)) {
                    tempList.add(newCarouselItem)
                    carousel.addData(tempList)
                }
            }

            followingButton.setText(PostsProvider.PostList[position].Button)
        }

        ivHeart.setOnClickListener {

            if(PostsProvider.PostList[position].HeartImg === "https://static.thenounproject.com/png/4590951-200.png"){
                PostsProvider.PostList[position].HeartImg = "https://pixsector.com/cache/5f5275bf/av83bcb11eee42d2ca6cd.png"
                Glide.with(ivHeart.context).load(PostsProvider.PostList[position].HeartImg).into(ivHeart)
                Log.e("Position: ", position.toString())
                Log.e("Heart", PostsProvider.PostList[position].HeartImg)
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiResponseInsertSave(UserSingleton.currentUserId,PostsProvider.PostList[position].idPost)
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
                PostsProvider.PostList[position].HeartImg = "https://static.thenounproject.com/png/4590951-200.png"
                Glide.with(ivHeart.context).load(PostsProvider.PostList[position].HeartImg).into(ivHeart)
                Log.e("Position: ", position.toString())
                Log.e("Heart", PostsProvider.PostList[position].HeartImg)
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiResponseInsertSave(UserSingleton.currentUserId,PostsProvider.PostList[position].idPost)
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

        followingButton.setOnClickListener {

            if(PostsProvider.PostList[position].Button === " + FOLLOW"){
                PostsProvider.PostList[position].Button = "  Following"
                followingButton.setText(PostsProvider.PostList[position].Button)
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiInsertFollowers(UserSingleton.currentUserId,PostsProvider.PostList[position].idUser)
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
                PostsProvider.PostList[position].Button = " + FOLLOW"
                followingButton.setText(PostsProvider.PostList[position].Button)
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiInsertFollowers(UserSingleton.currentUserId,PostsProvider.PostList[position].idUser)
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




        val Return : ImageView = findViewById(R.id.ivArrowReturn)

        Return.setOnClickListener{
            val extras = intent.extras

            if (extras != null) {
                val datoRecibido = extras.get("opcionMain")

                when (datoRecibido) {
                    0->{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("opcionMain", 0)
                        startActivity(intent)
                    }
                    1->{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("opcionMain", 1)
                        startActivity(intent)
                    }
                }
            }
        }

        val rating: RatingBar = findViewById(R.id.rbRate)

        val apiInterface = RetrofitInstance.instance
        val call: Call<ApiResponseRating> = apiInterface.getApiResponseRating(UserSingleton.currentUserId,PostsProvider.PostList[position].idPost)
        call.enqueue(object : Callback<ApiResponseRating> {
            override fun onResponse(call: Call<ApiResponseRating>, response: Response<ApiResponseRating>) {
                if (response.isSuccessful) {
                    val apiResponse: ApiResponseRating? = response.body()
                    if (apiResponse != null) {
                        Log.e("Usuario: ", UserSingleton.currentUserId.toString())
                        Log.e("Post Id: ", PostsProvider.PostList[position].idPost.toString())
                        Log.e("Exito", apiResponse.rate.toString())
                        rating.rating = apiResponse.rate.toFloat()
                    }
                } else {
                    Log.e("Cosas Error: ", "No jaló")
                }
            }
            override fun onFailure(call: Call<ApiResponseRating>, t: Throwable) {
                Log.e("Error en la solicitud:", t.message.toString())
            }
        })

        rating.setOnRatingBarChangeListener { _, rating, _ ->
            val mensaje = "Valor de RatingBar: ${rating.toInt()}"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

            val apiInterface = RetrofitInstance.instance
            val call: Call<ApiRespone> = apiInterface.getApiResponseInsertRating(UserSingleton.currentUserId,PostsProvider.PostList[position].idPost,rating.toInt())
            call.enqueue(object : Callback<ApiRespone> {
                override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                    if (response.isSuccessful) {
                        val apiResponse: ApiRespone? = response.body()
                        if (apiResponse != null) {
                            Log.e("Exito", apiResponse.resultado)
                        }
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