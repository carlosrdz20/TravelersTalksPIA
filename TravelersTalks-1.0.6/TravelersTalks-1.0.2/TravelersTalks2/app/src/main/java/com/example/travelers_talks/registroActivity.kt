package com.example.travelers_talks

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.travelers_talks.data.DataDbHelper
import com.example.travelers_talks.data.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.InputStream
import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.travelers_talks.data.UserSingleton
import java.util.Base64

class registroActivity : AppCompatActivity(), View.OnClickListener {

        private val PICK_IMAGE_REQUEST = 1
        private val CAMERA_CODE = 1002
        private val REQUEST_CAMERA_PERMISSION = 1002
        private var imageByteArray: ByteArray? = null
        private lateinit var dbHelper: DataDbHelper




        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        var linkLogin = findViewById<TextView>(R.id.linkALogin)
        linkLogin.setOnClickListener(this)

        var btnRegistro = findViewById<TextView>(R.id.btnSubmitRegistro)
        btnRegistro.setOnClickListener(this)

        var bttnOpenGalery = findViewById<ImageView>(R.id.imgPicture)
        bttnOpenGalery.setOnClickListener {
            openGallery()
        }

        var bttnOpenCamera = findViewById<ImageView>(R.id.imgCamera)

            bttnOpenCamera.setOnClickListener {
            tomarFoto()
        }


            var labelUsername = findViewById<TextView>(R.id.editNickname)

            var labelName = findViewById<TextView>(R.id.editName)

            var labelLastName = findViewById<TextView>(R.id.editLastName)

            var labelEmail = findViewById<TextView>(R.id.editEmail)

            var labelPassword = findViewById<TextView>(R.id.editPassword)

    }

    override fun onClick(p0: View?) {

        var userFun:Users = Users()
        var registerUser = false
        var nicknameBool = false
        var nameBool = false
        var lastnameBool = false
        var emailBool = false
        var passwordBool = false
        var imgBool = false

        if(p0!!.id == R.id.linkALogin){
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent);
        }

        val editNickname = findViewById<EditText>(R.id.editNickname)
        val editName = findViewById<EditText>(R.id.editName)
        val editLastName = findViewById<EditText>(R.id.editLastName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)

        val nickname = editNickname.text.toString()
        val name = editName.text.toString()
        val lastName = editLastName.text.toString()
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        userFun.Nickname = nickname
        userFun.Name = name
        userFun.LastName = lastName
        userFun.Email = email
        userFun.Password = password
        userFun.ImageURL = imageByteArray

        if(p0!!.id == R.id.btnSubmitRegistro){
            if(!nickname.isEmpty()){
                if(nickname.length >= 3 && nickname.matches(Regex("[a-zA-Z0-9]+"))){
                    nicknameBool = true
                }else{
                    Toast.makeText(this, "Nickname invalido", Toast.LENGTH_SHORT).show()
                    nicknameBool = false
                }
            }else{
                Toast.makeText(this, "Falta llenar el Nickname", Toast.LENGTH_SHORT).show()
                nicknameBool = false
            }
            if(!name.isEmpty()){
                if (name.matches(Regex("^[a-zA-Z]+( [a-zA-Z]+)*$"))) {
                    nameBool = true
                } else {
                    Toast.makeText(this, "El nombre debe contener solo letras y un espacio", Toast.LENGTH_SHORT).show()
                    nameBool = false
                }
            }else{
                Toast.makeText(this, "Falta llenar el Nombre", Toast.LENGTH_SHORT).show()
                nameBool = false
            }
            if(!lastName.isEmpty()){
                if (lastName.matches(Regex("^[a-zA-Z]+( [a-zA-Z]+)*$"))) {
                    lastnameBool = true
                } else {
                    Toast.makeText(this, "El nombre debe contener solo letras y un espacio", Toast.LENGTH_SHORT).show()
                    lastnameBool = false
                }
            }else{
                Toast.makeText(this, "Falta llenar el Apellido", Toast.LENGTH_SHORT).show()
                lastnameBool = false
            }
            if(!email.isEmpty()){
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailBool = true
                } else {
                    Toast.makeText(this, "Correo Invalido", Toast.LENGTH_SHORT).show()
                    emailBool = false
                }
            }else{
                Toast.makeText(this, "Falta llenar el Correo", Toast.LENGTH_SHORT).show()
                emailBool = false
            }
            if(!password.isEmpty()){
                if(validarPassword(password)){
                    passwordBool = true
                }else{
                    Toast.makeText(this, "La contraseña debe de ser de 8 caracteres", Toast.LENGTH_SHORT).show()
                    passwordBool = false
                }
            }else{
                Toast.makeText(this, "Falta llenar la contraseña", Toast.LENGTH_SHORT).show()
                passwordBool = false
            }
            if(imageByteArray?.isNotEmpty() == true){
                imgBool = true
            }else{
                Toast.makeText(this, "Por favor selecciona una imagen", Toast.LENGTH_SHORT).show()
                imgBool = false
            }
            if(nicknameBool === true && nameBool === true && lastnameBool === true && emailBool === true && emailBool === true && passwordBool === true && imgBool === true){
                registerUser = true
            }else{
                registerUser = false
            }
            if(registerUser === true){
                val dbHelper = DataDbHelper(this)
                var progressHome = findViewById<ProgressBar>(R.id.progressBarRegister)
                progressHome.visibility = View.VISIBLE

                registerUser = false
                val encodedString:String =  java.util.Base64.getEncoder().encodeToString(userFun.ImageURL)

                val strEncodeImage:String = "data:image/png;base64," + encodedString
                Log.e("Alo:",strEncodeImage)
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiResponseInsert(userFun.Nickname!!,
                    userFun.Name!!, userFun.LastName!!, userFun.Email!!, userFun.Password!!, strEncodeImage)

                call.enqueue(object : Callback<ApiRespone> {
                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                        if (response.isSuccessful) {
                            val apiResponse: ApiRespone? = response.body()
                            Log.e("Exito", "Que pro en 4K")
                            val apiInterface = RetrofitInstance.instance
                            val call: Call<UsersResponse> = apiInterface.getApiResponseUnique(userFun.Email!!,userFun.Password!!)

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

                                        progressHome.visibility = View.GONE
                                        loginRegister()
                                    } else {
                                        Log.e("Cosas Error: ", "No jaló")

                                    }
                                }
                                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
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
                Toast.makeText(this, "No se puede registrar el usuario", Toast.LENGTH_SHORT).show()
            }


        }
    }


    private fun loginRegister(){
        if (UserSingleton.currentUserNickname!!.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("opcionMain", 0)
            startActivity(intent);
        } else {
            Toast.makeText(this, "Credenciales no válidas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun tomarFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_CODE)
    }

    fun validarPassword(password: String): Boolean {
        // Al menos una minúscula, una mayúscula y un número
        val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")

        return pattern.matches(password)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data ?: return // Verificar si la Uri es nula


            val imageView = findViewById<ImageView>(R.id.imagenAvatar)
            imageView.setImageURI(selectedImageUri)


            val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri)
            val buffer = ByteArrayOutputStream()

            inputStream?.use { input ->
                input.copyTo(buffer)
            }
            imageByteArray = buffer.toByteArray()

            val localImageByteArray = imageByteArray
            if (localImageByteArray != null) {
                Log.d("ImageByteArray", "Size: ${localImageByteArray.size}")
            }

        }else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null){
            val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap

            if (imageBitmap != null) {

                val imageView = findViewById<ImageView>(R.id.imagenAvatar)
                imageView.setImageBitmap(imageBitmap)

                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes: ByteArray = stream.toByteArray()

                imageByteArray = bytes
            }

        }
    }


}