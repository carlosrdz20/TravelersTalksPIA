<?php

include 'DAOPosts.php';

$posts = new posts();

$idpostsuser = $_POST['iduser'];

$res = $posts->getFavorites($idpostsuser);
    echo json_encode($res);



?>