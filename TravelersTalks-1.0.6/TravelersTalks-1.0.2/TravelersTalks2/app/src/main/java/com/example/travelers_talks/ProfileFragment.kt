package com.example.travelers_talks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.travelers_talks.data.UserSingleton
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val PICK_IMAGE_REQUEST = 1
private val CAMERA_CODE = 1002
var byteArray: ByteArray? = null
/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), popUpSeguro.OnUserUpdateListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("WrongThread")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivPfp = view.findViewById<ImageView>(R.id.imgUserPic)

        val progessBar = view.findViewById<ProgressBar>(R.id.progressBarEditProfile)
        val tvEditName = view.findViewById<EditText>(R.id.etName)
        tvEditName.setText(UserSingleton.currentUserName)
        val tvEditLastName = view.findViewById<EditText>(R.id.etLastName)
        tvEditLastName.setText(UserSingleton.currentUserLastName)
        val tvEditPassword = view.findViewById<EditText>(R.id.etPwd)
        tvEditPassword.setText(UserSingleton.currentUserPassword)
        val tvEditNickname = view.findViewById<EditText>(R.id.etNickName)
        tvEditNickname.setText(UserSingleton.currentUserNickname)

        val imageDataBytes = java.util.Base64.getDecoder().decode(UserSingleton.currentUserImg!!.substringAfter(','))
        val decodedImageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.size)
        ivPfp.setImageBitmap(decodedImageBitmap)
        val draw = ivPfp.drawable

        if (draw is BitmapDrawable) {
            val bitmap = draw.bitmap
            // Convierte el Bitmap a un ByteArray
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            byteArray = stream.toByteArray()
        }

        val btnGuardar : Button = view.findViewById(R.id.btnSave)

        btnGuardar.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("name", tvEditName.text.toString())
            bundle.putString("lastName", tvEditLastName.text.toString())
            bundle.putString("email", UserSingleton.currentUserEmail)
            bundle.putString("password", tvEditPassword.text.toString())
            bundle.putString("nickname", tvEditNickname.text.toString())
            bundle.putByteArray("img",byteArray)
            val showPopUp = popUpSeguro()
            showPopUp.arguments = bundle
            showPopUp.onUserUpdateListener = activity as? popUpSeguro.OnUserUpdateListener
            showPopUp.show(parentFragmentManager, "showPopUp")
        }

        val btnCancelar : Button = view.findViewById(R.id.btnCancel)

        btnCancelar.setOnClickListener{
            val showPopUp = popUpSeguroCancel()
            showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
        }

        var bttnOpenGalery = view.findViewById<ImageView>(R.id.imgPicture)
        bttnOpenGalery.setOnClickListener {
            openGallery()
        }

        var bttnOpenCamera = view.findViewById<ImageView>(R.id.imgCamera)

        bttnOpenCamera.setOnClickListener {
            tomarFoto()
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data ?: return // Verificar si la Uri es nula


            val imageView = view?.findViewById<ImageView>(R.id.imgUserPic)
            if (imageView != null) {
                imageView.setImageURI(selectedImageUri)
            }


            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(selectedImageUri)
            val buffer = ByteArrayOutputStream()

            inputStream?.use { input ->
                input.copyTo(buffer)
            }
            byteArray = buffer.toByteArray()

            val localImageByteArray = byteArray
            if (localImageByteArray != null) {
                Log.d("ImageByteArray", "Size: ${localImageByteArray.size}")
            }

        }else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null){
            val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap

            if (imageBitmap != null) {

                val imageView = view?.findViewById<ImageView>(R.id.imgUserPic)
                if (imageView != null) {
                    imageView.setImageBitmap(imageBitmap)
                }

                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes: ByteArray = stream.toByteArray()

                byteArray = bytes
            }

        }
    }

    override fun onUserUpdated() {
        Log.e("Que", "Onda")
    }
}