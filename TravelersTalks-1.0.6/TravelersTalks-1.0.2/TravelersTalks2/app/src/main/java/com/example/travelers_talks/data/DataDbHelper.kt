package com.example.travelers_talks.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.travelers_talks.Posts
import com.example.travelers_talks.PostsOffline
import com.example.travelers_talks.PostsOfflineList
import com.example.travelers_talks.PostsProvider
import com.example.travelers_talks.UserOffline
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class DataDbHelper (var context: Context): SQLiteOpenHelper(context, SetDB.DB_Name, null, SetDB.DB_Version){
    init {
        Log.e("DataDbHelper", "Instancia creada")
    }
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val createUsersTable: String = "CREATE TABLE " + SetDB.tblUsuarios.Table_Name + "(" +
                    SetDB.tblUsuarios.COL_IdUser + " INTEGER PRIMARY KEY, " +
                    SetDB.tblUsuarios.COL_Nickname + " VARCHAR(50), " +
                    SetDB.tblUsuarios.COL_Name + " VARCHAR(80), " +
                    SetDB.tblUsuarios.COL_LastName + " VARCHAR(80), " +
                    SetDB.tblUsuarios.COL_Email + " VARCHAR(100), " +
                    SetDB.tblUsuarios.COL_Password + " VARCHAR(25), " +
                    SetDB.tblUsuarios.COL_ImageURL + " LONGBLOB) "

            db?.execSQL(createUsersTable)

            val createCountriesTable: String = "CREATE TABLE " + SetDB.tblCountries.Table_Name + "(" +
                    SetDB.tblCountries.COL_IdCountry + " INTEGER PRIMARY KEY, " +
                    SetDB.tblCountries.COL_Name + " VARCHAR(100), " +
                    SetDB.tblCountries.COL_ImageURL + " LONGBLOB) "

            db?.execSQL(createCountriesTable)

            val createPostsTable: String = "CREATE TABLE " + SetDB.tblPosts.Table_Name + "(" +
                    SetDB.tblPosts.COL_IdPost + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SetDB.tblPosts.COL_Title + " VARCHAR(150), " +
                    SetDB.tblPosts.COL_Description + " VARCHAR(200), " +
                    SetDB.tblPosts.COL_IdUser + " INTEGER, " +
                    SetDB.tblPosts.COL_IdCountry + " INTEGER, " +
                    SetDB.tblPosts.COL_RowDate + " DATE DEFAULT CURRENT_TIMESTAMP, " +
                    SetDB.tblPosts.COL_Rate + " FLOAT, " +
                    SetDB.tblPosts.COL_WRate + " INTEGER, " +
                    SetDB.tblPosts.COL_ACTIVEP + " INTEGER) "

            db?.execSQL(createPostsTable)

            val createMultimediaTable: String = "CREATE TABLE " + SetDB.tblMultimedia.Table_Name + "(" +
                    SetDB.tblMultimedia.COL_IdMul + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SetDB.tblMultimedia.COL_ImgURL + " BLOB, " +
                    SetDB.tblMultimedia.COL_IdPost + " INTEGER, " +
                    SetDB.tblMultimedia.COL_ImgURL2 + " LONGBLOB, " +
                    SetDB.tblMultimedia.COL_ImgURL3 + " LONGBLOB) "

            db?.execSQL(createMultimediaTable)

            val createFollowingTable: String = "CREATE TABLE " + SetDB.tblFollowing.Table_Name + " (" +
                    SetDB.tblFollowing.COL_IdFollow + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SetDB.tblFollowing.COL_IdUserFollower + " INTEGER, " +
                    SetDB.tblFollowing.COL_IdUserFollowing + " INTEGER, " +
                    SetDB.tblFollowing.COL_ActiveF + " INTEGER) "

            db?.execSQL(createFollowingTable)

            val createRatingTable: String = "CREATE TABLE " + SetDB.tblRating.Table_Name + "(" +
                    SetDB.tblRating.COL_IdRating + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SetDB.tblRating.COL_IdPost + " INTEGER, " +
                    SetDB.tblRating.COL_IdUser + " INTEGER, " +
                    SetDB.tblRating.COL_Rate + " INTEGER) "

            db?.execSQL(createRatingTable)

            val createSaveTable: String = "CREATE TABLE " + SetDB.tblSave.Table_Name + "(" +
                    SetDB.tblSave.COL_IdSave + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SetDB.tblSave.COL_IdUser + " INTEGER, " +
                    SetDB.tblSave.COL_IdPost + " INTEGER, " +
                    SetDB.tblSave.COL_ActiveS + " INTEGER) "

            db?.execSQL(createSaveTable)

            Log.e("Entro", "Creo Tablas")
        }catch (e: Exception){
            Log.e("Exception", e.toString())
            Log.e("No Entro", "Joder no entro")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    public fun insertUser(user: Users):Boolean{
        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblUsuarios.COL_IdUser ,user.idUser)
        values.put(SetDB.tblUsuarios.COL_Nickname ,user.Nickname)
        values.put(SetDB.tblUsuarios.COL_Name ,user.Name)
        values.put(SetDB.tblUsuarios.COL_LastName ,user.LastName)
        values.put(SetDB.tblUsuarios.COL_Email ,user.Email)
        values.put(SetDB.tblUsuarios.COL_Password,user.Password)
        values.put(SetDB.tblUsuarios.COL_ImageURL,user.ImageURL)

        try {
            val result =  dataBase.insert(SetDB.tblUsuarios.Table_Name, null, values)
            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }


    fun updateUser(user: Users): Boolean {
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        values.put(SetDB.tblUsuarios.COL_Nickname, user.Nickname)
        values.put(SetDB.tblUsuarios.COL_Name, user.Name)
        values.put(SetDB.tblUsuarios.COL_LastName, user.LastName)
        values.put(SetDB.tblUsuarios.COL_Email, user.Email)
        values.put(SetDB.tblUsuarios.COL_Password, user.Password)
        values.put(SetDB.tblUsuarios.COL_ImageURL, user.ImageURL)

        val whereClause = "${SetDB.tblUsuarios.COL_IdUser} = ?"
        val whereArgs = arrayOf(user.idUser.toString())

        try {
            val result = dataBase.update(SetDB.tblUsuarios.Table_Name, values, whereClause, whereArgs)
            if (result == 0) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
                boolResult = false
            } else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            boolResult = false
        }

        dataBase.close()
        return boolResult
    }

    fun insertCountry(countryId:Int , name: String, imageByteArrray: ByteArray): Boolean {
        val db = writableDatabase
        db.beginTransaction()

        val values = ContentValues()
        values.put(SetDB.tblCountries.COL_IdCountry, countryId)
        values.put(SetDB.tblCountries.COL_Name, name)
        values.put(SetDB.tblCountries.COL_ImageURL, imageByteArrray)

        var isSuccess = false

        try {
            val result = db.insert(SetDB.tblCountries.Table_Name, null, values)
            if (result != -1L) {
                db.setTransactionSuccessful()
                isSuccess = true
            }
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        } finally {
            db.endTransaction()
            db.close()
        }

        return isSuccess
    }


    fun insertPost(post: Post, active:Int): Long{
        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var lastInsertId: Long = -1

        values.put(SetDB.tblPosts.COL_Title ,post.titlePost)
        values.put(SetDB.tblPosts.COL_Description ,post.description)
        values.put(SetDB.tblPosts.COL_IdUser ,post.idUser)
        values.put(SetDB.tblPosts.COL_IdCountry ,post.idCountry)
        values.put(SetDB.tblPosts.COL_Rate ,0)
        values.put(SetDB.tblPosts.COL_WRate ,0)
        values.put(SetDB.tblPosts.COL_ACTIVEP ,active)


        try {
            val result =  dataBase.insert(SetDB.tblPosts.Table_Name, null, values)
            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                lastInsertId = result
                Toast.makeText(this.context, "Success. Last inserted ID: $result", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
        }

        dataBase.close()

        return lastInsertId
    }

    fun insertPostMultimedia(imageByteArrays: MutableList<ByteArray>, lastId: Long): Boolean {
        val dataBase: SQLiteDatabase = this.writableDatabase
        var boolResult: Boolean = true
        var countImage: Int = 1
        try {
            val values: ContentValues = ContentValues()
            for (imageByteArray in imageByteArrays) {

                if(countImage == 1){
                    values.put(SetDB.tblMultimedia.COL_ImgURL, imageByteArray)
                }else if(countImage == 2){
                    values.put(SetDB.tblMultimedia.COL_ImgURL2, imageByteArray)
                }else{
                    values.put(SetDB.tblMultimedia.COL_ImgURL3, imageByteArray)
                }
                countImage++
            }
            countImage = 1
            values.put(SetDB.tblMultimedia.COL_IdPost, lastId.toInt())
            val result = dataBase.insert(SetDB.tblMultimedia.Table_Name, null, values)
            if (result == (-1).toLong()) {
                boolResult = false
            }
            // Muestra un mensaje de éxito o fracaso según el valor de boolResult
            if (boolResult) {
                Toast.makeText(this.context, "Success Images", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Failed Images", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            boolResult = false
        } finally {

            dataBase.close()
        }

        return boolResult
    }



    fun showToastWithUserDetails(context: Context): UserOffline? {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${SetDB.tblUsuarios.Table_Name}"
        val cursor = db.rawQuery(query, null)

        var idUser: Int?=null
        var nickname: String?=null
        var name: String?=null
        var lastName: String?=null
        var email: String?=null
        var password: String?=null
        var imageUrl: ByteArray?=null
        var userOffline: UserOffline?=null

        val userDetails = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                idUser = cursor.getInt(cursor.getColumnIndex(SetDB.tblUsuarios.COL_IdUser))
                nickname = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Nickname))
                name = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Name))
                lastName = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_LastName))
                email = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Email))
                password = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Password))
                imageUrl = cursor.getBlob(cursor.getColumnIndex(SetDB.tblUsuarios.COL_ImageURL))

                userDetails.append("Aquí inicia: ")
                userDetails.append("ID: $idUser\n")
                userDetails.append("Nickname: $nickname\n")
                userDetails.append("Name: $name\n")
                userDetails.append("Last Name: $lastName\n")
                userDetails.append("Email: $email\n")
                userDetails.append("Password: $password\n")
                userDetails.append("Image URL: $imageUrl\n\n")

                userOffline = UserOffline(
                    idUser,
                    nickname,
                    name,
                    lastName,
                    email,
                    password,
                    imageUrl
                )


            } while (cursor.moveToNext())
        }




        cursor.close()
        db.close()

        Toast.makeText(context, userDetails.toString(), Toast.LENGTH_LONG)
        Log.e("Terminado",userDetails.toString())
        return userOffline
    }

    fun showToastCountry(context: Context) {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${SetDB.tblCountries.Table_Name}"
        val cursor = db.rawQuery(query, null)

        val countryDetails = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(SetDB.tblCountries.COL_IdCountry))
                val nameCountry = cursor.getString(cursor.getColumnIndex(SetDB.tblCountries.COL_Name))
                val countryImageBlob = cursor.getBlob(cursor.getColumnIndex(SetDB.tblCountries.COL_ImageURL))

                countryDetails.append("ID: $id\n")
                countryDetails.append("Name: $nameCountry\n")
                countryDetails.append("Img: $countryImageBlob\n\n")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.e("Terminado", countryDetails.toString())
    }

    fun showToastMultimedia(context: Context) {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${SetDB.tblMultimedia.Table_Name}"
        val cursor = db.rawQuery(query, null)

        val multimediaDetails = StringBuilder()

        if (cursor.moveToFirst()) {
            do {

                val id = cursor.getInt(cursor.getColumnIndex(SetDB.tblMultimedia.COL_IdMul))
                val imgUrl = cursor.getBlob(cursor.getColumnIndex(SetDB.tblMultimedia.COL_ImgURL))
                val imgUrl2 = cursor.getBlob(cursor.getColumnIndex(SetDB.tblMultimedia.COL_ImgURL2))
                val imgUrl3 = cursor.getBlob(cursor.getColumnIndex(SetDB.tblMultimedia.COL_ImgURL3))
                val postId = cursor.getInt(cursor.getColumnIndex(SetDB.tblMultimedia.COL_IdPost))

                multimediaDetails.append("ID: $id\n")
                multimediaDetails.append("Imagen 1: $imgUrl\n")
                multimediaDetails.append("Imagen 2: $imgUrl2\n")
                multimediaDetails.append("Imagen 3: $imgUrl3\n")
                multimediaDetails.append("Post Id: $postId\n\n")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.e("Multimedia", multimediaDetails.toString())
    }


    fun showToastPosts(context: Context) {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${SetDB.tblPosts.Table_Name}"
        val cursor = db.rawQuery(query, null)

        val postsDetails = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdPost))
                val titlePo = cursor.getString(cursor.getColumnIndex(SetDB.tblPosts.COL_Title))
                val descPo = cursor.getString(cursor.getColumnIndex(SetDB.tblPosts.COL_Description))
                val idUser = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdUser))
                val idCountry = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdCountry))

                postsDetails.append("ID: $id\n")
                postsDetails.append("Name: $titlePo\n")
                postsDetails.append("Description: $descPo\n")
                postsDetails.append("IdUser: $idUser\n")
                postsDetails.append("Pais: $idCountry\n")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.e("Posts", postsDetails.toString())
    }

    fun showPosts(context: Context){
        val db = this.readableDatabase
        val query = "SELECT * FROM ${SetDB.tblPosts.Table_Name}"
        val cursor = db.rawQuery(query, null)
        var id: Int? = null
        var titlePo: String? = null
        var descPo: String? = null
        var idUser: Int? = null
        var idCountry: Int? = null
        var idUserSelect: Int? = null
        var nickName: String? = null
        var imageUrl: ByteArray? = null
        var idPostMult: Int? = null
        var imageMult: MutableList<ByteArray>? = null
        var idCountrySelect: Int? = null
        var nameCountry: String? = null
        var imageCountry: ByteArray? = null
        var date: String? = null
        var rating: Float? = null
        val byteArrayList: MutableList<ByteArray> = mutableListOf()
        var byteArrayForList: ByteArray


        val postsDetails = StringBuilder()


        if (cursor.moveToFirst()) {
            do {

                 id = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdPost))
                 titlePo = cursor.getString(cursor.getColumnIndex(SetDB.tblPosts.COL_Title))
                 descPo = cursor.getString(cursor.getColumnIndex(SetDB.tblPosts.COL_Description))
                 idUser = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdUser))
                 idCountry = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdCountry))
                 date = cursor.getString(cursor.getColumnIndex(SetDB.tblPosts.COL_RowDate))
                 rating = cursor.getFloat(cursor.getColumnIndex(SetDB.tblPosts.COL_Rate))

                // Convertir la fecha de SQLite a un objeto Date
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateFormat = inputFormat.parse(date)

                // Formatear la fecha para obtener solo año, mes y día
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = outputFormat.format(dateFormat)

                val queryUser = "SELECT * FROM ${SetDB.tblUsuarios.Table_Name} WHERE ${SetDB.tblUsuarios.COL_IdUser} = $idUser "
                val cursorUser = db.rawQuery(queryUser, null)
                if(cursorUser.moveToFirst()){
                    do{
                         idUserSelect = cursorUser.getInt(cursorUser.getColumnIndex(SetDB.tblUsuarios.COL_IdUser))
                         nickName = cursorUser.getString(cursorUser.getColumnIndex(SetDB.tblUsuarios.COL_Nickname))
                         imageUrl = cursorUser.getBlob(cursorUser.getColumnIndex(SetDB.tblUsuarios.COL_ImageURL))

                        postsDetails.append("Datos Usuario:\n")
                        postsDetails.append("ID: $idUserSelect\n")
                        postsDetails.append("NickName: $nickName\n")
                        postsDetails.append("Image: $imageUrl\n")

                    } while (cursorUser.moveToNext())
                }

                val queryImages = "SELECT * FROM ${SetDB.tblMultimedia.Table_Name} WHERE ${SetDB.tblMultimedia.COL_IdPost} = $id "
                val cursorImages = db.rawQuery(queryImages, null)
                if(cursorImages.moveToFirst()){
                    do{
                        idPostMult = cursorImages.getInt(cursorImages.getColumnIndex(SetDB.tblMultimedia.COL_IdPost))
                        byteArrayForList = cursorImages.getBlob(cursorImages.getColumnIndex(SetDB.tblMultimedia.COL_ImgURL))

                        byteArrayList.add(byteArrayForList)

                        postsDetails.append("Imagenes Post:\n")
                        postsDetails.append("IdPostMult: $idPostMult\n")
                        postsDetails.append("Img: $imageMult\n")

                    } while (cursorImages.moveToNext())
                }

                val queryCountry =  "SELECT * FROM ${SetDB.tblCountries.Table_Name} WHERE ${SetDB.tblCountries.COL_IdCountry} = $idCountry "
                val cursorCountry = db.rawQuery(queryCountry, null)
                if(cursorCountry.moveToFirst()){
                    do{
                        idCountrySelect = cursorCountry.getInt(cursorCountry.getColumnIndex(SetDB.tblCountries.COL_IdCountry))
                        nameCountry = cursorCountry.getString(cursorCountry.getColumnIndex(SetDB.tblCountries.COL_Name))
                        imageCountry = cursorCountry.getBlob(cursorCountry.getColumnIndex(SetDB.tblCountries.COL_ImageURL))

                        postsDetails.append("Datos Usuario:\n")
                        postsDetails.append("ID: $idCountrySelect\n")
                        postsDetails.append("NickName: $nameCountry\n")
                        postsDetails.append("Image: $imageCountry\n")

                    } while (cursorUser.moveToNext())
                }

                postsDetails.append("ID: $id\n")
                postsDetails.append("Name: $titlePo\n")
                postsDetails.append("Description: $descPo\n")
                postsDetails.append("IdUser: $idUser\n")
                postsDetails.append("Pais: $idCountry\n")

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.e("Posts", postsDetails.toString())
    }


    fun checkCredentials(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${SetDB.tblUsuarios.Table_Name} WHERE ${SetDB.tblUsuarios.COL_Email} = ? AND ${SetDB.tblUsuarios.COL_Password} = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        val isValid = cursor.moveToFirst()
        if (isValid) {
            // Usuario encontrado, asigna los datos a UserSingleton
            UserSingleton.currentUserId = cursor.getInt(cursor.getColumnIndex(SetDB.tblUsuarios.COL_IdUser))
            UserSingleton.currentUserNickname = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Nickname))
            UserSingleton.currentUserName = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Name))
            UserSingleton.currentUserLastName = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_LastName))
            UserSingleton.currentUserEmail = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Email))
            UserSingleton.currentUserPassword = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_Password))
            UserSingleton.currentUserImg = cursor.getString(cursor.getColumnIndex(SetDB.tblUsuarios.COL_ImageURL))

            cursor.close()
        }

        return isValid
    }

    fun getCountryNames(): List<CountryId> {
        val countryData = mutableListOf<CountryId>()
        val db = readableDatabase

        val query = "SELECT ${SetDB.tblCountries.COL_IdCountry}, ${SetDB.tblCountries.COL_Name} FROM ${SetDB.tblCountries.Table_Name}"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex(SetDB.tblCountries.COL_IdCountry))
                val name = it.getString(it.getColumnIndex(SetDB.tblCountries.COL_Name))
                val country = CountryId(id, name)
                countryData.add(country)
            }
        }

        return countryData
    }


    fun showPostsOffline(context: Context) {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${SetDB.tblPosts.Table_Name}"
        val cursor = db.rawQuery(query, null)
        var id: Int? = null
        var titlePo: String? = null
        var descPo: String? = null
        var idUser: Int? = null
        var idCountry: Int? = null
        var activeP: Int? = null
        var idPostMult: Int? = null
        var img1:ByteArray?=null
        var img2:ByteArray?=null
        var img3:ByteArray?=null

        val postsDetails = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdPost))
                titlePo = cursor.getString(cursor.getColumnIndex(SetDB.tblPosts.COL_Title))
                descPo = cursor.getString(cursor.getColumnIndex(SetDB.tblPosts.COL_Description))
                idUser = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdUser))
                idCountry = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_IdCountry))
                activeP = cursor.getInt(cursor.getColumnIndex(SetDB.tblPosts.COL_ACTIVEP))

                val byteArrayList: MutableList<ByteArray> = mutableListOf()

                val queryImages =
                    "SELECT * FROM ${SetDB.tblMultimedia.Table_Name} WHERE ${SetDB.tblMultimedia.COL_IdPost} = $id "
                val cursorImages = db.rawQuery(queryImages, null)
                if (cursorImages.moveToFirst()) {
                    do {
                        idPostMult = cursorImages.getInt(cursorImages.getColumnIndex(SetDB.tblMultimedia.COL_IdPost))
                        img1 = cursorImages.getBlob(cursorImages.getColumnIndex(SetDB.tblMultimedia.COL_ImgURL))
                        img2 = cursorImages.getBlob(cursorImages.getColumnIndex(SetDB.tblMultimedia.COL_ImgURL2))
                        img3 = cursorImages.getBlob(cursorImages.getColumnIndex(SetDB.tblMultimedia.COL_ImgURL3))

                        if (img1 != null) {
                            byteArrayList.add(img1)
                        }
                        if (img2 != null) {
                            byteArrayList.add(img2)
                        }
                        if (img3 != null) {
                            byteArrayList.add(img3)
                        }

                        postsDetails.append("Imagenes Post:\n")
                        postsDetails.append("IdPostMult: $idPostMult\n")
                        postsDetails.append("Img : $img1\n")
                        postsDetails.append("Img 2: $img2\n")
                        postsDetails.append("Img 3: $img3\n")

                    } while (cursorImages.moveToNext())
                }
                Log.e("NumImages", "Número de imágenes para el post $id: ${byteArrayList.size}")
                val postsOffline = PostsOffline(
                    titlePo,
                    descPo,
                    idUser,
                    idCountry,
                    activeP,
                    id,
                    byteArrayList.toList()
                )

                PostsOfflineList.PostsOfflineList += postsOffline

                postsDetails.append("Id: $id\n")
                postsDetails.append("Name: $titlePo\n")
                postsDetails.append("Description: $descPo\n")
                postsDetails.append("IdUser: $idUser\n")
                postsDetails.append("Pais: $idCountry\n")
                postsDetails.append("Active : $activeP\n")

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.e("Posts", postsDetails.toString())
    }



    fun deletePost(postId: Int) {
        val db = writableDatabase
        val tableName = SetDB.tblPosts.Table_Name
        val whereClause = "${SetDB.tblPosts.COL_IdPost} = ?"
        val whereArgs = arrayOf(postId.toString())

        db.delete(tableName, whereClause, whereArgs)
        db.close()
    }

    fun deleteUser(iduser: Int?) {
        val db = writableDatabase
        val tableName = SetDB.tblUsuarios.Table_Name
        val whereClause = "${SetDB.tblUsuarios.COL_IdUser} = ?"
        val whereArgs = arrayOf(iduser.toString())

        db.delete(tableName, whereClause, whereArgs)
        db.close()
    }








}