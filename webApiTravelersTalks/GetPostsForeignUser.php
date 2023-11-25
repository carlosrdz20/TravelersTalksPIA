<?php

include 'DAOPosts.php';

$posts = new posts();

$foreignuser = $_POST['foreignUser'];
$iduser = $_POST['idUser'];

$res = $posts->getPostsForeign($foreignuser, $iduser);

echo json_encode($res);


?>