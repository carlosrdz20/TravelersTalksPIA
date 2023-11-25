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
import com.example.travelers_talks.adapter.MyPostsAdapter
import com.example.travelers_talks.data.DataDbHelper
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
 * Use the [fragment_myposts.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_myposts : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dbHelper: DataDbHelper

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
        return inflater.inflate(R.layout.fragment_myposts, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_myposts.
         */
        const val REQUEST_CODE_DELETE_POST = 1001
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_myposts().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DataDbHelper(requireContext())
        val postData = dbHelper.showToastPosts(requireContext())
        val spinnerLoad = view.findViewById<ProgressBar>(R.id.progressBarMisPublis)
        obtenerPost(spinnerLoad)


    }

     fun obtenerPost(progessBar: ProgressBar?){
         val apiInterface = RetrofitInstance.instance
         val call: Call<List<ApiResponsePosts>> = apiInterface.getApiMyPosts(UserSingleton.currentUserId)
         if (progessBar != null) {
             progessBar.visibility = View.VISIBLE
         }
         call.enqueue(object : Callback<List<ApiResponsePosts>> {
             override fun onResponse(call: Call<List<ApiResponsePosts>>, response: Response<List<ApiResponsePosts>>) {
                 if (response.isSuccessful) {
                     MyPostsProvider.myPostsList.clear()
                     val apiResponse: List<ApiResponsePosts>? = response.body()
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
                         val mypostA = MyPosts(
                             post.UserNickname,
                             java.util.Base64.getDecoder().decode(base64WithoutPrefixImageUser),
                             post.DatePost,
                             post.TituloPost,
                             0.0f,
                             "https://www.pngall.com/wp-content/uploads/2017/05/Star-PNG-Image.png",
                             java.util.Base64.getDecoder().decode(base64WithoutPrefixCountryImage),
                             "https://static.thenounproject.com/png/4590951-200.png",
                             newByteArrayList.toList(),
                             " + FOLLOW",
                             post.PostId,
                             post.IdCount,
                             post.IdUser,
                             post.Description,
                             "https://www.pngall.com/wp-content/uploads/5/Black-Play-Button-PNG-Free-Download.png",
                             "https://icons.veryicon.com/png/o/miscellaneous/convenient-svn-icon/pencil-60.png",
                             "https://cdn-icons-png.flaticon.com/512/1214/1214594.png",
                             post.CountryName
                         )
                         MyPostsProvider.myPostsList += mypostA
                         newByteArrayList.clear()
                     }
                     val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerViewMisPublicaciones)
                     if (recyclerView != null) {
                         recyclerView.layoutManager = LinearLayoutManager(requireContext())
                     }
                     if (recyclerView != null) {
                         recyclerView.adapter = MyPostsAdapter(MyPostsProvider.myPostsList,this@fragment_myposts)
                     }
                     if (progessBar != null) {
                         progessBar.visibility = View.GONE
                     }
                 } else {
                     Log.e("Cosas Error: ", "No jaló")
                     if (progessBar != null) {
                         progessBar.visibility = View.GONE
                     }
                 }
             }
             override fun onFailure(call: Call<List<ApiResponsePosts>>, t: Throwable) {
                 Log.e("Error en la solicitud:", t.message.toString())
                 if (progessBar != null) {
                     progessBar.visibility = View.GONE
                 }
             }
         })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_DELETE_POST && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra("position", -1)
            if (position != -1) {
                val spinnerLoad = view?.findViewById<ProgressBar>(R.id.progressBarMisPublis)
                obtenerPost(spinnerLoad)
            }

        }
    }

}