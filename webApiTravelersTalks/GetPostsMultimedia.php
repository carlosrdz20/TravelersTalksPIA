<?php

include 'DAOPosts.php';

$posts = new posts();


$res = $posts->getPosts();
echo json_encode($res);

?>