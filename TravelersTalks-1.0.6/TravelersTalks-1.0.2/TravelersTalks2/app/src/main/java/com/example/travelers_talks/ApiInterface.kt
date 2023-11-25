package com.example.travelers_talks
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {
    //Login
    @FormUrlEncoded
    @POST("SignIn.php")
    fun getApiResponseUnique(@Field("email") email: String, @Field("password") password: String): Call<UsersResponse>

    // Registro de Usuarios
    @FormUrlEncoded
    @POST("SignUp.php")
    fun getApiResponseInsert(@Field("nickname") nickname: String, @Field("nameuser") nameuser: String, @Field("lastname") lastname:String, @Field("email") email:String,@Field("pwduser") pwduser:String,@Field("imguser") imguser:String ): Call<ApiRespone>

    // Registro de Usuarios
    @FormUrlEncoded
    @POST("EditUsers.php")
    fun getApiEditResponse(@Field("idUser")idUser:Int,@Field("nickname") nickname: String, @Field("nameuser") nameuser: String, @Field("lastname") lastname:String, @Field("email") email:String,@Field("pwduser") pwduser:String,@Field("imguser") imguser:String ): Call<ApiRespone>

    // Select de Países
    @POST("CountryQuery.php")
    fun getApiCountries(): Call<List<ApiResponseCountries>>

    //Insertar Post
    @FormUrlEncoded
    @POST("InsertPost.php")
    fun getApiPostsInsert(@Field("title") titlePost:String?=null, @Field("descrip") descrip: String?=null, @Field("iduser") iduser: Int?, @Field("idcountry") idcountry:Int?=null, @Field("active") active:Int): Call<ApiResponsePostId>

    //Insertar Multimedia
    @FormUrlEncoded
    @POST("InsertPostMultimedia.php")
    fun getApiPostsInsertMultimedia(@Field("url")url:String?=null,@Field("idpost") idpost: Int,@Field("urldos")urldos:String?=null,@Field("urltres")urltres:String?=null): Call<ApiRespone>

    //Insertar Multimedia
    @FormUrlEncoded
    @POST("InsertCountry.php")
    fun getApiResponseInsertCountry(@Field("countryName")countryName:String,@Field("countryImg") countryImg: String): Call<ApiRespone>

    //Obtener Posts
    @FormUrlEncoded
    @POST("GetPosts.php")
    fun getApiPosts(@Field("idusuario")idusuario:Int?=null): Call<List<ApiResponsePosts>>

    //Obtener Posts
    @FormUrlEncoded
    @POST("GetPostsMultimedia.php")
    fun getApiIPostMultimedia(@Field("postid")postid:Int): Call<List<ApiResponseMultimedia>>

    //Obtener Mis Posts
    @FormUrlEncoded
    @POST("GetMyPosts.php")
    fun getApiMyPosts(@Field("iduser")iduser:Int?=null): Call<List<ApiResponsePosts>>

    //Editar Post
    @FormUrlEncoded
    @POST("UpdatePosts.php")
    fun getApiResponseEditPost(@Field("title") title:String?=null, @Field("descrip") description: String?=null, @Field("idcountry") idcountry: Int?=null, @Field("idpost") idpost: Int?=null): Call<ApiRespone>

    //Editar Post Multimedia
    @FormUrlEncoded
    @POST("UpdatePostMultimedia.php")
    fun getApiResponseEditPostMultimedia(@Field("imguno") imguno:String?=null, @Field("imgdos") imgdos: String?=null, @Field("imgtres") imgtres: String?=null, @Field("idpost") idpost: Int?): Call<ApiRespone>

    //Borrar Post
    @FormUrlEncoded
    @POST("DeletePost.php")
    fun getApiResponseDeletePost(@Field("idpost") idpost: Int?): Call<ApiRespone>

    //Obtener Mis Borradores
    @FormUrlEncoded
    @POST("GetMyDrafts.php")
    fun getApiResponseDrafts(@Field("iduser") idpost: Int?=null): Call<List<ApiResponsePosts>>

    //Insertar Rating / Update
    @FormUrlEncoded
    @POST("InsertRating.php")
    fun getApiResponseInsertRating(@Field("userid") userid: Int?=null,@Field("postid") postid: Int?=null,@Field("rate") rate: Int?=null): Call<ApiRespone>

    //Insertar Follow / Update
    @FormUrlEncoded
    @POST("InsertFollowers.php")
    fun getApiResponseInsertFollow(@Field("follower") follower: Int?=null, @Field("following") following: Int?=null): Call<ApiRespone>

    //Insertar Favoritos / Update
    @FormUrlEncoded
    @POST("InsertSave.php")
    fun getApiResponseInsertSave(@Field("iduser") iduser: Int?=null,@Field("postid") postid: Int?=null): Call<ApiRespone>

    //Busqueda Rating
    @FormUrlEncoded
    @POST("GetUserRating.php")
    fun getApiResponseRating(@Field("iduser") iduser: Int?=null,@Field("postid") postid: Int?=null): Call<ApiResponseRating>

    //Busqueda de Favoritos
    @FormUrlEncoded
    @POST("GetFavoritesPosts.php")
    fun getApiMyFavorites(@Field("iduser") iduser: Int?=null): Call<List<ApiResponseFavorites>>

    //Insertar Follower
    @FormUrlEncoded
    @POST("InsertFollowers.php")
    fun getApiInsertFollowers(@Field("follower") follower: Int?=null,@Field("following") following: Int?=null): Call<ApiRespone>

    //User Foraneo
    @FormUrlEncoded
    @POST("GetUserForeign.php")
    fun getApiResponseGetForeign(@Field("userforeign") follower: Int?=null,@Field("iduser") iduser: Int?=null): Call<ApiResponseUserForeign>

    //Posts Foraneo
    @FormUrlEncoded
    @POST("GetPostsForeignUser.php")
    fun getApiForeignPosts(@Field("foreignUser") foreignUser: Int?=null,@Field("idUser") idUser: Int?=null): Call<List<ApiReponseForeignPosts>>

    //Busqueda
    @FormUrlEncoded
    @POST("getBusq.php")
    fun getApiBusquedaPosts(@Field("txtBusq") txtBusq: String?=null, @Field("countryId") countryId: Int?=null, @Field("rating") rating: Int?=null, @Field("orderF") orderF: Int?=null,@Field("iduser") iduser: Int?=null): Call<List<ApiResponsePosts>>

    //Following Posts
    @FormUrlEncoded
    @POST("getPostsFollowing.php")
    fun getApiFollowingPosts(@Field("userfollower") userfollower: Int?=null): Call<List<ApiResponsePosts>>

    //Editar Borrador
    @FormUrlEncoded
    @POST("updateDraft.php")
    fun getApiEditDraftToSend(@Field("titleD") title:String?=null, @Field("descripD") description: String?=null, @Field("idcountryD") idcountry: Int?=null, @Field("idpostD") idpost: Int?=null): Call<ApiRespone>

    //Editar Borrar
    @FormUrlEncoded
    @POST("updateDraftMultimedia.php")
    fun getApiEditImagesDraftToSend(@Field("imgunoD") imguno:String?=null, @Field("imgdosD") imgdos: String?=null, @Field("imgtresD") imgtres: String?=null, @Field("idpostD") idpost: Int?=null): Call<ApiRespone>

    //Insert Full Offline
    @FormUrlEncoded
    @POST("insertFull.php")
    fun getApiInsertOffline(@Field("title") title:String?=null, @Field("descrip") descrip: String?=null, @Field("iduser") iduser: Int?=null, @Field("idcountry") idcountry: Int?=null, @Field("imgurl") imgurl: String?=null, @Field("imgurldos") imgurldos: String?=null, @Field("imgurltres") imgurltres: String?=null, @Field("active") active: Int?=null): Call<ApiRespone>



}