package com.example.travelers_talks

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelers_talks.adapter.PostsAdapter
import com.example.travelers_talks.data.CountryId
import com.example.travelers_talks.data.DataDbHelper
import com.example.travelers_talks.data.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() , DatePickerDialog.OnDateSetListener  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var day = 0
    private var month = 0
    private var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    var fechaInicial = "Initial Date"

    private var dayF = 0
    private var monthF = 0
    private var yearF = 0

    var savedDayF = 0
    var savedMonthF = 0
    var savedYearF = 0

    var fechaFinal = "Final Date"

    var myOption = 0

    val listCountries = mutableListOf<CountryId>()
    val fechasArray = listOf<OpcionesFecha>(
        OpcionesFecha("Más Recientes",1),
        OpcionesFecha("Más Antiguos",0)
    )
    var country: CountryId ?= null
    var countryId: Int ?=null;
    var ordenFechaText: String ?= null
    var ordenFechaId: Int ?= null


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

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic

        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }

            }
    }


    //pop up filtros
    @SuppressLint("WrongThread")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DataDbHelper(requireContext())
        val byteArrayList: MutableList<ByteArray> = mutableListOf()
        var progressHome = view.findViewById<ProgressBar>(R.id.progressBarPosts)
        if(isInternetAvailable()){
            Log.e("Hay", "Internet")
            val apiInterface = RetrofitInstance.instance
            val call: Call<List<ApiResponsePosts>> = apiInterface.getApiPosts(UserSingleton.currentUserId)
            call.enqueue(object : Callback<List<ApiResponsePosts>> {
                override fun onResponse(call: Call<List<ApiResponsePosts>>, response: Response<List<ApiResponsePosts>>) {
                    if (response.isSuccessful) {

                        progressHome.visibility = View.VISIBLE
                        PostsProvider.PostList.clear()
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

                            var stringSave:String?=null
                            var stringFollowing:String?=null

                            if(post.FollowingStatus == 1){
                                stringFollowing = "  Following"
                            }else{
                                stringFollowing =  " + FOLLOW"
                            }
                            if(post.SavedStatus == 1){
                                stringSave = "https://pixsector.com/cache/5f5275bf/av83bcb11eee42d2ca6cd.png"
                            }else{
                                stringSave = "https://static.thenounproject.com/png/4590951-200.png"
                            }

                            val postA = Posts(
                                post.UserNickname,
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixImageUser),
                                post.DatePost,
                                post.TituloPost,
                                post.Rating,
                                "https://www.pngall.com/wp-content/uploads/2017/05/Star-PNG-Image.png",
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixCountryImage),
                                stringSave,
                                newByteArrayList.toList(),
                                stringFollowing,
                                post.PostId,
                                post.IdCount,
                                post.IdUser,
                                post.Description
                            )
                            PostsProvider.PostList += postA
                            newByteArrayList.clear()
                        }
                        PostsProvider.updateLocalPostList()
                        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPosts)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.adapter = PostsAdapter(PostsProvider.PostList)
                        progressHome.visibility = View.GONE
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }

                override fun onFailure(call: Call<List<ApiResponsePosts>>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })
        }else{
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPosts)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = PostsAdapter(PostsProvider.getLocalPostList())
            progressHome.visibility = View.GONE
        }


        Log.e("Sqlite", "Inicio")
        dbHelper.showToastWithUserDetails(requireContext())
        dbHelper.showToastCountry(requireContext())
        dbHelper.showToastMultimedia(requireContext())
        dbHelper.showToastPosts(requireContext())
        Log.e("Sqlite", "Final")

        val txtFilters : TextView = view.findViewById(R.id.txtFilters)
        val linearLayout: LinearLayout = view.findViewById(R.id.LinearLayoutFiltros)
        txtFilters.setOnClickListener{
            if (linearLayout.visibility == View.VISIBLE) {
                linearLayout.visibility = View.GONE
            } else {
                linearLayout.visibility = View.VISIBLE
            }
        }




        val actionColor = ContextCompat.getColor(txtFilters.context, R.color.TT_Action)
        val white = ContextCompat.getColor(txtFilters.context, R.color.white)

        val bttnCreatePub = view.findViewById<Button>(R.id.bttnCreatePub)
        val followingPosts = view.findViewById<TextView>(R.id.txtSiguiendo)
        val forYouPosts = view.findViewById<TextView>(R.id.txtParati)
        followingPosts.setOnClickListener {
            followingPosts.setTextColor(actionColor)
            forYouPosts.setTextColor(white)
            val apiInterface = RetrofitInstance.instance
            val call: Call<List<ApiResponsePosts>> = apiInterface.getApiFollowingPosts(UserSingleton.currentUserId)
            progressHome.visibility = View.VISIBLE
            call.enqueue(object : Callback<List<ApiResponsePosts>> {
                override fun onResponse(call: Call<List<ApiResponsePosts>>, response: Response<List<ApiResponsePosts>>) {
                    if (response.isSuccessful) {
                        PostsProvider.PostList.clear()
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

                            var stringSave:String?=null
                            if(post.SavedStatus == 1){
                                stringSave = "https://pixsector.com/cache/5f5275bf/av83bcb11eee42d2ca6cd.png"
                            }else{
                                stringSave = "https://static.thenounproject.com/png/4590951-200.png"
                            }


                            val postA = Posts(
                                post.UserNickname,
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixImageUser),
                                post.DatePost,
                                post.TituloPost,
                                post.Rating,
                                "https://www.pngall.com/wp-content/uploads/2017/05/Star-PNG-Image.png",
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixCountryImage),
                                stringSave!!,
                                newByteArrayList.toList(),
                                "Following",
                                post.PostId,
                                post.IdCount,
                                post.IdUser,
                                post.Description
                            )
                            PostsProvider.PostList += postA
                            newByteArrayList.clear()
                        }
                        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPosts)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.adapter = PostsAdapter(PostsProvider.PostList)
                        progressHome.visibility = View.GONE
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }

                override fun onFailure(call: Call<List<ApiResponsePosts>>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })
        }

        forYouPosts.setOnClickListener {
            followingPosts.setTextColor(white)
            forYouPosts.setTextColor(actionColor)
            val apiInterface = RetrofitInstance.instance
            val call: Call<List<ApiResponsePosts>> = apiInterface.getApiPosts(UserSingleton.currentUserId)
            progressHome.visibility = View.VISIBLE
            call.enqueue(object : Callback<List<ApiResponsePosts>> {
                override fun onResponse(call: Call<List<ApiResponsePosts>>, response: Response<List<ApiResponsePosts>>) {
                    if (response.isSuccessful) {
                        PostsProvider.PostList.clear()
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

                            var stringSave:String?=null
                            var stringFollowing:String?=null

                            if(post.FollowingStatus == 1){
                                stringFollowing = "  Following"
                            }else{
                                stringFollowing =  " + FOLLOW"
                            }
                            if(post.SavedStatus == 1){
                                stringSave = "https://pixsector.com/cache/5f5275bf/av83bcb11eee42d2ca6cd.png"
                            }else{
                                stringSave = "https://static.thenounproject.com/png/4590951-200.png"
                            }


                            val postA = Posts(
                                post.UserNickname,
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixImageUser),
                                post.DatePost,
                                post.TituloPost,
                                post.Rating,
                                "https://www.pngall.com/wp-content/uploads/2017/05/Star-PNG-Image.png",
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixCountryImage),
                                stringSave!!,
                                newByteArrayList.toList(),
                                stringFollowing!!,
                                post.PostId,
                                post.IdCount,
                                post.IdUser,
                                post.Description
                            )
                            PostsProvider.PostList += postA
                            newByteArrayList.clear()
                        }
                        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPosts)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.adapter = PostsAdapter(PostsProvider.PostList)
                        progressHome.visibility = View.GONE
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }

                override fun onFailure(call: Call<List<ApiResponsePosts>>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })
        }

        bttnCreatePub.setOnClickListener {

            val intent = Intent(requireContext(), createPosts::class.java)
            intent.putExtra("opcionMain", 0)
            startActivity(intent)

        }

        val apiInterface2 = RetrofitInstance.instance
        val call2: Call<List<ApiResponseCountries>> = apiInterface2.getApiCountries()
        call2.enqueue(object : Callback<List<ApiResponseCountries>> {
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
            override fun onFailure(call2: Call<List<ApiResponseCountries>>, t: Throwable) {
                Log.e("Error en la solicitud:", t.message.toString())
            }
        })

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listCountries)
        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.actvdropwon_field)
        autoCompleteTextView.setAdapter(adapter)

        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, fechasArray)
        val autoCompleteTextView2 = view.findViewById<AutoCompleteTextView>(R.id.actvdropwon_fieldFecha)
        autoCompleteTextView2.setAdapter(adapter2)

        Log.e("AutoComplete: ", autoCompleteTextView.text.toString())
        val ratingBar = view.findViewById<RatingBar>(R.id.ratBar)

        val rating = ratingBar.rating

        val ratingEntero = rating.toInt()
        Log.e("RatingBar",ratingEntero.toString())

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCountry = parent.adapter.getItem(position) as CountryId
            countryId = selectedCountry.id
            var countryName = selectedCountry.name

            Log.d("Country Info", "ID: $countryId, Name: $countryName")
        }

        autoCompleteTextView2.setOnItemClickListener { parent, view, position, id ->
            val selectedCountry = parent.adapter.getItem(position) as OpcionesFecha
            ordenFechaText = selectedCountry.texto
            ordenFechaId = selectedCountry.valor

            Log.d("Orden fechas", "ID: $ordenFechaId, Name: $ordenFechaText")
        }

        val ratingBarCheck = view.findViewById<RatingBar>(R.id.ratBar)

        val ivEliminarRating = view.findViewById<ImageView>(R.id.ivEliminarRating)
        ivEliminarRating.setOnClickListener {
            ratingBarCheck.rating = 0.0f
        }

        val busqueda: ImageView = view.findViewById<ImageView>(R.id.busqIV)

        busqueda.setOnClickListener {
            var countryIdPrueba: Int ?=null
            Log.e("AutoComplete: ", autoCompleteTextView.text.toString())
            if(autoCompleteTextView.text.toString() == ""){
                countryIdPrueba = -1
            }else{
                countryIdPrueba = countryId
            }
            val ratingBar = view.findViewById<RatingBar>(R.id.ratBar)
            val rating = ratingBar.rating
            val ratingEntero = rating.toInt()
            Log.e("RatingBar",ratingEntero.toString())
            val textBusqueda = view.findViewById<EditText>(R.id.busquedaTexto)
            val texto: String = textBusqueda.text.toString()
            if(ordenFechaId ==  null){
                ordenFechaId = 1;
            }
            val apiInterface = RetrofitInstance.instance
            val call: Call<List<ApiResponsePosts>> = apiInterface.getApiBusquedaPosts(texto,countryIdPrueba,ratingEntero,ordenFechaId,UserSingleton.currentUserId)
            call.enqueue(object : Callback<List<ApiResponsePosts>> {
                override fun onResponse(call: Call<List<ApiResponsePosts>>, response: Response<List<ApiResponsePosts>>) {
                    if (response.isSuccessful) {
                        PostsProvider.PostList.clear()
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


                            var stringSave:String?=null
                            var stringFollowing:String?=null

                            if(post.FollowingStatus == 1){
                                stringFollowing = "  Following"
                            }else{
                                stringFollowing =  " + FOLLOW"
                            }
                            if(post.SavedStatus == 1){
                                stringSave = "https://pixsector.com/cache/5f5275bf/av83bcb11eee42d2ca6cd.png"
                            }else{
                                stringSave = "https://static.thenounproject.com/png/4590951-200.png"
                            }



                            val postA = Posts(
                                post.UserNickname,
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixImageUser),
                                post.DatePost,
                                post.TituloPost,
                                post.Rating,
                                "https://www.pngall.com/wp-content/uploads/2017/05/Star-PNG-Image.png",
                                java.util.Base64.getDecoder().decode(base64WithoutPrefixCountryImage),
                                stringSave!!,
                                newByteArrayList.toList(),
                                stringFollowing!!,
                                post.PostId,
                                post.IdCount,
                                post.IdUser,
                                post.Description
                            )
                            PostsProvider.PostList += postA
                            newByteArrayList.clear()
                        }
                        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPosts)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.adapter = PostsAdapter(PostsProvider.PostList)
                    } else {
                        Log.e("Cosas Error: ", "No jaló")
                    }
                }

                override fun onFailure(call: Call<List<ApiResponsePosts>>, t: Throwable) {
                    Log.e("Error en la solicitud:", t.message.toString())
                }
            })
        }

    }

    override fun onDateSet(p0: DatePicker?, Pyear: Int, Pmonth: Int, Pday: Int) {
        if(myOption === 1){
            savedDay = Pday
            savedMonth = Pmonth + 1
            savedYear = Pyear
            val txtFechaInicial : TextView = requireView().findViewById(R.id.txtInitialDate)
            fechaInicial = "$savedDay - $savedMonth - $savedYear"
            if (txtFechaInicial != null) {
                txtFechaInicial.text = fechaInicial
            }
        }else{
            savedDayF = Pday
            savedMonthF = Pmonth + 1
            savedYearF = Pyear
            val txtFechaFinal : TextView = requireView().findViewById(R.id.txtFinalDate)
            fechaFinal = "$savedDayF - $savedMonthF - $savedYearF"
            txtFechaFinal.text = fechaFinal
        }
    }


    private fun getDateTimeCalendar(option : Int){
        myOption = option
        if(option === 1){
            val cal = Calendar.getInstance()
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year = cal.get(Calendar.YEAR)
        }else{
            val cal = Calendar.getInstance()
            dayF = cal.get(Calendar.DAY_OF_MONTH)
            monthF = cal.get(Calendar.MONTH)
            yearF = cal.get(Calendar.YEAR)
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





}