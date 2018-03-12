<?php 

$returnArray=array();

if (empty($_REQUEST["id"])) {
    $returnArray["status"] = "203";
    $returnArray["message"] = "Missing required information";
    echo json_encode($returnArray);
    return;
}


$id = htmlentities($_REQUEST["id"]);

// Connection to database
require ("access.php");
$access = new access("localhost", "root", "", "relica");
$connection=$access->connect();

if (!$connection)
{
    $returnArray["status"] = "404";
    $returnArray["message"] = "Connection failed.";
    echo json_encode($returnArray);
    return;
}

// User infos
$user = $access->selectUserIdyeGore($id);

$returnArray["status"] = "200";
$returnArray["message"] = "Success";
$returnArray["id"] = $user["id"];
$returnArray["username"] = $user["username"];
$returnArray["fullname"] = $user["fullname"];
$returnArray["mail"] = $user["mail"];
$returnArray["avatar"] = $user["avatar"];

// Disconnect
$access->disconnect();


// Return array to user
echo json_encode($returnArray);

?>
