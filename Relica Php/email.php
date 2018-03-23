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
    function resetPassTemplate() {

        // Open file in the main project folder
        $file = fopen("templates/resetPasswordTemplate.html", "r") or die("File openning failed.");

        // Save content in $template
        $sablon = fread($file, filesize("templates/resetPasswordTemplate.html"));

        fclose($file);

        return $sablon;

    }
    function sendEmail($details) {

        $subject = $details["subject"]; //subject
        $whom = $details["whom"]; //to
        $fromName = $details["fromName"]; //sender name
        $fromEmail = $details["fromEmail"]; //sender mail
        $body = $details["body"]; //body

        // Headers
        $headers = "MIME-Version: 1.0" . "\r\n";
        $headers .= "Content-type:text/html;content=UTF-8" . "\r\n";
        $headers .= "From: " . $fromName . " <" . $fromEmail . ">" . "\r\n";

        // Send email
        if(mail($whom, $subject, $body, $headers){
            return "Mail sent";
        }
        else{
        	return "Mail send is failed";
        }
    }
    
    function sendEmailPhpMailer($detay){

        $subject = $detay["konu"]; //subject
        $whom = $detay["kime"]; //to
        $body = $detay["body"]; //gövde
        $fromName = $detay["fromName"]; //sender name

        $mail = new PHPMailer();
        $mail->IsSMTP();
        $mail->CharSet = 'UTF-8';
        $mail->Host = "smtp.gmail.com";
        $mail->SMTPDebug = 0;
        $mail->SMTPAuth = true;
        $mail->Port = 587;
        $mail->Username = "volkan.yilmazboun@gmail.com";
        $mail->Password = "denemesifresi"; // Change it accordingly
        $mail->SMTPSecure = 'tlf';
        $mail->SetFrom($mail->Username, $fromName);
        $mail->isHTML(true);

        $mail->Subject = $subject;
        $mail->MsgHTML($body);
        $mail->AddAddress($whom);


        if (!$mail->Send()) {
            return "Mailer error: " . $mail->ErrorInfo;
        } else {
            return "Mail succesfully sent!";
        }

    }
}
?>
