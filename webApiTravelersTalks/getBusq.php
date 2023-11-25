<?php


include 'DAOPosts.php';

$user = new posts();


$txtBusq = "%". $_POST['txtBusq'] . "%";
$countryId = $_POST['countryId'];
$rating = $_POST['rating'];
$orderF = $_POST['orderF'];
$iduser = $_POST['iduser'];


$res = $user->getPostsBusq($txtBusq,$countryId,$rating,$orderF,$iduser);
    echo json_encode($res);




?>