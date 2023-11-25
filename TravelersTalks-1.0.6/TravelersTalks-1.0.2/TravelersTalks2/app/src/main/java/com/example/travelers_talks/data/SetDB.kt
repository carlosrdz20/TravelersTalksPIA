package com.example.travelers_talks.data

class SetDB {

    companion object{
        val DB_Name = "dbTravelersTalks15"
        val DB_Version = 12
    }

    abstract class tblUsuarios{
        companion object{
            val Table_Name = "tbl_usuarios"
            val COL_IdUser = "idUser"
            val COL_Nickname = "nickname"
            val COL_Name = "nameUser"
            val COL_LastName = "lastName"
            val COL_Email = "email"
            val COL_Password = "passwordUser"
            val COL_ImageURL = "imageURL"
        }
    }

    abstract class tblCountries{
        companion object{
            val Table_Name = "tbl_country"
            val COL_IdCountry = "idCountry"
            val COL_Name = "countryName"
            val COL_ImageURL = "countryImg"
        }
    }

    abstract class tblPosts{
        companion object{
            val Table_Name = "tbl_posts"
            val COL_IdPost = "idPost"
            val COL_Title = "titlePost"
            val COL_Description = "descPost"
            val COL_IdUser = "idUser"
            val COL_IdCountry = "idCountry"
            val COL_RowDate = "rowDate"
            val COL_Rate = "rate"
            val COL_WRate = "wrate"
            val COL_ACTIVEP = "activeP"
        }
    }

    abstract class tblMultimedia{
        companion object{
            val Table_Name = "tbl_multimedia"
            val COL_IdMul = "idMultimedia"
            val COL_IdPost = "imgURL"
            val COL_ImgURL = "idPost"
            val COL_ImgURL2 = "imgURL2"
            val COL_ImgURL3 = "imgURL3"
        }
    }

    abstract class tblFollowing{
        companion object{
            val Table_Name = "tbl_following"
            val COL_IdFollow = "idFollow"
            val COL_IdUserFollower = "idUserFollower"
            val COL_IdUserFollowing = "idUserFollowing"
            val COL_ActiveF = "activeF"
        }
    }

    abstract class tblRating{
        companion object{
            val Table_Name = "tbl_rating"
            val COL_IdRating = "idRating"
            val COL_IdUser = "idUser"
            val COL_IdPost = "idPost"
            val COL_Rate = "rate"
        }
    }

    abstract class tblSave{
        companion object{
            val Table_Name = "tbl_save"
            val COL_IdSave = "idSpost"
            val COL_IdUser = "idUser"
            val COL_IdPost = "idPost"
            val COL_ActiveS = "activeS"
        }
    }











}