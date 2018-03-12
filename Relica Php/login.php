<?php
/**
 * Created by PhpStorm.
 */
$returnArray=array();

if (empty($_REQUEST["username"]) || empty($_REQUEST["password"])) {
    $returnArray["status"] = "203";
    $returnArray["message"] = "Missing data!";
    echo json_encode($returnArray); // We can see info on Android via echo
    return;
}


$username = htmlentities($_REQUEST["username"]);
$password = htmlentities($_REQUEST["password"]);


// Connection to database
require ("access.php");
$access = new access("localhost", "root", "", "relica");
$connection=$access->connect();

if (!$connection)
{
    $returnArray["status"] = "404";
    $returnArray["message"] = "Connection failed!";
    echo json_encode($returnArray);
    return;
}

$user=$access->selectUser($username);

if (empty($user))
{
    $returnArray["status"] = "400";
    $returnArray["message"] = "Username does not exist.";
    echo json_encode($returnArray);
    return;

}

$safe_password=$user["password"];
$salt=$user["salt"];

if ($user["emailVerification"]==1 && $safe_password==sha1($password . $salt)){
    // Save user infos in array to return as Json.
    $returnArray["status"] = "200";
    $returnArray["message"] = "Sign in successful.";
    $returnArray["id"] = $user["id"];
    $returnArray["username"] = $user["username"];
    $returnArray["mail"] = $user["mail"];
    $returnArray["fullname"] = $user["fullname"];
    $returnArray["avatar"] = $user["avatar"];

    echo json_encode($returnArray);
    return;
}
else if ($user["emailVerification"]==0){
    $returnArray["status"] = "401";
    $returnArray["message"] = "You need to verify your mail.";
    echo json_encode($returnArray);
    return;
}
else if ($safe_password!=sha1($password . $salt)){
    $returnArray["status"] = "403";
    $returnArray["message"] = "Password is wrong.";
    echo json_encode($returnArray);
    return;
}

$access->disconnect();

echo json_encode($returnArray);


?>
