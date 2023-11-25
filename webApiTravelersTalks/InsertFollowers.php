<?php

include 'DAOFollow.php';

$user = new follow();


$follower = $_POST['follower'];
$following = $_POST['following'];

$res = $user->insertFollow($follower,$following);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
    $res2['resultado'] = "false";
    echo json_encode($res2);
}



?>