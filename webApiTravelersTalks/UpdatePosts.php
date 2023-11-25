<?php

include 'DAOPosts.php';


$user = new posts();


$title = $_POST['title'];
$descrip = $_POST['descrip'];
$idcountry = $_POST['idcountry'];
$idpost = $_POST['idpost'];

$res = $user->updatePosts($title, $descrip,$idcountry,$idpost);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
     $res2['resultado'] = "false";
    echo json_encode($res2);  
}

?>