<?php

include 'DAOCountry.php';

$countries = new country();
$res = $countries->getAllCountries();

if($res){
    // Convertir los BLOBs a base64
    foreach($res as &$usuario){
        $usuario['countryImg'] = base64_encode($usuario['countryImg']);
        
    }
    echo json_encode($res);
    
} else {
    $response['error'] = "No se encontraron países";
    echo json_encode($response);
}



?>