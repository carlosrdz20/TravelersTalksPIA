<?php


include 'DAORating.php';

$user = new rating();


$iduserSave = $_POST['iduser'];
$idpostSave = $_POST['postid'];

$res = $user->getRatingUnique($iduserSave,$idpostSave);

echo json_encode($res);




?>