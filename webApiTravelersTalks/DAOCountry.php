<?php

include 'DBConnection.php';

Class country extends DB{

    function getAllCountries(){

        try{
            $con = $this->connect();

            $query = "SELECT idCountry,countryName, countryImg FROM `tbl_country`";
            $exec = $con->prepare($query);
    
            $exec->execute();
    
            if($exec->rowCount() > 0){
                $user = $exec->fetchAll(PDO::FETCH_ASSOC);
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
    
        function insertCountry($countryname, $countryimg){

        try{
            $con = $this->connect();
            $query = "INSERT INTO `tbl_country` (`countryName`, `countryImg`) VALUES (:countryname, :countryimg)";
            $exec = $con->prepare($query);       
            
            $exec->bindParam(':countryname', $countryname);
            $exec->bindParam(':countryimg', $countryimg);
    
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