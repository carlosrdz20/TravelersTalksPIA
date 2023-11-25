<?php

include 'DBConnection.php';

class rating extends DB{

    function insertRating($userid,$postid,$rate){

        try{
            $con = $this->connect();
            $query = "CALL InsertOrUpdateRating(:userid, :postid, :rate);";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':userid', $userid);
            $exec->bindParam(':postid', $postid);
            $exec->bindParam(':rate', $rate);
    
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


        function getRatingUnique($userid,$postid){

        try{
            $con = $this->connect();
            $query = "SELECT rate FROM `tbl_rating` WHERE idUser = :userid AND idPost = :postid ";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':userid', $userid);
            $exec->bindParam(':postid', $postid);
    
            $exec->execute();
    
            if ($exec->rowCount() > 0) {
                $rating = $exec->fetch(PDO::FETCH_ASSOC);
                $exec = null;
                return $rating;
            } else {
                $result['rate'] = 0;
                $exec = null;
                return $result; 
            }
        }catch(PDOException $e){
                        echo "Error en la inserción: " . $e->getMessage();
                        $stmt = null;
                        return false;
        }

    }

}



?>