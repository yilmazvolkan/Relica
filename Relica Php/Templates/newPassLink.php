<?php
if (!empty($_POST["pass_1"]) && !empty($_POST["pass_2"]) && !empty($_POST["token"])) {

    $pass_1 = htmlentities($_POST["pass_1"]);
    $pass_2 = htmlentities($_POST["pass_2"]);
    $token = htmlentities($_POST["token"]);
    
    if ($pass_1 == $pass_2) { //Success
       // Connection to database
        require ("../access.php");
        $access = new access("localhost", "root", "", "relica");
        $connection=$access->connect();

        if (!$connection)
        {
          $returnArray["status"] = "404";
          $returnArray["message"] = "Connection failed.";
          echo json_encode($returnArray);
          return;
        }
        // Get user id via Token
        $user = $access->getUserID("resetPasswordTokens", $token);


        // Update database
        if (!empty($user)) {

            // Create safe password
            $salt = openssl_random_pseudo_bytes(20);
            $safe_password = sha1($pass_1 . $salt);
            
            // Update password
            $result = $access->updatePassword($user["id"], $safe_password, $salt);

            if ($result) {

                // Delete token
                $access->deleteToken("resetPasswordTokens", $token);
                $message = "New password was successfully created";

                header("Location:passwordReseted.php?message=" . $message);

            } 
            else {
                echo 'User not found';
            }
        }
    }
    else {
        $mesaj = "Passwords do not match";
    }
}
?>



    <html>
           <head>
               <!--Başlık-->
               <title>Create new password</title>
               <!--Türkçe karakterlerin görülebilmesi için-->
               <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

               <!--CSS Sitilleri-->
               <style>

                   .password_zone
                   {
                       margin: 10px;
                   }

                   .button
                   {
                       margin: 10px;
                   }

               </style>

           </head>


            <body>
                <h1>Create new password</h1>

                <?php
                    if (!empty($message)) {
                        echo "</br>" . $message. "</br>";
                    }
                ?>

            <!--Form-->
            <form method="POST" action="<?php $_SERVER['PHP_SELF'];?>">
            <div><input type="password" name="pass_1" placeholder="New password" class="password_zone"/></div>
            <div><input type="password" name="pass_1" placeholder="Password again" class="password_zone"/></div>
            <div><input type="submit" value="Save" class="button"/></div>
            <input type="hidden" value="<?php echo $_GET['token'];?>" name="token">
            </form>

            </body>

    </html>
