<?php

include 'DAOUsers.php';


$user = new users();


$nickname = $_POST['nickname'];
$nameuser = $_POST['nameuser'];
$lastname = $_POST['lastname'];
$email = $_POST['email'];
$pwduser = $_POST['pwduser'];
$imguser = $_POST['imguser'];

$res = $user->insertUser($nickname,$nameuser,$lastname,$email,$pwduser,$imguser);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}

?>