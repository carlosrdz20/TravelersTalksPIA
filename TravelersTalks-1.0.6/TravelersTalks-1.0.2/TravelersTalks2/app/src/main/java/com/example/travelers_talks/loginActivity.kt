package com.example.travelers_talks

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelers_talks.data.DataDbHelper
import com.example.travelers_talks.data.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


import android.renderscript.*
import com.example.travelers_talks.data.Users
import java.util.Base64

class loginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dbHelper: DataDbHelper
    var currentIndex = 1
    var image: String?=null
    var image2: String?=null
    var image3: String?=null
    var lastid: Int ?= null
    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        dbHelper = DataDbHelper(this)

        //Poner el link de registro con el estilo de subrayado
        val textView = findViewById<TextView>(R.id.linkARegistro)
        val paint = textView.paint
        paint.isUnderlineText = true

        var linkRegistro = findViewById<TextView>(R.id.linkARegistro)
        linkRegistro.setOnClickListener(this)

        var btnLogin = findViewById<TextView>(R.id.btnSubmitLogin)
        btnLogin.setOnClickListener(this)

        if(isInternetAvailable()){
            Log.e("Ayuda", "Que pedo")
            dbHelper.showPostsOffline(this)
            if(!PostsOfflineList.PostsOfflineList.isEmpty()){
                PostsOfflineList.PostsOfflineList.forEach { elemento->
                    Log.e("Title", elemento.Title.toString())
                    Log.e("Descriocion", elemento.description.toString())
                    Log.e("idUser", elemento.idUser.toString())
                    Log.e("Pais", elemento.idCountry.toString())
                    Log.e("Id", elemento.id.toString())
                    elemento.PostImg?.forEach {byte->
                        val base64String = Base64.getEncoder().encodeToString(byte)
                        val strEncodeImage:String = "data:image/png;base64," + base64String
                        if(currentIndex <= 3){
                            when (currentIndex) {
                                1 -> image = strEncodeImage
                                2 -> image2 = strEncodeImage
                                3 -> image3 = strEncodeImage
                            }
                            currentIndex++
                        }
                    }
                    val apiInterface = RetrofitInstance.instance
                    val call: Call<ApiRespone> = apiInterface.getApiInsertOffline(elemento.Title,elemento.description,elemento.idUser,elemento.idCountry,image,image2,image3,elemento.active)
                    call.enqueue(object : Callback<ApiRespone> {
                        override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                            if (response.isSuccessful) {
                                val apiResponse: ApiRespone? = response.body()
                                if (apiResponse != null) {
                                    Log.e("Si se logró", "Todo")
                                    elemento.id?.let { dbHelper.deletePost(it) }
                                }
                            } else {
                                Log.e("Cosas Error: ", "No jaló")
                                elemento.id?.let { dbHelper.deletePost(it) }
                            }
                        }
                        override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                            Log.e("Error en la solicitud:", t.message.toString())
                            elemento.id?.let { dbHelper.deletePost(it) }
                        }
                    })
                    currentIndex = 1
                }
            }
            var idusuario = dbHelper.showToastWithUserDetails(this)
            Log.e("Usuario a borrar:", idusuario.toString())
            if (idusuario != null) {
                val base64String = Base64.getEncoder().encodeToString(idusuario.imageURL)
                val strEncodeImage:String = "data:image/png;base64," + base64String
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiEditResponse(
                    idusuario.idUser!!,idusuario.nickname!!,
                    idusuario.nameUser!!, idusuario.lastName!!, idusuario.email!!, idusuario.passwordUser!!, strEncodeImage)
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
                dbHelper.deleteUser(idusuario.idUser)
            }
        }

    }

    override fun onClick(p0: View?) {
        if(p0!!.id == R.id.linkARegistro){
            val intent = Intent(this, registroActivity::class.java)
            startActivity(intent);
        }



        if(p0!!.id == R.id.btnSubmitLogin){
            var progress = findViewById<ProgressBar>(R.id.progressBar)
            progress.visibility = View.VISIBLE

            val dbHelper = DataDbHelper(this)
            var etEmail = findViewById<EditText>(R.id.editEmail)
            var etPassword = findViewById<EditText>(R.id.editPassword)
            var email = etEmail.text.toString()
            var password = etPassword.text.toString()

            val apiInterface = RetrofitInstance.instance
            val call: Call<UsersResponse> = apiInterface.getApiResponseUnique(email,password)

            call.enqueue(object : Callback<UsersResponse> {
                override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse: UsersResponse? = response.body()
                        apiResponse?.let { Log.e("Exito", it.nickname) }
                        UserSingleton.currentUserNickname = apiResponse!!.nickname
                        UserSingleton.currentUserId = apiResponse.idUser
                        UserSingleton.currentUserName = apiResponse.nameUser
                        UserSingleton.currentUserEmail = apiResponse.email
                        UserSingleton.currentUserPassword = apiResponse.passwordUser
                        UserSingleton.currentUserLastName = apiResponse.lastName
                        UserSingleton.currentUserImg = apiResponse.imageURL
                        val imageDataBytes = java.util.Base64.getDecoder().decode(UserSingleton.currentUserImg!!.substringAfter(','))
                        val decodedImageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.size)
                        var userio =  Users(
                        UserSingleton.currentUserId,
                        UserSingleton.currentUserNickname,
                        UserSingleton.currentUserName,
                        UserSingleton.currentUserLastName,
                        UserSingleton.currentUserEmail,
                        UserSingleton.currentUserPassword,
                        imageDataBytes
                        )
                        dbHelper.insertUser(userio)

                        progress.visibility = View.GONE
                        credential()
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                        onFailed()
                    }
                }
                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                    onFailed()
                }
            })


        }
    }

    fun convertDrawableToByteArray(context: Context, drawableId: Int): ByteArray {
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun credential(){
        if (UserSingleton.currentUserNickname!!.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("opcionMain", 0)
            startActivity(intent);
        } else {
            Toast.makeText(this, "Credenciales no válidas", Toast.LENGTH_SHORT).show()
        }
    }

    fun onFailed(){
        Toast.makeText(this, "Credenciales no válidas", Toast.LENGTH_SHORT).show()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                // Puede agregar más transportes según sus necesidades
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }




}