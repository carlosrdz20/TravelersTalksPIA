<?php

include 'DBConnection.php';

Class users extends DB{

function insertUser($nickname, $nameuser, $lastname, $email, $password, $imguser){
    try{
        $con = $this->connect();

        // Verificar si el correo electrónico ya existe
        $checkEmailQuery = "SELECT COUNT(*) as count FROM `tbl_usuarios` WHERE `email` = :email";
        $checkEmailStmt = $con->prepare($checkEmailQuery);
        $checkEmailStmt->bindParam(':email', $email);
        $checkEmailStmt->execute();
        $emailCount = $checkEmailStmt->fetch(PDO::FETCH_ASSOC)['count'];

        // Si el correo electrónico no existe, realizar el insert
        if ($emailCount == 0) {
            $insertQuery = "INSERT INTO `tbl_usuarios` (`nickname`, `nameUser`, `lastName`, `email`, `passwordUser`, `imageURL`) VALUES (:nickname, :nameu, :lastname, :email, :passwordu, :imguser)";
            $insertStmt = $con->prepare($insertQuery);
            
            $insertStmt->bindParam(':nickname', $nickname);
            $insertStmt->bindParam(':nameu', $nameuser);
            $insertStmt->bindParam(':lastname', $lastname);
            $insertStmt->bindParam(':email', $email);
            $insertStmt->bindParam(':passwordu', $password);
            $insertStmt->bindParam(':imguser', $imguser);

            $insertStmt->execute();

            if ($insertStmt->rowCount() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            // El correo electrónico ya existe, no realizar el insert
            return false;
        }
    } catch(PDOException $e){
        echo "Error en la inserción: " . $e->getMessage();
        return false;
    } finally {
        // Cerrar la conexión
        $con = null;
    }
}

        
        function getUsuarioUnique($email,$password){

            try{
                $con = $this->connect();
    
                $query = "SELECT * FROM `tbl_usuarios` WHERE email = :emailuser AND passwordUser = :passworduser;";
                $exec = $con->prepare($query);
    
                $exec->bindParam(':emailuser', $email);
                $exec->bindParam(':passworduser', $password);
        
                $exec->execute();
        
                if($exec->rowCount() > 0){
                    $user = $exec->fetch(PDO::FETCH_ASSOC);
                    $exec = null;
                    return $user;
                }else{
                    $exec = null;
                    return false;
                }
            }catch(PDOException $e){
                echo "Error: " . $e->getMessage();
                $exec = null;
                return false;
            }
    
        }
        
            function editUsers($id,$nickname, $nameuser,$lastname, $email, $password, $imguser){
        try{
            $con = $this->connect();
            $query = "UPDATE `tbl_usuarios` SET `nickname` = :nickname, `nameUser` = :nameu, `lastName` = :lastname, `email` = :email,  `passwordUser` = :passwordu, `imageURL` = :imguser WHERE idUser = :idUser";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':idUser', $id);
            $exec->bindParam(':nickname', $nickname);
            $exec->bindParam(':nameu', $nameuser);
            $exec->bindParam(':lastname', $lastname);
            $exec->bindParam(':email', $email);
            $exec->bindParam(':passwordu', $password);
            $exec->bindParam(':imguser', $imguser);
    
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
        
        
        
    function getUserForeign($userforeign, $iduser){

        try{
            $con = $this->connect();
            $query = "SELECT
            u.idUser,
            u.nickname,
            u.nameUser,
            u.lastName,
            u.imageURL,
            (SELECT COUNT(*) FROM tbl_following WHERE idUserFollowing = :userid AND activeF = 1) AS followerCount,
            (SELECT COUNT(*) FROM tbl_following WHERE idUserFollower = :userid AND activeF = 1) AS followingCount,
            f.activeF
            FROM
                tbl_usuarios u
            LEFT JOIN
                tbl_following f ON u.idUser = f.idUserFollowing AND f.idUserFollower = :iduser
            WHERE
                u.idUser = :userid;";
            
            $exec = $con->prepare($query);       
    
            $exec->bindParam(':userid', $userforeign);
            $exec->bindParam(':iduser', $iduser);

            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $post = $exec->fetch(PDO::FETCH_ASSOC);
                $exec = null;
                return $post; 
            } else {
                $post = $exec->fetch(PDO::FETCH_ASSOC);
                $exec = null;
                return $post; 
            }
        }catch(PDOException $e){
                echo "Error en la inserción: " . $e->getMessage();
                $stmt = null;
                return false;
        }


    }
        
        
}

?>