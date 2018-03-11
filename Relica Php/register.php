<?php
/**
 * Created by PhpStorm.
 */
$returnArray=array();

if (empty($_REQUEST["username"]) || empty($_REQUEST["password"]) || empty($_REQUEST["mail"]) || empty($_REQUEST["fullname"])) {
    $returnArray["status"] = "203";
    $returnArray["message"] = "Missing data!";
    echo json_encode($returnArray); // We can see info on Android via echo 
    return;
}


$username = htmlentities($_REQUEST["username"]);
$password = htmlentities($_REQUEST["password"]);
$mail = htmlentities($_REQUEST["mail"]);
$fullname = htmlentities($_REQUEST["fullname"]);

// More safe password consists of 20 char length binary password with cryptography
$salt = openssl_random_pseudo_bytes(20);
$safe_password = sha1($password . $salt); 

// Connection to database
require ("access.php");
$access = new access("localhost", "root", "", "relica");
$connection=$access->connect();

if (!$baglanti)
{
    $returnArray["status"] = "404";
    $returnArray["message"] = "Connection failed!";
    echo json_encode($returnArray);
    return;
}

// Register operation
$result = $access->registerUser($username, $safe_password, $salt, $mail, $fullname);

if ($result){
// Success

    // Query according to username and get user infos
    $user = $access->selectUser($username);

    // Store user infos and return as JSON type
    $returnArray["status"] = "200";
    $returnArray["message"] = "Registration successfull!";
    $returnArray["id"] = $user["id"];
    $returnArray["username"] = $user["username"];
    $returnArray["mail"] = $user["mail"];
    $returnArray["fullname"] = $user["fullname"];
    $returnArray["avatar"] = $user["avatar"];

    require ("email.php");
    $email=new email();
    $token=$email->createToken(20);
    $access->saveToken($token,$user["id"]);

    //mail gönderilecek
    $detay=array();
    $detay["subject"]="Mail verification for Relica.";
    $detay["whom"]=$user["mail"];
    $detay["fromName"]="Relica";
    $detay["fromEmail"]="volkan.yilmazboun@gmail.com"; // it should be changed

    $sablon=$email->verificationTemplate();
    $sablon=str_replace("{token}",$token,$sablon);
    $detay["body"]=$sablon;
    $email->sendEmailPhpMailer($detay);


}else {

    $returnArray["status"] = "400";
    $returnArray["message"] = "Registration failed. Username is already exist.";

}

// Disconnection
$access->disconnect();


// Return as JSON
echo json_encode($returnArray);

?>
