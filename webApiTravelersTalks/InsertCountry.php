<?php

include 'DAOCountry.php';


$user = new country();

$countryname = $_POST['countryName'];
$countryimg = $_POST['countryImg'];

$res = $user->insertCountry($countryname,$countryimg);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}


?>