package com.example.travelers_talks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelers_talks.data.CountryId
import com.example.travelers_talks.data.DataDbHelper
import com.example.travelers_talks.data.Post
import com.example.travelers_talks.data.UserSingleton
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.Base64

class createPosts : AppCompatActivity(){
    private lateinit var dbHelper: DataDbHelper

    var countryId:Int?= null
    var countryName:String?= null
    private val PICK_IMAGE_REQUEST = 1
    var byteArray: ByteArray? = null
    private lateinit var carouselPost: ImageCarousel
    private lateinit var imgPrueba: ImageView
    val list = mutableListOf<CarouselItem>()
    val listCountries = mutableListOf<CountryId>()
    val imageByteArrays: MutableList<ByteArray> = mutableListOf()
    var country: CountryId ?= null
    var lastid: Int ?= null
    var image: String?=null
    var image2: String?=null
    var image3: String?=null
    var currentIndex = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.createpost)
        dbHelper = DataDbHelper(this)
        val btnReturn: ImageView = findViewById(R.id.ivArrowReturn)
        if(isInternetAvailable()){
            Log.e("Si hay", "Internet")
            val apiInterface = RetrofitInstance.instance
            val call: Call<List<ApiResponseCountries>> = apiInterface.getApiCountries()
            call.enqueue(object : Callback<List<ApiResponseCountries>> {
                override fun onResponse(call: Call<List<ApiResponseCountries>>, response: Response<List<ApiResponseCountries>>) {
                    if (response.isSuccessful) {
                        val apiResponse: List<ApiResponseCountries>? = response.body()
                        if (apiResponse != null && apiResponse.isNotEmpty()) {
                            for (i in 0 until apiResponse.size) {
                                country = CountryId(
                                    apiResponse[i].idCountry,
                                    apiResponse[i].countryName
                                )
                                listCountries.add(country!!)
                                Log.e("A:",apiResponse[i].idCountry.toString())
                            }
                        }
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }
                override fun onFailure(call: Call<List<ApiResponseCountries>>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })
        }else{
            Log.e("No hay", "Internet")
            val countryData = dbHelper.getCountryNames()
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, countryData)
            val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.actvdropwon_field)
            autoCompleteTextView.setAdapter(adapter)

            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                val selectedCountry = parent.adapter.getItem(position) as CountryId
                countryId = selectedCountry.id
                countryName = selectedCountry.name

                Log.d("Country Info", "ID: $countryId, Name: $countryName")
            }
        }

        btnReturn.setOnClickListener {
            val showPopUp = popUpCPCancel()
            showPopUp.show((this as AppCompatActivity).supportFragmentManager, "showPopUp")
        }

        val btnPost: Button = findViewById(R.id.btnPost)

        btnPost.setOnClickListener {
            var postFun: Post = Post()
            var titleBool:Boolean = false
            var descrposBool:Boolean = false
            var imagen1:Boolean = false
            var imagen2:Boolean = false
            var countryBool:Boolean = false
            val editTitle = findViewById<EditText>(R.id.etTitulo)
            val editDescription = findViewById<EditText>(R.id.etDescripcion)
            val titlePost = editTitle.text.toString()
            val descrPost = editDescription.text.toString()
            if (titlePost.isNotEmpty() && descrPost.isNotEmpty()) {
                titleBool = true
                descrposBool = true
                if (image != null && image2 != null) {

                    imagen1 = true
                    imagen2 = true
                    postFun.titlePost = titlePost
                    postFun.description = descrPost
                    postFun.idUser = UserSingleton.currentUserId
                    postFun.idCountry = countryId
                    if(postFun.idCountry != null){
                        countryBool = true
                    }else{
                        countryBool = false
                        Toast.makeText(this, "Por favor ingresa un país", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if(imagen1 ===  false){
                        imagen1 = false
                        Toast.makeText(this, "Por favor ingresa una primer imagen", Toast.LENGTH_SHORT).show()
                    }
                    if(imagen2 === false){
                        imagen2 = false
                        Toast.makeText(this, "Por favor ingresa una segunda imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if(titleBool === false){
                    titleBool = false
                    Toast.makeText(this, "Por favor ingresa un titulo", Toast.LENGTH_SHORT).show()
                }
                if(descrposBool === false){
                    descrposBool = false
                    Toast.makeText(this, "Por favor ingresa una descripción", Toast.LENGTH_SHORT).show()
                }
            }

            if(isInternetAvailable() && titleBool === true && descrposBool === true && imagen1 === true && imagen2 === true && countryBool === true){
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiResponsePostId> = apiInterface.getApiPostsInsert(postFun.titlePost!!,
                    postFun.description!!,postFun.idUser!!,postFun.idCountry!!,1)
                call.enqueue(object : Callback<ApiResponsePostId> {
                    override fun onResponse(call: Call<ApiResponsePostId>, response: Response<ApiResponsePostId>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiResponsePostId? = response.body()
                            if (apiResponse != null) {
                                Log.e("IdPost:", apiResponse.idpost.toString())
                                lastid = apiResponse.idpost.toInt()
                                imageByteArrays.forEach { byteArray ->
                                    val base64String = Base64.getEncoder().encodeToString(byteArray)
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
                                val apiInterface2 = RetrofitInstance.instance
                                val call: Call<ApiRespone> = apiInterface2.getApiPostsInsertMultimedia(image, lastid!!,image2,image3)
                                call.enqueue(object : Callback<ApiRespone> {
                                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                                        if (response.isSuccessful) {
                                            val apiResponse: ApiRespone? = response.body()
                                            if (apiResponse != null) {
                                                Log.e("Resultado:", apiResponse.resultado)

                                                returnMain()

                                            }
                                        } else {
                                            Log.e("Cosas Error: ", "No jaló")
                                            returnMain()
                                        }
                                    }
                                    override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                                        Log.e("Error en la solicitud:", t.message.toString())
                                        returnMain()
                                    }
                                })
                            }
                        } else {
                            Log.e("Cosas Error: ", "No jaló")
                            returnMain()
                        }
                    }
                    override fun onFailure(call: Call<ApiResponsePostId>, t: Throwable) {
                        Log.e("Error en la solicitud:", t.message.toString())
                    }
                })
            }else if(titleBool === true && descrposBool === true && imagen1 === true && imagen2 === true && countryBool === true){
                var lastId = dbHelper.insertPost(postFun,1)

                dbHelper.insertPostMultimedia(imageByteArrays,lastId)
                dbHelper.showToastMultimedia(this)
            }



        }

        val btnSaveDraft: Button = findViewById(R.id.btnDraft)

        btnSaveDraft.setOnClickListener {
            var postFun: Post = Post()
            val editTitle = findViewById<EditText>(R.id.etTitulo)
            val editDescription = findViewById<EditText>(R.id.etDescripcion)
            currentIndex = 1
            val titlePost = editTitle.text.toString()
            val descrPost = editDescription.text.toString()
            postFun.titlePost = titlePost
            postFun.description = descrPost
            postFun.idUser = UserSingleton.currentUserId
            postFun.idCountry = countryId
            if(postFun.idCountry == null){
                postFun.idCountry = 7
            }
            Log.e("Titulo: ", postFun.titlePost!!)
            Log.e("Titulo: ", postFun.description!!)
            Log.e("Titulo: ", postFun.idUser.toString())
            if(isInternetAvailable()){
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiResponsePostId> = apiInterface.getApiPostsInsert(postFun.titlePost,
                    postFun.description,postFun.idUser,postFun.idCountry,2)
                call.enqueue(object : Callback<ApiResponsePostId> {
                    override fun onResponse(call: Call<ApiResponsePostId>, response: Response<ApiResponsePostId>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiResponsePostId? = response.body()
                            if (apiResponse != null) {
                                Log.e("IdPost:", apiResponse.idpost.toString())
                                lastid = apiResponse.idpost.toInt()
                                imageByteArrays.forEach { byteArray ->
                                    val base64String = Base64.getEncoder().encodeToString(byteArray)
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
                                Log.e("Id Usuario: ", lastid.toString())
                                val apiInterface2 = RetrofitInstance.instance
                                val call: Call<ApiRespone> = apiInterface2.getApiPostsInsertMultimedia(image, lastid!!,image2,image3)
                                call.enqueue(object : Callback<ApiRespone> {
                                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                                        if (response.isSuccessful) {
                                            val apiResponse: ApiRespone? = response.body()
                                            if (apiResponse != null) {
                                                Log.e("Resultado:", apiResponse.resultado)
                                                returnMain()
                                            }
                                        } else {
                                            Log.e("Cosas Error: ", "No jaló")
                                            returnMain()
                                        }
                                    }
                                    override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                                        Log.e("Error en imagenes:", t.message.toString())
                                        returnMain()
                                    }
                                })
                            }
                        } else {
                            Log.e("Cosas Error: ", "No jaló")
                        }
                    }
                    override fun onFailure(call: Call<ApiResponsePostId>, t: Throwable) {
                        Log.e("Error en la solicitud:", t.message.toString())
                        returnMain()
                    }
                })
            }else{
                var lastId = dbHelper.insertPost(postFun,2)

                dbHelper.insertPostMultimedia(imageByteArrays,lastId)
                dbHelper.showToastMultimedia(this)
            }



        }

        if(isInternetAvailable()){
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listCountries)
            val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.actvdropwon_field)
            autoCompleteTextView.setAdapter(adapter)

            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                val selectedCountry = parent.adapter.getItem(position) as CountryId
                countryId = selectedCountry.id
                countryName = selectedCountry.name

                Log.d("Country Info", "ID: $countryId, Name: $countryName")
            }
        }


        val imageSelector = findViewById<ImageView>(R.id.imgPicture)


        val btnImgPic: ImageView = findViewById(R.id.imgPicture)
        val btnImgPic2: ImageView = findViewById(R.id.imgPicture2)
        val btnImgPic3: ImageView = findViewById(R.id.imgPicture3)
        btnImgPic.setOnClickListener { seleccionarImagenDeGaleria(1) }
        btnImgPic2.setOnClickListener { seleccionarImagenDeGaleria(2) }
        btnImgPic3.setOnClickListener { seleccionarImagenDeGaleria(3) }

        val btnImgCamera: ImageView = findViewById(R.id.imgCamera)
        val btnImgCamera2: ImageView = findViewById(R.id.imgCamera2)
        val btnImgCamera3: ImageView = findViewById(R.id.imgCamera3)
        btnImgCamera.setOnClickListener { tomarFotoDesdeCamara(4) }
        btnImgCamera2.setOnClickListener { tomarFotoDesdeCamara(5) }
        btnImgCamera3.setOnClickListener { tomarFotoDesdeCamara(6) }


        val btndelete: ImageView = findViewById(R.id.imgPictureDelete1)
        val btndelete2: ImageView = findViewById(R.id.imgPictureDelete2)
        val btndelete3: ImageView = findViewById(R.id.imgPictureDelete3)

        btndelete.setOnClickListener{
            deleteImage(1)
        }
        btndelete2.setOnClickListener{
            deleteImage(2)
        }
        btndelete3.setOnClickListener{
            deleteImage(3)
        }



    }

    private fun seleccionarImagenDeGaleria(imagenNumero: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imagenNumero)
    }

    private fun tomarFotoDesdeCamara( numeroFoto: Int){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, numeroFoto)
    }

    fun returnMain(){
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
            }
        }

    }


    private fun deleteImage(imagenNumero: Int) {

        when(imagenNumero){
            1->{
                val imageView = findViewById<ImageView>(R.id.imgPost)
                imageView.setImageResource(R.drawable.purecomedy)
                image = null
            }
            2->{
                val imageView = findViewById<ImageView>(R.id.imgPost2)
                imageView.setImageResource(R.drawable.purecomedy)
                image2 = null
            }
            3->{
                val imageView = findViewById<ImageView>(R.id.imgPost3)
                imageView.setImageResource(R.drawable.purecomedy)
                image3 = null
            }
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {

            if(requestCode == 1 || requestCode == 2 || requestCode == 3){
                val imageUri: Uri? = data.data

                when (requestCode) {
                    1 -> {

                        val imageView = findViewById<ImageView>(R.id.imgPost)
                        imageView.setImageURI(imageUri)

                        val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                        val bytes: ByteArray = inputStream?.readBytes() ?: byteArrayOf()
                        val base64Image: String = java.util.Base64.getEncoder().encodeToString(bytes)

                        val base64ImageString: String = "data:image/png;base64,$base64Image"
                        image = base64ImageString
                    }
                    2 -> {

                        val imageView = findViewById<ImageView>(R.id.imgPost2)
                        imageView.setImageURI(imageUri)

                        val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                        val bytes: ByteArray = inputStream?.readBytes() ?: byteArrayOf()
                        val base64Image: String = java.util.Base64.getEncoder().encodeToString(bytes)

                        val base64ImageString: String = "data:image/png;base64,$base64Image"
                        image2 = base64ImageString

                    }
                    3 -> {

                        val imageView = findViewById<ImageView>(R.id.imgPost3)
                        imageView.setImageURI(imageUri)

                        val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                        val bytes: ByteArray = inputStream?.readBytes() ?: byteArrayOf()
                        val base64Image: String = java.util.Base64.getEncoder().encodeToString(bytes)

                        val base64ImageString: String = "data:image/png;base64,$base64Image"
                        image3 = base64ImageString

                    }
                }



            }else if(requestCode == 4 || requestCode == 5 || requestCode == 6){
                val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap

                if (imageBitmap != null) {
                    when (requestCode) {
                        4 -> {
                            val imageView = findViewById<ImageView>(R.id.imgPost)
                            imageView.setImageBitmap(imageBitmap)

                            val stream = ByteArrayOutputStream()
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val bytes: ByteArray = stream.toByteArray()

                            // Codificar bytes a base64 usando java.util.Base64
                            val base64Image: String = Base64.getEncoder().encodeToString(bytes)

                            val base64ImageString: String = "data:image/png;base64,$base64Image"
                            image = base64ImageString
                        }

                        5 -> {
                            val imageView = findViewById<ImageView>(R.id.imgPost2)
                            imageView.setImageBitmap(imageBitmap)

                            val stream = ByteArrayOutputStream()
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val bytes: ByteArray = stream.toByteArray()

                            // Codificar bytes a base64 usando java.util.Base64
                            val base64Image: String = Base64.getEncoder().encodeToString(bytes)

                            val base64ImageString: String = "data:image/png;base64,$base64Image"
                            image2 = base64ImageString
                        }

                        6 -> {
                            val imageView = findViewById<ImageView>(R.id.imgPost3)
                            imageView.setImageBitmap(imageBitmap)
                            Log.e("Debería de tener imagen", "Hijos de puta")

                            val stream = ByteArrayOutputStream()
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val bytes: ByteArray = stream.toByteArray()

                            // Codificar bytes a base64 usando java.util.Base64
                            val base64Image: String = Base64.getEncoder().encodeToString(bytes)

                            val base64ImageString: String = "data:image/png;base64,$base64Image"
                            image3 = base64ImageString
                        }
                    }
                } else {
                    Log.e("ImageBitmap", "Es nulo")
                }
            }

        }
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