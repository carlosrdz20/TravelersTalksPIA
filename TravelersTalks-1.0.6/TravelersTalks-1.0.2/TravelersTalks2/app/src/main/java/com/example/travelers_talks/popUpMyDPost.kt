package com.example.travelers_talks

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class popUpMyDPost : DialogFragment() {

    var counterimage = 1
    var image: String?=null
    var image2: String?=null
    var image3: String?=null
    var hayImagen = 0

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): popUpMyDPost {
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            val fragment = popUpMyDPost()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up_my_d_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(popUpMyDPost.ARG_POSITION, -1) ?: -1
        val btnYes: Button = view.findViewById(R.id.btnYes)
        val btnNo: Button = view.findViewById(R.id.btnNo)
        btnYes.setOnClickListener {
            MyDraftsProvider.myDraftsList[position].PostImg?.forEach { elemento->

                if(counterimage <= 3){

                    when (counterimage) {
                        1 ->{
                            hayImagen++
                            image = java.util.Base64.getEncoder().encodeToString(elemento)
                        }
                        2 ->{
                            hayImagen++
                            image2 = java.util.Base64.getEncoder().encodeToString(elemento)
                        }
                        3 ->{
                            hayImagen++
                            image3 = java.util.Base64.getEncoder().encodeToString(elemento)
                        }
                    }
                    counterimage++
                }


            }
            if(MyDraftsProvider.myDraftsList[position].idPost != null && MyDraftsProvider.myDraftsList[position].Title != null && MyDraftsProvider.myDraftsList[position].description != null && MyDraftsProvider.myDraftsList[position].Title != "" && MyDraftsProvider.myDraftsList[position].description != "" && MyDraftsProvider.myDraftsList[position].idCountry != null && hayImagen > 0){
                val apiInterface = RetrofitInstance.instance
                val call: Call<ApiRespone> = apiInterface.getApiEditDraftToSend(MyDraftsProvider.myDraftsList[position].Title,MyDraftsProvider.myDraftsList[position].description,MyDraftsProvider.myDraftsList[position].idCountry,MyDraftsProvider.myDraftsList[position].idPost)
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
                                val call: Call<ApiRespone> = apiInterface.getApiEditImagesDraftToSend(image,image2,image3,MyDraftsProvider.myDraftsList[position].idPost)
                                call.enqueue(object : Callback<ApiRespone> {
                                    override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                                        if (response.isSuccessful) {
                                            val apiResponse: ApiRespone? = response.body()
                                            if (apiResponse != null) {
                                                Log.e("Multimedia:", apiResponse.resultado)
                                                hayImagen = 0
                                                targetFragment?.onActivityResult(
                                                    targetRequestCode,
                                                    Activity.RESULT_OK,
                                                    Intent().putExtra("position", position)
                                                )
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
                        } else {
                            Log.e("Cosas Error: ", "No jaló")
                        }
                    }
                    override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                        Log.e("Error en la solicitud:", t.message.toString())
                    }
                })

                dismiss()
            }else{
                dismiss()
            }

        }

        btnNo.setOnClickListener {

            dismiss()
        }
    }
}