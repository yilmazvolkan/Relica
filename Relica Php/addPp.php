<?php 

$returnArray=array();

if (empty($_REQUEST["id"]) || empty($_REQUEST["profile"])) {
    $returnArray["message"] = "Required information is missing.";
    $returnArray["status"] = "203";
    echo json_encode($returnArray);
    return;
}

$profile = htmlentities($_REQUEST["profile"]);
$id = htmlentities($_REQUEST["id"]);




// Creating id folder path for users for Windows
$folder = "C:/xampp/htdocs/Relica/users/" . $id;


// If it does not exist, create
if (!file_exists($folder)) {
    mkdir($folder, 0777,true);
}


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


// Save path to database
$path = "http://10.0.2.2/Relica/users/" . $id . "/profile.jpg";
$resim_yukleme_yolu="users/" . $id. "/profile.jpg";

// Save path of image to database
$access->updateProfileFotoPath($path, $id);


// Upload the profile photo
$result=file_put_contents($resim_yukleme_yolu,base64_decode($profile));

if ($result) {
$returnArray["status"] = "200";
$returnArray["message"] = "Profile picture has been successfully updated.";	
}else{
	 $returnArray["status"] = "300";
     $returnArray["message"] = "There was an error while loading the profile image.";
}


// After update, new user info
$user = $access->selectUserIdyeGore($id);

$returnArray["id"] = $user["id"];
$returnArray["username"] = $user["username"];
$returnArray["fullname"] = $user["fullname"];
$returnArray["mail"] = $user["mail"];
$returnArray["avatar"] = $user["avatar"];



// Disconnection
$access->disconnect();


// Return backforward array
echo json_encode($returnArray);

 ?>
