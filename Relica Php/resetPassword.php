<?php 

$returnArray=array();

if (empty($_REQUEST["mail"])) {
    $returnArray["status"] = "203";
    $returnArray["message"] = "Required information is missing.";
    echo json_encode($returnArray);
    return;
}

$mail = htmlentities($_REQUEST["mail"]);

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

// User information
$user = $access->selectUserMaileGore($mail);

if (empty($user)) {
	$returnArray["status"] = "204";
    $returnArray["message"] = "Mail address is not found";
    echo json_encode($returnArray);
    $access->disconnect();
    return;
}

    require ("email.php");
    $email=new email();
    $token=$email->createToken(20);
    $access->saveToken("resetPasswordTokens",$token,$user["id"]);

   // Send mail
    $details=array();
    $details["subject"]="Password reset";
    $details["whom"]=$user["mail"];
    $details["fromName"]="Relica";
    $details["fromEmail"]="volkan.yilmazboun@gmail.com";
   

    $temp=$email->resetPassTemplate();
    $temp=str_replace("{token}",$token,$temp);
    $details["body"]=$temp;

    $result=$email->sendEmail($details);
    $returnArray["mailstatus"] = $result;

    $returnArray["status"] = "200";
    $returnArray["message"] = "Email sent for password reset";

$access->disconnect();
echo json_encode($returnArray);

 ?>
