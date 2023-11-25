package com.example.travelers_talks

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
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
import android.widget.Button
import android.widget.RatingBar
import com.bumptech.glide.Glide
import com.example.travelers_talks.adapter.PostsAdapter
import com.example.travelers_talks.adapter.UserForeignAdapter
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class otherprofile : AppCompatActivity(){

    val list = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otherprofile)
        val position = intent.getIntExtra("position", -1)
        val imageUser: ImageView = findViewById(R.id.imgProfilePicture)
        val fullname: TextView = findViewById(R.id.txtName)
        val usernickname: TextView = findViewById(R.id.txtUsername)
        val followingText: TextView = findViewById(R.id.txtfollowingNumber)
        val followerText: TextView = findViewById(R.id.txtFollowersNumber)
        val bttnFollow: Button = findViewById(R.id.bttnFollow)
        if (position != -1) {
            Log.e("Posicion Other Profile:", position.toString());
            val apiInterface = RetrofitInstance.instance
            val call: Call<ApiResponseUserForeign> = apiInterface.getApiResponseGetForeign(PostsProvider.PostList[position].idUser, UserSingleton.currentUserId)
            call.enqueue(object : Callback<ApiResponseUserForeign> {
                override fun onResponse(call: Call<ApiResponseUserForeign>, response: Response<ApiResponseUserForeign>) {
                    if (response.isSuccessful) {
                        val apiResponse: ApiResponseUserForeign? = response.body()
                        Log.e("Exito", "Que pro en 4K")
                        val base64WithoutPrefixImagenUno = apiResponse?.imageURL?.removePrefix("data:image/png;base64,")
                        val bitmap: ByteArray = java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno)
                        val bitmaperal = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
                        imageUser.setImageBitmap(bitmaperal)
                        if (apiResponse != null) {
                            fullname.text = apiResponse.nameUser + " " + apiResponse.lastName
                            usernickname.text = apiResponse.nickname
                            followingText.text = apiResponse.followingCount.toString()
                            followerText.text = apiResponse.followerCount.toString()
                            Log.e("Ayuda:", apiResponse.activeF.toString())
                            if(apiResponse.activeF == 1){
                                bttnFollow.text = "Following"
                            }else if(apiResponse.activeF == 0){
                                bttnFollow.text = " FOLLOW +"
                            }
                        }
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }
                override fun onFailure(call: Call<ApiResponseUserForeign>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })

            Log.e("Id del usuario: ", PostsProvider.PostList[position].idUser.toString())
            val apiInterface2 = RetrofitInstance.instance
            val call2: Call<List<ApiReponseForeignPosts>> = apiInterface2.getApiForeignPosts(PostsProvider.PostList[position].idUser, UserSingleton.currentUserId)
            call2.enqueue(object : Callback<List<ApiReponseForeignPosts>> {
                override fun onResponse(call: Call<List<ApiReponseForeignPosts>>, response: Response<List<ApiReponseForeignPosts>>) {
                    if (response.isSuccessful) {
                        UFProvider.ufPosts.clear()
                        val apiResponse: List<ApiReponseForeignPosts>? = response.body()
                        val newByteArrayList: MutableList<ByteArray> = mutableListOf()
                        apiResponse?.forEach { post ->
                            Log.e("Aqui empieza el Post", "Aqui")
                            Log.e("Post Id: ", post.PostId.toString())
                            post.TituloPost?.let { Log.e("Titulo Post: ", it) }
                            Log.e("Id Country: ", post.IdCount.toString())
                            Log.e("Id User: ", post.IdUser.toString())
                            post.UserNickname?.let { Log.e("Nickname: ", it) }
                            post.imageUser?.let { Log.e("ImageUser: ", it) }
                            post.CountryName?.let { Log.e("Country Name: ", it) }
                            post.countryImg?.let { Log.e("Country Img: ", it) }
                            val base64WithoutPrefixImageUser = post.imageUser?.removePrefix("data:image/png;base64,")
                            val base64WithoutPrefixCountryImage = post.countryImg?.removePrefix("data:image/png;base64,")
                            if (base64WithoutPrefixImageUser != null) {
                                Log.e("Image User Base64:", base64WithoutPrefixImageUser)
                            }
                            if (base64WithoutPrefixCountryImage != null) {
                                Log.e("Country Image Base64:", base64WithoutPrefixCountryImage)
                            }
                            post.ImagenUno?.let {
                                val base64WithoutPrefixImagenUno = post.ImagenUno.removePrefix("data:image/png;base64,")
                                newByteArrayList.add(java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno))
                            }
                            post.ImagenDos?.let {
                                val base64WithoutPrefixImagenDos = post.ImagenDos.removePrefix("data:image/png;base64,")
                                newByteArrayList.add(java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenDos)) }
                            post.ImagenTres?.let {
                                val base64WithoutPrefixImagenTres = post.ImagenTres.removePrefix("data:image/png;base64,")
                                newByteArrayList.add(java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenTres)) }

                            var stringSave:String?=null

                            if(post.activeS == 1){
                                stringSave = "https://pixsector.com/cache/5f5275bf/av83bcb11eee42d2ca6cd.png"
                            }else{
                                stringSave = "https://static.thenounproject.com/png/4590951-200.png"
                            }

                            val postA = PostsForeign(
                                post.UserNickname,
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixImageUser),
                                post.DatePost,
                                post.TituloPost,
                                post.Rating,
                                "https://www.pngall.com/wp-content/uploads/2017/05/Star-PNG-Image.png",
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixCountryImage),
                                stringSave,
                                " + FOLLOW",
                                newByteArrayList.toList(),
                                post.PostId,
                                post.IdCount,
                                post.IdUser,
                                post.Description
                            )
                            UFProvider.ufPosts += postA
                            newByteArrayList.clear()
                        }
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPostsForeign)
                        recyclerView.layoutManager = LinearLayoutManager(this@otherprofile)
                        recyclerView.adapter = UserForeignAdapter(UFProvider.ufPosts, position)
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }

                override fun onFailure(call: Call<List<ApiReponseForeignPosts>>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })

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



        val btnFollow: Button = findViewById(R.id.bttnFollow)

        btnFollow.setOnClickListener {

            if(PostsProvider.PostList[position].Button === " + FOLLOW"){
                PostsProvider.PostList[position].Button = "  Following"
                btnFollow.text = PostsProvider.PostList[position].Button
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiInsertFollowers(UserSingleton.currentUserId,PostsProvider.PostList[position].idUser)
                call.enqueue(object : Callback<ApiRespone> {
                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiRespone? = response.body()
                            Log.e("Exito", "Que pro en 4K")
                            val apiInterface = RetrofitInstance.instance
                            val call: Call<ApiResponseUserForeign> = apiInterface.getApiResponseGetForeign(PostsProvider.PostList[position].idUser)
                            call.enqueue(object : Callback<ApiResponseUserForeign> {
                                override fun onResponse(call: Call<ApiResponseUserForeign>, response: Response<ApiResponseUserForeign>) {
                                    if (response.isSuccessful) {
                                        val apiResponse: ApiResponseUserForeign? = response.body()
                                        Log.e("Exito", "Que pro en 4K")
                                        val base64WithoutPrefixImagenUno = apiResponse?.imageURL?.removePrefix("data:image/png;base64,")
                                        val bitmap: ByteArray = java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno)
                                        val bitmaperal = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
                                        imageUser.setImageBitmap(bitmaperal)
                                        if (apiResponse != null) {
                                            fullname.text = apiResponse.nameUser + " " + apiResponse.lastName
                                            usernickname.text = apiResponse.nickname
                                            followingText.text = apiResponse.followingCount.toString()
                                            followerText.text = apiResponse.followerCount.toString()
                                        }
                                    } else {
                                        Log.e("Cosas Error: ", "No jaló")
                                    }
                                }
                                override fun onFailure(call: Call<ApiResponseUserForeign>, t: Throwable) {
                                    Log.e("Error en la solicitud:", t.message.toString())
                                }
                            })
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
                btnFollow.text = PostsProvider.PostList[position].Button
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiInsertFollowers(UserSingleton.currentUserId,PostsProvider.PostList[position].idUser)
                call.enqueue(object : Callback<ApiRespone> {
                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiRespone? = response.body()
                            Log.e("Exito", "Que pro en 4K")
                            val apiInterface = RetrofitInstance.instance
                            val call: Call<ApiResponseUserForeign> = apiInterface.getApiResponseGetForeign(PostsProvider.PostList[position].idUser)
                            call.enqueue(object : Callback<ApiResponseUserForeign> {
                                override fun onResponse(call: Call<ApiResponseUserForeign>, response: Response<ApiResponseUserForeign>) {
                                    if (response.isSuccessful) {
                                        val apiResponse: ApiResponseUserForeign? = response.body()
                                        Log.e("Exito", "Que pro en 4K")
                                        val base64WithoutPrefixImagenUno = apiResponse?.imageURL?.removePrefix("data:image/png;base64,")
                                        val bitmap: ByteArray = java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno)
                                        val bitmaperal = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
                                        imageUser.setImageBitmap(bitmaperal)
                                        if (apiResponse != null) {
                                            fullname.text = apiResponse.nameUser + " " + apiResponse.lastName
                                            usernickname.text = apiResponse.nickname
                                            followingText.text = apiResponse.followingCount.toString()
                                            followerText.text = apiResponse.followerCount.toString()
                                        }
                                    } else {
                                        Log.e("Cosas Error: ", "No jaló")
                                    }
                                }
                                override fun onFailure(call: Call<ApiResponseUserForeign>, t: Throwable) {
                                    Log.e("Error en la solicitud:", t.message.toString())
                                }
                            })
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

}