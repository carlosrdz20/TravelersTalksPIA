package com.example.travelers_talks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.travelers_talks.data.DataDbHelper
import com.example.travelers_talks.data.UserSingleton
import com.example.travelers_talks.data.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class popUpSeguro : DialogFragment() {

    private lateinit var dbHelper: DataDbHelper

    var onUserUpdateListener: OnUserUpdateListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbHelper = DataDbHelper(requireContext())
        return inflater.inflate(R.layout.fragment_pop_up_seguro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        var userUpdate: Users = Users()
        var registerUser = false
        var nicknameBool = false
        var nameBool = false
        var lastnameBool = false
        var emailBool = false
        var passwordBool = false
        var imgBool = false

        val name = arguments?.getString("name", "")
        val lastName = arguments?.getString("lastName", "")
        val email = arguments?.getString("email","")
        val password = arguments?.getString("password","")
        val nickname = arguments?.getString("nickname","")
        val img = arguments?.getByteArray("img") ?: byteArrayOf()

        userUpdate.Name = name.toString()
        userUpdate.LastName = lastName.toString()
        userUpdate.Email = email.toString()
        userUpdate.Password = password.toString()
        userUpdate.Nickname = nickname.toString()
        userUpdate.ImageURL = img
        userUpdate.idUser = UserSingleton.currentUserId


        val btnYes: Button = view.findViewById(R.id.btnYes)
        btnYes.setOnClickListener {

            if(!userUpdate.Nickname!!.isEmpty()){
                if(userUpdate.Nickname!!.length >= 3 && userUpdate.Nickname!!.matches(Regex("[a-zA-Z0-9]+"))){
                    nicknameBool = true
                }else{
                    Toast.makeText(requireContext(), "Nickname invalido", Toast.LENGTH_SHORT).show()
                    nicknameBool = false
                }
            }else{
                Toast.makeText(requireContext(), "Falta llenar el Nickname", Toast.LENGTH_SHORT).show()
                nicknameBool = false
            }
            if(!userUpdate.Name!!.isEmpty()){
                if (userUpdate.Name!!.matches(Regex("^[a-zA-Z]+( [a-zA-Z]+)*$"))) {
                    nameBool = true
                } else {
                    Toast.makeText(requireContext(), "El nombre debe contener solo letras y un espacio", Toast.LENGTH_SHORT).show()
                    nameBool = false
                }
            }else{
                Toast.makeText(requireContext(), "Falta llenar el Nombre", Toast.LENGTH_SHORT).show()
                nameBool = false
            }
            if(!userUpdate.LastName!!.isEmpty()){
                if ( userUpdate.LastName!!.matches(Regex("^[a-zA-Z]+( [a-zA-Z]+)*$"))) {
                    lastnameBool = true
                } else {
                    Toast.makeText(requireContext(), "El nombre debe contener solo letras y un espacio", Toast.LENGTH_SHORT).show()
                    lastnameBool = false
                }
            }else{
                Toast.makeText(requireContext(), "Falta llenar el Apellido", Toast.LENGTH_SHORT).show()
                lastnameBool = false
            }
            if(!userUpdate.Email!!.isEmpty()){
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(userUpdate.Email).matches()) {
                    emailBool = true
                } else {
                    Toast.makeText(requireContext(), "Correo Invalido", Toast.LENGTH_SHORT).show()
                    emailBool = false
                }
            }else{
                Toast.makeText(requireContext(), "Falta llenar el Correo", Toast.LENGTH_SHORT).show()
                emailBool = false
            }
            if(!userUpdate.Password!!.isEmpty()){
                passwordBool = true
            }else{
                Toast.makeText(requireContext(), "Falta llenar la contraseña", Toast.LENGTH_SHORT).show()
                passwordBool = false
            }
            if(userUpdate.ImageURL?.isNotEmpty() == true){
                imgBool = true
            }else{
                Toast.makeText(requireContext(), "Por favor selecciona una imagen", Toast.LENGTH_SHORT).show()
                imgBool = false
            }
            if(nicknameBool === true && nameBool === true && lastnameBool === true && emailBool === true && emailBool === true && passwordBool === true && imgBool === true){
                registerUser = true
            }else{
                registerUser = false
            }
            if(registerUser === true) {
                if(isInternetAvailable()){
                    val encodedString:String =  java.util.Base64.getEncoder().encodeToString(userUpdate.ImageURL)
                    val strEncodeImage:String = "data:image/png;base64," + encodedString
                    Log.e("Alo:",strEncodeImage)
                    val apiInterface = RetrofitInstance.instance
                    val call: Call<ApiRespone> = apiInterface.getApiEditResponse(
                        userUpdate.idUser!!,userUpdate.Nickname!!,
                        userUpdate.Name!!, userUpdate.LastName!!, userUpdate.Email!!, userUpdate.Password!!, strEncodeImage)

                    call.enqueue(object : Callback<ApiRespone> {
                        override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                            if (response.isSuccessful) {
                                val apiResponse: ApiRespone? = response.body()
                                Log.e("Exito", "Que pro en 4K")
                                UserSingleton.currentUserNickname = userUpdate.Nickname
                                UserSingleton.currentUserName = userUpdate.Name
                                UserSingleton.currentUserEmail = userUpdate.Email
                                UserSingleton.currentUserPassword = userUpdate.Password
                                UserSingleton.currentUserLastName = userUpdate.LastName
                                UserSingleton.currentUserImg = encodedString

                                val encodedString:String =  java.util.Base64.getEncoder().encodeToString(userUpdate.ImageURL)
                                val strEncodeImage:String = "data:image/png;base64," + encodedString
                                val imageDataBytes = android.util.Base64.decode(strEncodeImage.substringAfter(','), android.util.Base64.DEFAULT)

                                var userio = Users(
                                    userUpdate.idUser!!,
                                    userUpdate.Nickname!!,
                                    userUpdate.Name!!,
                                    userUpdate.LastName!!,
                                    userUpdate.Email!!,
                                    userUpdate.Password!!,
                                    imageDataBytes
                                )
                                dbHelper.updateUser(userio)


                                onUserUpdateListener?.onUserUpdated()
                            } else {
                                Log.e("Cosas Error: ", "No jaló")
                            }
                        }
                        override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                            Log.e("Error en la solicitud:", t.message.toString())
                        }
                    })
                }else{
                    val encodedString:String =  java.util.Base64.getEncoder().encodeToString(userUpdate.ImageURL)
                    val strEncodeImage:String = "data:image/png;base64," + encodedString
                    val imageDataBytes = android.util.Base64.decode(strEncodeImage.substringAfter(','), android.util.Base64.DEFAULT)

                    var userio = Users(
                        userUpdate.idUser!!,
                        userUpdate.Nickname!!,
                        userUpdate.Name!!,
                        userUpdate.LastName!!,
                        userUpdate.Email!!,
                        userUpdate.Password!!,
                        imageDataBytes
                    )
                    dbHelper.updateUser(userio)
                }
                registerUser = false
            }
            dismiss()
        }

        val btnNo: Button = view.findViewById(R.id.btnNo)
        btnNo.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

    interface OnUserUpdateListener {
        fun onUserUpdated()
    }

}
