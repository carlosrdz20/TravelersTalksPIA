<?php

include 'DAOPosts.php';

$user = new posts();

$userfollower = $_POST['userfollower'];


$res = $user->getFollowingPosts($userfollower);
    echo json_encode($res);

?>