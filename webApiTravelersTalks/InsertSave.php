<?php

include 'DAOSave.php';

$user = new save();


$iduserSave = $_POST['iduser'];
$idpostSave = $_POST['postid'];

$res = $user->insertSave($iduserSave,$idpostSave);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
    $res2['resultado'] = "false";
    echo json_encode($res2);
}

?>