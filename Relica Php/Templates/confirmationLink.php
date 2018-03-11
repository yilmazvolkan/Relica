<?php
/**
 * Created by PhpStorm.
 */

//echo "Hello... Verification for e-mail is successful..."

if(empty($_GET["token"])){
    echo "Missing info...";
    return;
}

$token=htmlentities($_GET["token"]);

// Connection to database
require ("../access.php");
$access = new access("localhost", "root", "", "relica");
$connection=$access->connect();

$id=$access->getUserId($token);

if (empty($id["id"])){
    echo "Something went wrong. Username is not found!";
    return;
}

// Verification of email to database
$result=$access->changeStatusEmail($id["id"],1);

if ($result){

    //delete token from emailTokens table
    $access->deleteToken($token);
    echo "Thank you! Your mail is approved successfully.";
}

$access->disconnect();

?>
