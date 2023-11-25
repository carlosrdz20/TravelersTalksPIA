<?php

include 'DAOUsers.php';


$user = new users();


$email = $_POST['email'];
$password = $_POST['password'];

$res = $user->getUsuarioUnique($email,$password);
if($res){
    echo json_encode($res);
}




?>