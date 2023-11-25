<?php

include 'DAOPosts.php';


$user = new posts();


$img1 = $_POST['imguno'];
$img2 = $_POST['imgdos'];
$img3 = $_POST['imgtres'];
$idpost = $_POST['idpost'];

$res = $user->updatePostsImages($img1, $img2,$img3,$idpost);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}



?>