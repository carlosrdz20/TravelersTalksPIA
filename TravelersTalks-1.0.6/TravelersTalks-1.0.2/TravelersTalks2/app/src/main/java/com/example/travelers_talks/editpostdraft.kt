package com.example.travelers_talks

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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

class editpostdraft : AppCompatActivity() {

    val REQUEST_CODE = 100
    private val REQUEST_IMAGE_CAPTURE = 1002
    var countryId:Int?= null
    var countryName:String?= null
    var country: CountryId ?= null
    val listCountries = mutableListOf<CountryId>()
    var idpost:Int?=null
    var counterimage = 1
    var image: String?=null
    var image2: String?=null
    var image3: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editpostdraft)


        val apiInterface = RetrofitInstance.instance
        val call: Call<List<ApiResponseCountries>> = apiInterface.getApiCountries()
        call.enqueue(object : Callback<List<ApiResponseCountries>> {
            override fun onResponse(call: Call<List<ApiResponseCountries>>, response: Response<List<ApiResponseCountries>>){
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

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listCountries)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.actvdropwon_field)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCountry = parent.adapter.getItem(position) as CountryId
            countryId = selectedCountry.id
            countryName = selectedCountry.name

            Log.d("Country Info", "ID: $countryId, Name: $countryName")
        }

        val position = intent.getIntExtra("position", -1)
        if (position != -1) {
            MyDraftsProvider.myDraftsList[position].Title?.let { Log.e("Ayuda 1:", it) }
            MyDraftsProvider.myDraftsList[position].description?.let { Log.e("Ayuda 2: ", it) }
            val textTile = findViewById<EditText>(R.id.etTitulo)
            val textDescrip = findViewById<EditText>(R.id.etDescripcion)
            textTile.setText(MyDraftsProvider.myDraftsList[position].Title)
            textDescrip.setText(MyDraftsProvider.myDraftsList[position].description)
            autoCompleteTextView.setText(MyDraftsProvider.myDraftsList[position].countryName, false)
            countryId = MyDraftsProvider.myDraftsList[position].idCountry
            idpost = MyDraftsProvider.myDraftsList[position].idPost
            Log.e("Imagen1", MyDraftsProvider.myDraftsList[position].PostImg.toString());
            Log.e("Imagen2", MyDraftsProvider.myDraftsList[position].PostImg.toString());
            Log.e("Imagen3", MyDraftsProvider.myDraftsList[position].PostImg.toString());
            MyDraftsProvider.myDraftsList[position].PostImg?.forEach { elemento->
                var imageViews:ImageView = findViewById(R.id.imgPost)
                if(counterimage <= 3){

                    when (counterimage) {
                        1 ->{
                            imageViews = findViewById(R.id.imgPost)
                            image = java.util.Base64.getEncoder().encodeToString(elemento)
                        }
                        2 ->{
                            imageViews = findViewById(R.id.imgPost2)
                            image2 = java.util.Base64.getEncoder().encodeToString(elemento)
                        }
                        3 ->{
                            imageViews = findViewById(R.id.imgPost3)
                            image3 = java.util.Base64.getEncoder().encodeToString(elemento)
                        }
                    }
                    counterimage++
                }
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(elemento, 0, elemento.size)
                imageViews.setImageBitmap(bitmap)
            }
        }

        val btnDraft: Button = findViewById(R.id.btnDraft)

        btnDraft.setOnClickListener{
            val textTile = findViewById<EditText>(R.id.etTitulo)
            val textDescrip = findViewById<EditText>(R.id.etDescripcion)
            val tituloEdit = textTile.text.toString()
            val descripEdit = textDescrip.text.toString()
            Log.e("Titulo",tituloEdit)
            Log.e("descripEdit",descripEdit)
            Log.e("Id del pais",countryId.toString())
            Log.e("Id del post",idpost.toString())
            val apiInterface = RetrofitInstance.instance
            val call: Call<ApiRespone> = apiInterface.getApiResponseEditPost(tituloEdit,descripEdit,countryId,idpost)
            call.enqueue(object : Callback<ApiRespone> {
                override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                    if (response.isSuccessful) {
                        val apiResponse: ApiRespone? = response.body()
                        if (apiResponse != null) {
                            Log.e("Editar Post:", apiResponse.resultado)
                            image?.let { it1 -> Log.e("Imagen 1: ", it1) }
                            image2?.let { it1 -> Log.e("Imagen 2: ", it1) }
                            image3?.let { it1 -> Log.e("Imagen 3: ", it1) }
                            val apiInterface = RetrofitInstance.instance
                            val call: Call<ApiRespone> = apiInterface.getApiResponseEditPostMultimedia(image,image2,image3,idpost)
                            call.enqueue(object : Callback<ApiRespone> {
                                override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                                    if (response.isSuccessful) {
                                        val apiResponse: ApiRespone? = response.body()
                                        if (apiResponse != null) {
                                            Log.e("Multimedia:", apiResponse.resultado)
                                            returnToMain()
                                        }
                                    } else {
                                        Log.e("Cosas Error: ", "No jaló")
                                        returnToMain()
                                    }
                                }
                                override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                                    Log.e("Error en la solicitud:", t.message.toString())
                                    returnToMain()
                                }
                            })
                        }
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                        returnToMain()
                    }
                }
                override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                    returnToMain()
                }
            })
        }

        val btnedit: Button = findViewById(R.id.btnPost)

        btnedit.setOnClickListener{
            val textTile = findViewById<EditText>(R.id.etTitulo)
            val textDescrip = findViewById<EditText>(R.id.etDescripcion)
            val tituloEdit = textTile.text.toString()
            val descripEdit = textDescrip.text.toString()
            Log.e("Titulo",tituloEdit)
            Log.e("descripEdit",descripEdit)
            Log.e("Id del pais",countryId.toString())
            Log.e("Id del post",idpost.toString())
            var titleBool:Boolean = false
            var descrposBool:Boolean = false
            var imagen1:Boolean = false
            var imagen2:Boolean = false
            var countryBool:Boolean = false

            if (tituloEdit.isNotEmpty() && descripEdit.isNotEmpty()) {
                titleBool = true
                descrposBool = true
                if (image != null && image2 != null) {

                    imagen1 = true
                    imagen2 = true
                    if(countryId != null){
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
            if(titleBool === true && descrposBool === true && imagen1 === true && imagen2 === true && countryBool === true){
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiEditDraftToSend(tituloEdit,descripEdit,countryId,idpost)
                call.enqueue(object : Callback<ApiRespone> {
                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiRespone? = response.body()
                            if (apiResponse != null) {
                                Log.e("Editar Post:", apiResponse.resultado)
                                image?.let { it1 -> Log.e("Imagen 1: ", it1) }
                                image2?.let { it1 -> Log.e("Imagen 2: ", it1) }
                                image3?.let { it1 -> Log.e("Imagen 3: ", it1) }
                                val apiInterface = RetrofitInstance.instance
                                val call: Call<ApiRespone> = apiInterface.getApiEditImagesDraftToSend(image,image2,image3,idpost)
                                call.enqueue(object : Callback<ApiRespone> {
                                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                                        if (response.isSuccessful) {
                                            val apiResponse: ApiRespone? = response.body()
                                            if (apiResponse != null) {
                                                Log.e("Multimedia:", apiResponse.resultado)
                                                returnToMain()
                                            }
                                        } else {
                                            Log.e("Cosas Error: ", "No jaló")
                                            returnToMain()
                                        }
                                    }
                                    override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                                        Log.e("Error en la solicitud:", t.message.toString())
                                        returnToMain()
                                    }
                                })
                            }
                        } else {
                            Log.e("Cosas Error: ", "No jaló")
                            returnToMain()
                        }
                    }
                    override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                        Log.e("Error en la solicitud:", t.message.toString())
                        returnToMain()
                    }
                })
            }else{
                Toast.makeText(this, "Para enviar el post tienes que llenar todos los datos", Toast.LENGTH_SHORT).show()
            }

        }

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
                    2->{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("opcionMain", 3)
                        startActivity(intent)
                    }
                }
            }
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


    fun returnToMain(){
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


}