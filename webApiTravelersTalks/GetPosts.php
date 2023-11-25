<?php

include 'DAOPosts.php';

$posts = new posts();

$newidUser = $_POST['idusuario'];

$res = $posts->getPosts($newidUser);

echo json_encode($res);   



 


?>