<?php

include 'DAOPosts.php';


$user = new posts();


$title = $_POST['titleD'];
$descrip = $_POST['descripD'];
$idcountry = $_POST['idcountryD'];
$idpost = $_POST['idpostD'];

$res = $user->updateDrafts($title, $descrip,$idcountry,$idpost);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
     $res2['resultado'] = "false";
    echo json_encode($res2);  
}

?>