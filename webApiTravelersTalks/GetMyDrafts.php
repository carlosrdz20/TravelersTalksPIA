<?php


include 'DAOPosts.php';

$posts = new posts();

$idpostsuser = $_POST['iduser'];

$res = $posts->getMyDrafts($idpostsuser);
echo json_encode($res);



?>