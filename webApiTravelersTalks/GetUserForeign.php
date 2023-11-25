<?php

include 'DAOUsers.php';

$post = new users();


$idforeign = $_POST['userforeign'];
$iduser = $_POST['iduser'];


$res = $post->getUserForeign($idforeign,$iduser);

    echo json_encode($res);


?>