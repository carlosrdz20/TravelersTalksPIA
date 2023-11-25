package com.example.travelers_talks

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.travelers_talks.data.UserSingleton
import com.google.android.material.navigation.NavigationView
import android.util.Base64
import android.util.Log
import android.widget.RatingBar
import com.bumptech.glide.Glide
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class fpostfavorites : AppCompatActivity(){

    val list = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fpostfavorites)
        val carousel: ImageCarousel = findViewById(R.id.carouselPost)
        val position = intent.getIntExtra("position", -1)
        if (position != -1) {
            val textUser = findViewById<TextView>(R.id.txtuserName)
            val titlePost = findViewById<TextView>(R.id.txtTituloPost)
            val textRating = findViewById<TextView>(R.id.txtRating)
            val textDescription = findViewById<TextView>(R.id.txtDescripcionPost)
            val ivFlag = findViewById<ImageView>(R.id.ivFlag)
            val ivPfp = findViewById<ImageView>(R.id.imgProfilePicture)
            val prefix = MyFavoritesProvider.myFavoritesList[position].imageUser?.removePrefix("data:image/png;base64,")
            val decode: ByteArray = java.util.Base64.getDecoder().decode(prefix)
            val bitmap = decode?.let { BitmapFactory.decodeByteArray(decode, 0, it.size) }
            ivPfp.setImageBitmap(bitmap)
            textUser.text = MyFavoritesProvider.myFavoritesList[position].usernickname.toString()
            titlePost.text = MyFavoritesProvider.myFavoritesList[position].titlePost
            textRating.text = MyFavoritesProvider.myFavoritesList[position].Rating.toString()
            textDescription.text = MyFavoritesProvider.myFavoritesList[position].descPost
            Glide.with(ivFlag.context).load(MyFavoritesProvider.myFavoritesList[position].countryImg).into(ivFlag)
            MyFavoritesProvider.myFavoritesList[position].imageUno?.let { Log.e("Imagen Antes de Byte: ", it) }
            if(MyFavoritesProvider.myFavoritesList[position].imageUno != null){
                // Guarda el byteArray como un archivo temporal
                val filename = "${System.currentTimeMillis()}.jpg"
                val file = File(this.cacheDir, filename)
                val base64WithoutPrefixImagenUno = MyFavoritesProvider.myFavoritesList[position].imageUno?.removePrefix("data:image/png;base64,")
                val decodeImg = java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno)

                file.writeBytes( decodeImg ?: byteArrayOf())

                // Guarda la ruta del archivo para usarla en el ImageCarousel
                val filePath = file.absolutePath

                val tempList = ArrayList(list)
                val newCarouselItem = CarouselItem(filePath)
                if (!tempList.contains(newCarouselItem)) {
                    tempList.add(newCarouselItem)
                    carousel.addData(tempList)
                }
            }else if(MyFavoritesProvider.myFavoritesList[position].imageDos != null){
                // Guarda el byteArray como un archivo temporal
                val filename = "${System.currentTimeMillis()}.jpg"
                val file = File(this.cacheDir, filename)
                val base64WithoutPrefixImagenUno = MyFavoritesProvider.myFavoritesList[position].imageUno?.removePrefix("data:image/png;base64,")
                val decodeImg = java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno)
                file.writeBytes(
                    decodeImg ?: byteArrayOf())

                // Guarda la ruta del archivo para usarla en el ImageCarousel
                val filePath = file.absolutePath

                val tempList = ArrayList(list)
                val newCarouselItem = CarouselItem(filePath)
                if (!tempList.contains(newCarouselItem)) {
                    tempList.add(newCarouselItem)
                    carousel.addData(tempList)
                }
            }else if(MyFavoritesProvider.myFavoritesList[position].imageTres != null){
                // Guarda el byteArray como un archivo temporal
                val filename = "${System.currentTimeMillis()}.jpg"
                val file = File(this.cacheDir, filename)
                val base64WithoutPrefixImagenUno = MyFavoritesProvider.myFavoritesList[position].imageUno?.removePrefix("data:image/png;base64,")
                val decodeImg = java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno)
                file.writeBytes(decodeImg ?: byteArrayOf())

                // Guarda la ruta del archivo para usarla en el ImageCarousel
                val filePath = file.absolutePath

                val tempList = ArrayList(list)
                val newCarouselItem = CarouselItem(filePath)
                if (!tempList.contains(newCarouselItem)) {
                    tempList.add(newCarouselItem)
                    carousel.addData(tempList)
                }
            }
        }

        val Return : ImageView = findViewById(R.id.ivArrowReturn)

        Return.setOnClickListener{
            Log.e("Si entré aquí", "Perron")
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
                    2->{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("opcionMain", 2)
                        startActivity(intent)
                    }
                    3->{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("opcionMain", 3)
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