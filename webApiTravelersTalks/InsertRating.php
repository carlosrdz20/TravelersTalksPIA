<?php


include 'DAORating.php';

$user = new rating();


$iduser = $_POST['userid'];
$idpost = $_POST['postid'];
$rate = $_POST['rate'];


$res = $user->insertRating($iduser,$idpost,$rate);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
    $res2['resultado'] = "false";
    echo json_encode($res2);
}



?>