<?php
/**
 * Created by PhpStorm.
 */
require 'PHPMailer/class.phpmailer.php';
require 'PHPMailer/PHPMailerAutoload.php';

class email{

    function createToken($length){

        $chars="qwertyuopasdfghjklzxcvbnmiQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        $numberOfChars=strlen($chars);

        $token="";

        for ($i=0; $i<$length; $i++){
            $token.=$chars[rand(0,$numberOfChars-1)];
        }
        return $token;
    }
    function verificationTemplate() {

        // Open file in the main project folder
        $file = fopen("templates/emailVerificationTemplate.html", "r") or die("File openning failed.");

        // Save content in $template
        $sablon = fread($file, filesize("templates/emailVerificationTemplate.html"));

        fclose($file);

        return $sablon;

    }
    function sendEmail($detay) {

        $subject = $detay["subject"]; //subject
        $whom = $detay["whom"]; //to
        $fromName = $detay["fromName"]; //gönderici ismi
        $fromEmail = $detay["fromEmail"]; //gönderici mail adresi
        $body = $detay["body"]; //gövde

        // Headers
        $headers = "MIME-Version: 1.0" . "\r\n";
        $headers .= "Content-type:text/html;content=UTF-8" . "\r\n";
        $headers .= "From: " . $fromName . " <" . $fromEmail . ">" . "\r\n";

        // Send email
        mail($whom, $subject, $body, $headers);
    }
}
?>
