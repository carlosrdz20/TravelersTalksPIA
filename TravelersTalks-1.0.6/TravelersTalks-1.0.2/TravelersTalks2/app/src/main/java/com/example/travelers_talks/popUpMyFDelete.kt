package com.example.travelers_talks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.travelers_talks.data.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class popUpMyFDelete : DialogFragment() {


    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): popUpMyFDelete {
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            val fragment = popUpMyFDelete()
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
        val position = arguments?.getInt(ARG_POSITION, -1) ?: -1
        val btnYes: Button = view.findViewById(R.id.btnYes)
        btnYes.setOnClickListener {
            val apiInterface = RetrofitInstance.instance
            val call: Call<ApiRespone> = apiInterface.getApiResponseInsertSave(UserSingleton.currentUserId,MyFavoritesProvider.myFavoritesList[position].PostId)
            call.enqueue(object : Callback<ApiRespone> {
                override fun onResponse(call: Call<ApiRespone>, response: Response<ApiRespone>) {
                    if (response.isSuccessful) {
                        val apiResponse: ApiRespone? = response.body()
                        targetFragment?.onActivityResult(
                            targetRequestCode,
                            Activity.RESULT_OK,
                            Intent().putExtra("position", position)
                        )
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }
                override fun onFailure(call: Call<ApiRespone>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })
            dismiss()
        }

        val btnNo: Button = view.findViewById(R.id.btnNo)
        btnNo.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
        }
    }
}