<?php

include 'DAOPosts.php';

$user = new posts();


$img1 = $_POST['imgunoD'];
$img2 = $_POST['imgdosD'];
$img3 = $_POST['imgtresD'];
$idpost = $_POST['idpostD'];

$res = $user->updateDraftsImages($img1, $img2,$img3,$idpost);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
     $res2['resultado'] = "false";
    echo json_encode($res2);  
}




?>