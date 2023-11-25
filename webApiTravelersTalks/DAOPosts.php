<?php

include 'DBConnection.php';

Class posts extends DB{

        function insertPosts($title,$descrip,$iduser,$idcountry,$active){
            try{
            $con = $this->connect();
            $query = "INSERT INTO `tbl_posts` (`titlePost`, `descPost`, `idUser`, `idCountry`, `rate`, `wrate`,`activeP`) VALUES (:title,:descrip, :iduser, :idcountry, 0, 0, :active)";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':title', $title);
            $exec->bindParam(':descrip', $descrip);
            $exec->bindParam(':iduser', $iduser);
            $exec->bindParam(':idcountry', $idcountry);
            $exec->bindParam(':active', $active);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $lastInsertId = $con->lastInsertId(); // Obtiene el ID del último registro insertado
                $exec = null;
                return $lastInsertId;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                        echo "Error en la inserción: " . $e->getMessage();
                        $stmt = null;
                        return false;
        }
    }
    
        function insertPostsMultimedia($idpost,$url,$url2,$url3){
        try{
            $con = $this->connect();
            $query = "INSERT INTO `tbl_multimedia` (`ImgURL`,`idPost`,`ImgURL2`,`ImgURL3` ) VALUES (:imgurl,:idpost,:imgurldos,:imgurltres)";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':imgurl', $url);
            $exec->bindParam(':imgurldos', $url2);
            $exec->bindParam(':imgurltres', $url3);
            $exec->bindParam(':idpost', $idpost);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $exec = null;
                return true;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                        echo "Error en la inserción: " . $e->getMessage();
                        $stmt = null;
                        return false;
        }

    }
    
    function getPosts($iduser){

        try{
            $con = $this->connect();
            $query = "SELECT
            tbl_posts.idPost as PostId,
            tbl_posts.titlePost as TituloPost,
            tbl_posts.rowDate as DatePost,
            tbl_posts.descPost as Description,
            tbl_posts.idCountry as IdCount,
            tbl_posts.idUser as IdUser,
            tbl_posts.rate as Rating,
            tbl_usuarios.nickname as UserNickname,
            tbl_usuarios.imageURL as imageUser,
            tbl_country.countryName as CountryName,
            tbl_country.countryImg,
            tbl_multimedia.ImgURL as ImagenUno,
            tbl_multimedia.imgURL2 as ImagenDos,
            tbl_multimedia.imgURL3 as ImagenTres,
            tbl_following.activeF as FollowingStatus, -- Nueva columna para indicar si el usuario sigue al autor
            tbl_save.activeS as SavedStatus -- Nueva columna para indicar si el usuario tiene guardada la publicación
            FROM
                tbl_posts
            LEFT JOIN
                tbl_usuarios ON tbl_posts.idUser = tbl_usuarios.idUser
            LEFT JOIN
                tbl_country ON tbl_posts.idCountry = tbl_country.idCountry
            LEFT JOIN
                tbl_multimedia ON tbl_posts.idPost = tbl_multimedia.idPost
            LEFT JOIN
                tbl_following ON tbl_posts.idUser = tbl_following.idUserFollowing AND tbl_following.idUserFollower = :iduser
            LEFT JOIN
                tbl_save ON tbl_posts.idPost = tbl_save.idPost AND tbl_save.idUser = :iduser 
            WHERE
                tbl_posts.activeP = 1 AND tbl_posts.idUser != :iduser ORDER BY tbl_posts.rowDate DESC;";
            $exec = $con->prepare($query);       
    
            $exec->bindParam(':iduser', $iduser);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }

    }
    
    function getMyPosts($iduser){

        try{
            $con = $this->connect();
            $query = "SELECT tbl_posts.idPost as PostId, tbl_posts.titlePost as TituloPost,tbl_posts.rowDate as DatePost,tbl_posts.descPost as Description,tbl_posts.idCountry as IdCount,tbl_posts.idUser as IdUser, tbl_usuarios.nickname as UserNickname,tbl_usuarios.imageURL as imageUser, tbl_country.countryName as CountryName, tbl_country.countryImg, tbl_multimedia.ImgURL as ImagenUno,tbl_multimedia.imgURL2 as ImagenDos, tbl_multimedia.imgURL3 as ImagenTres FROM tbl_posts LEFT JOIN tbl_usuarios ON tbl_posts.idUser = tbl_usuarios.idUser LEFT JOIN tbl_country ON tbl_posts.idCountry = tbl_country.idCountry LEFT JOIN tbl_multimedia ON tbl_posts.idPost = tbl_multimedia.idPost WHERE activeP = 1 AND tbl_posts.idUser = :iduser ORDER BY tbl_posts.rowDate DESC";
            
            $exec = $con->prepare($query);       
    
            $exec->bindParam(':iduser', $iduser);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }

    }
    
    function updatePosts($titlepost, $descpost,$idcountry,$idpost){

        try{
            $con = $this->connect();
            $query = "UPDATE `tbl_posts` SET `titlePost` = :titlep, `descPost` = :descript, `idCountry` = :idcntry WHERE `tbl_posts`.`idPost` = :idpost ";

            $exec = $con->prepare($query);       
            
            $exec->bindParam(':titlep', $titlepost);
            $exec->bindParam(':descript', $descpost);
            $exec->bindParam(':idcntry', $idcountry);
            $exec->bindParam(':idpost', $idpost);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $exec = null;
                return true;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }


    }

    function updatePostsImages($imguno,$imgdos,$imgtres,$idpost){

        try{
            $con = $this->connect();
            $query = "UPDATE `tbl_multimedia` SET `ImgURL` = :imguno, `imgURL2` = :imgdos, `imgURL3` = :imgtres WHERE `tbl_multimedia`.`idPost` = :idpost ";

            $exec = $con->prepare($query);      
            $exec->bindParam(':imguno', $imguno);
            $exec->bindParam(':imgdos', $imgdos);
            $exec->bindParam(':imgtres', $imgtres);
            $exec->bindParam(':idpost', $idpost);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $exec = null;
                return true;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }


    }
    
        function deletePost($idpost){

        try{
            $con = $this->connect();
            $query = "UPDATE `tbl_posts` SET `activeP` = 0 WHERE `tbl_posts`.`idPost` = :idpost ";
            
            $exec = $con->prepare($query);       
        
            $exec->bindParam(':idpost', $idpost);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $exec = null;
                return true;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }


    }
    
    
        function getMyDrafts($iduser){

        try{
            $con = $this->connect();
            $query = "SELECT tbl_posts.idPost as PostId, tbl_posts.titlePost as TituloPost,tbl_posts.rowDate as DatePost,tbl_posts.descPost as Description,tbl_posts.idCountry as IdCount,tbl_posts.idUser as IdUser, tbl_usuarios.nickname as UserNickname,tbl_usuarios.imageURL as imageUser, tbl_country.countryName as CountryName, tbl_country.countryImg, tbl_multimedia.ImgURL as ImagenUno,tbl_multimedia.imgURL2 as ImagenDos, tbl_multimedia.imgURL3 as ImagenTres FROM tbl_posts LEFT JOIN tbl_usuarios ON tbl_posts.idUser = tbl_usuarios.idUser LEFT JOIN tbl_country ON tbl_posts.idCountry = tbl_country.idCountry LEFT JOIN tbl_multimedia ON tbl_posts.idPost = tbl_multimedia.idPost WHERE activeP = 2 AND tbl_posts.idUser = :iduser ORDER BY tbl_posts.rowDate DESC";
            
            $exec = $con->prepare($query);       
    
            $exec->bindParam(':iduser', $iduser);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            } else {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }

    }
    
    function getFavorites($iduser){

        try{
            $con = $this->connect();
            $query = "SELECT tbl_posts.idPost as PostId, tbl_posts.titlePost as titlePost, tbl_posts.descPost as descPost, tbl_posts.idUser as IdUsuarioPost, tbl_usuarios.nickname as usernickname, tbl_usuarios.imageURL as imageUser, tbl_posts.idCountry as CountryId, tbl_country.countryImg as countryImg, tbl_posts.rowDate as RowDate, tbl_posts.rate as Rating, tbl_multimedia.ImgURL as imageUno, tbl_multimedia.imgURL2 as imageDos, tbl_multimedia.imgURL3 as imageTres FROM tbl_posts LEFT JOIN tbl_usuarios ON tbl_posts.idUser = tbl_usuarios.idUser LEFT JOIN tbl_country ON tbl_posts.idCountry = tbl_country.idCountry LEFT JOIN tbl_multimedia ON tbl_posts.idPost = tbl_multimedia.idPost LEFT JOIN tbl_save ON tbl_posts.idPost = tbl_save.idPost WHERE tbl_posts.activeP = 1 AND tbl_save.idUser = :iduser AND tbl_save.activeS = 1 ORDER BY tbl_posts.rowDate DESC;";
            
            $exec = $con->prepare($query);       
    
            $exec->bindParam(':iduser', $iduser);

            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            } else {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }


    }
    
        function getPostsForeign($userforeign, $iduser){

        try{
            $con = $this->connect();
            $query = "SELECT tbl_posts.idPost as PostId, tbl_posts.titlePost as TituloPost,tbl_posts.rowDate as DatePost,tbl_posts.descPost as Description,tbl_posts.idCountry as IdCount,tbl_posts.idUser as IdUser,tbl_posts.rate as Rating,tbl_usuarios.nickname as UserNickname,tbl_usuarios.imageURL as imageUser, tbl_country.countryName as CountryName, tbl_country.countryImg, tbl_multimedia.ImgURL as ImagenUno,tbl_multimedia.imgURL2 as ImagenDos, tbl_multimedia.imgURL3 as ImagenTres, tbl_save.activeS FROM tbl_posts LEFT JOIN tbl_usuarios ON tbl_posts.idUser = tbl_usuarios.idUser LEFT JOIN tbl_country ON tbl_posts.idCountry = tbl_country.idCountry LEFT JOIN tbl_multimedia ON tbl_posts.idPost = tbl_multimedia.idPost LEFT JOIN tbl_save ON tbl_posts.idPost = tbl_save.idPost AND tbl_save.idUser = :idfollower  WHERE activeP = 1 AND tbl_posts.idUser = :idfollowing ORDER BY tbl_posts.rowDate DESC ;";
            $exec = $con->prepare($query);      

            $exec->bindParam(':idfollowing', $userforeign);
            $exec->bindParam(':idfollower', $iduser);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            } else {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }

    }
    
    function getPostsBusq($txtBusq,$countryId,$rating,$orderF,$iduser){

        try{
            $con = $this->connect();
            $query = "CALL busquedaAvanzadaFinal(:idCountry, :rating, :titleDescrip , :order,:iduser);";
            $exec = $con->prepare($query);
            
            $exec->bindParam(':idCountry', $countryId);
            $exec->bindParam(':rating', $rating);
            $exec->bindParam(':titleDescrip', $txtBusq);
            $exec->bindParam(':order', $orderF);
            $exec->bindParam(':iduser', $iduser);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            } else {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;  
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }

    }
    
    function getFollowingPosts($userforeign){

        try{
            $con = $this->connect();
            $query = "SELECT
    p.idPost as PostId,
    p.titlePost as TituloPost,
    p.rowDate as DatePost,
    p.descPost as Description,
    p.idCountry as IdCount,
    p.idUser as IdUser,
    p.rate as Rating,
    tbl_usuarios.nickname as UserNickname,
    tbl_usuarios.imageURL as imageUser,
    tbl_country.countryName as CountryName,
    tbl_country.countryImg,
    tbl_multimedia.ImgURL as ImagenUno,
    tbl_multimedia.imgURL2 as ImagenDos,
    tbl_multimedia.imgURL3 as ImagenTres,
    s.activeS as SavedStatus
    FROM
        tbl_following f
    INNER JOIN
        tbl_posts p ON f.idUserFollowing = p.idUser
    LEFT JOIN
        tbl_usuarios ON p.idUser = tbl_usuarios.idUser
    LEFT JOIN
        tbl_country ON p.idCountry = tbl_country.idCountry
    LEFT JOIN
        tbl_multimedia ON p.idPost = tbl_multimedia.idPost
    LEFT JOIN
        tbl_save s ON p.idPost = s.idPost AND s.idUser = :iduserfollower
    WHERE
        f.idUserFollower = :iduserfollower
        AND f.activeF = 1
        AND p.activeP = 1 ORDER BY p.rowDate DESC;";
            $exec = $con->prepare($query);    
            
            $exec->bindParam(':iduserfollower', $userforeign);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;
            } else {
                $post = $exec->fetchAll(PDO::FETCH_ASSOC);
                $exec = null;
                return $post;  
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }

    }
    
    function updateDrafts($titlepost, $descpost,$idcountry,$idpost){

        try{
            $con = $this->connect();
            $query = "UPDATE `tbl_posts` SET `titlePost` = :titlepost, `descPost` = :descript, `idCountry` = :idcountry, `activeP` = 1 WHERE `tbl_posts`.`idPost` = :idpost ";

            $exec = $con->prepare($query);       
            
            $exec->bindParam(':titlepost', $titlepost);
            $exec->bindParam(':descript', $descpost);
            $exec->bindParam(':idcountry', $idcountry);
            $exec->bindParam(':idpost', $idpost);
            
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $exec = null;
                return true;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }


    }

    function updateDraftsImages($imguno,$imgdos,$imgtres,$idpost){

        try{
            $con = $this->connect();
            $query = "UPDATE `tbl_multimedia` SET `ImgURL` = :imguno, `imgURL2` = :imgdos, `imgURL3` = :imgtres WHERE `tbl_multimedia`.`idPost` = :idpost ";

            $exec = $con->prepare($query);       
    
            $exec->bindParam(':imguno', $imguno);
            $exec->bindParam(':imgdos', $imgdos);
            $exec->bindParam(':imgtres', $imgtres);
            $exec->bindParam(':idpost', $idpost);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $exec = null;
                return true;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }


    }
    
    function insertOfflinePosts($title,$descrip,$iduser,$idcountry,$active, $imguno, $imgdos, $imgtres){
        try{
            $con = $this->connect();
            $query = "CALL insertarPosts(:titulo,:descripcion,:iduser,:idcountry,:active,:imgurl,:imgurldos,:imgurltres);";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':titulo', $title);
            $exec->bindParam(':descripcion', $descrip);
            $exec->bindParam(':iduser', $iduser);
            $exec->bindParam(':idcountry', $idcountry);
            $exec->bindParam(':active', $active);
            $exec->bindParam(':imgurl', $imguno);
            $exec->bindParam(':imgurldos', $imgdos);
            $exec->bindParam(':imgurltres', $imgtres);

            $exec->execute();

            if ($exec->rowCount() > 0) {
                $exec = null;
                return true;
            } else {
                $exec = null;
                return false; 
            }
        }catch(PDOException $e){
                        echo "Error en la inserción: " . $e->getMessage();
                        $stmt = null;
                        return false;
        }
    }
    
    
    
    
    
}

?>