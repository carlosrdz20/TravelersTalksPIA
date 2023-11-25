<?php

include 'DAOPosts.php';


$user = new posts();


$title = $_POST['title'];
$descrip = $_POST['descrip'];
$iduser = $_POST['iduser'];
$idcountry = $_POST['idcountry'];
$active = $_POST['active'];

$res = $user->insertPosts($title,$descrip,$iduser,$idcountry,$active);
if($res){
$res2['idpost'] = $res;
echo json_encode($res2);    
}else{
$res2['idpost'] = $res;
echo json_encode($res2);    
}



?>