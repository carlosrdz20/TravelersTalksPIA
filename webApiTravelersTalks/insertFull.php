<?php

include 'DAOPosts.php';


$user = new posts();


$title = $_POST['title'];
$descrip = $_POST['descrip'];
$iduser = $_POST['iduser'];
$idcountry = $_POST['idcountry'];
$active = $_POST['active'];
$imgurl = $_POST['imgurl'];
$imgurldos = $_POST['imgurldos'];
$imgurltres = $_POST['imgurltres'];

$res = $user->insertOfflinePosts($title,$descrip,$iduser,$idcountry,$active,$imgurl,$imgurldos,$imgurltres);
if($res){
$res2['resultado'] = $res;
echo json_encode($res2);    
}else{
$res2['resultado'] = $res;
echo json_encode($res2);    
}


?>