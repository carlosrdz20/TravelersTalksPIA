package com.example.travelers_talks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelers_talks.adapter.MyFavoritesAdapter
import com.example.travelers_talks.adapter.MyPostsAdapter
import com.example.travelers_talks.data.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_myfavorites.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_myfavorites : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
        return inflater.inflate(R.layout.fragment_myfavorites, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_myfavorites.
         */
        const val REQUEST_CODE_DELETE_POST = 1001
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_myfavorites().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinnerLoad = view.findViewById<ProgressBar>(R.id.progressBarMisBorradores)
        obtenerPost(spinnerLoad)
    }



    fun obtenerPost(progressBar: ProgressBar?){
        val apiInterface = RetrofitInstance.instance
        val call: Call<List<ApiResponseFavorites>> = apiInterface.getApiMyFavorites(UserSingleton.currentUserId)
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }
        call.enqueue(object : Callback<List<ApiResponseFavorites>> {
            override fun onResponse(call: Call<List<ApiResponseFavorites>>, response: Response<List<ApiResponseFavorites>>) {
                if (response.isSuccessful) {
                    MyFavoritesProvider.myFavoritesList.clear()
                    val apiResponse: List<ApiResponseFavorites>? = response.body()
                    val newByteArrayList: MutableList<ByteArray> = mutableListOf()
                    apiResponse?.forEach { post ->
                        Log.e("Aqui empieza el Post", "Aqui")
                        Log.e("Post Id: ", post.PostId.toString())
                        post.titlePost?.let { Log.e("Titulo Post: ", it) }
                        Log.e("Id Country: ", post.CountryId.toString())
                        Log.e("Id User: ", post.IdUsuarioPost.toString())
                        post.usernickname?.let { Log.e("Nickname: ", it) }
                        post.imageUser?.let { Log.e("ImageUser: ", it) }
                        post.countryImg?.let { Log.e("Country Img: ", it) }
                        val base64WithoutPrefixImageUser = post.imageUser?.removePrefix("data:image/png;base64,")
                        val base64WithoutPrefixCountryImage = post.countryImg?.removePrefix("data:image/png;base64,")
                        if (base64WithoutPrefixImageUser != null) {
                            Log.e("Image User Base64:", base64WithoutPrefixImageUser)
                        }
                        if (base64WithoutPrefixCountryImage != null) {
                            Log.e("Country Image Base64:", base64WithoutPrefixCountryImage)
                        }
                        post.imageUno?.let {
                            val base64WithoutPrefixImagenUno = post.imageUno.removePrefix("data:image/png;base64,")
                            newByteArrayList.add(java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenUno))
                        }
                        post.imageDos?.let {
                            val base64WithoutPrefixImagenDos = post.imageDos.removePrefix("data:image/png;base64,")
                            newByteArrayList.add(java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenDos)) }
                        post.imageTres?.let {
                            val base64WithoutPrefixImagenTres = post.imageTres.removePrefix("data:image/png;base64,")
                            newByteArrayList.add(java.util.Base64.getDecoder().decode(base64WithoutPrefixImagenTres)) }
                        val mypostA = MyFavorites(
                            post.RowDate,
                            "https://cdn-icons-png.flaticon.com/512/786/786352.png",
                        "https://cdn-icons-png.flaticon.com/512/1214/1214594.png",
                        post.PostId,
                        post.titlePost,
                        post.descPost,
                        post.IdUsuarioPost,
                        post.usernickname,
                        post.imageUser,
                        post.CountryId,
                        post.countryImg,
                        post.RowDate,
                        post.Rating,
                        post.imageUno,
                        post.imageDos,
                        post.imageTres
                        )
                        MyFavoritesProvider.myFavoritesList += mypostA
                        newByteArrayList.clear()
                    }
                    val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerViewMisPublicaciones)
                    if (recyclerView != null) {
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                    if (recyclerView != null) {
                        recyclerView.adapter = MyFavoritesAdapter(MyFavoritesProvider.myFavoritesList, this@fragment_myfavorites)
                    }
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                } else {
                    Log.e("Cosas Error: ", "No jaló")
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<List<ApiResponseFavorites>>, t: Throwable) {
                Log.e("Error en la solicitud:", t.message.toString())
                if (progressBar != null) {
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == fragment_myposts.REQUEST_CODE_DELETE_POST && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra("position", -1)
            if (position != -1) {
                val spinnerLoad = view?.findViewById<ProgressBar>(R.id.progressBarMisPublis)
                obtenerPost(spinnerLoad)
            }

        }
    }

}