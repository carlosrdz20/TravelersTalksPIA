<?php

include 'DAOPosts.php';


$user = new posts();

$idpost = $_POST['idpost'];
$imgurl = $_POST['url'];
$imgurl2 = $_POST['urldos'];
$imgurl3 = $_POST['urltres'];

$res = $user->insertPostsMultimedia($idpost,$imgurl,$imgurl2,$imgurl3);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}

?>