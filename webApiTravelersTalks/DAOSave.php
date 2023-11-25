<?php

include 'DBConnection.php';

class save extends DB{

    function insertSave($saveUser,$savePost){

        try{
            $con = $this->connect();
            $query = "CALL InsertOrUpdateSave(:userid, :postid);";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':userid', $saveUser);
            $exec->bindParam(':postid', $savePost);
    
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