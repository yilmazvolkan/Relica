<?php
/**
 * Created by PhpStorm.
 */
require 'PHPMailer/class.phpmailer.php';
require 'PHPMailer/PHPMailerAutoload.php';

class email{

    function CreateToken($length){

        $chars="qwertyuopasdfghjklzxcvbnmiQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        $numberOfChars=strlen($chars);

        $token="";

        for ($i=0; $i<$length; $i++){
            $token.=$chars[rand(0,$numberOfChars-1)];
        }
        return $token;
    }
}
?>
