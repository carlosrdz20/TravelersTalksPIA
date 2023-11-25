<?php

include 'DBConnection.php';

class follow extends DB{

    function insertFollow($follower,$following){

        try{
            $con = $this->connect();
            $query = "CALL InsertOrUpdateFollowing(:userfollower, :userfollowing);";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':userfollower', $follower);
            $exec->bindParam(':userfollowing', $following);
            
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