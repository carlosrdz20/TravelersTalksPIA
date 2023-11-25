<?php

include 'DAOPosts.php';

$posts = new posts();

$idpostsuser = $_POST['iduser'];

$res = $posts->getMyPosts($idpostsuser);
echo json_encode($res);

?>