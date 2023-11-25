<?php
include 'DAOPosts.php';


$user = new posts();

$idpost = $_POST['idpost'];

$res = $user->deletePost($idpost);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
     $res2['resultado'] = "false";
    echo json_encode($res2);  
}
?>