<?php

include 'DAOUsers.php';


$user = new users();


$id = $_POST['idUser'];
$nickname = $_POST['nickname'];
$nameuser = $_POST['nameuser'];
$lastname = $_POST['lastname'];
$email = $_POST['email'];
$pwduser = $_POST['pwduser'];
$imguser = $_POST['imguser'];

$res = $user->editUsers($id,$nickname,$nameuser,$lastname,$email,$pwduser,$imguser);
if($res){
    $res2['resultado'] = "true";
    echo json_encode($res2);
}else{
    $res2['resultado'] = "false";
    echo json_encode($res2);
}

?>