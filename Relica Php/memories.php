<?php 

$returnArray=array();

if (empty($_REQUEST["id"]) || empty($_REQUEST["uuid"]) || empty($_REQUEST["request_type"])) {
    $returnArray["status"] = "203";
    $returnArray["message"] = "Missing required information.";
    echo json_encode($returnArray);
    return;
}

$id = htmlentities($_REQUEST["id"]);
$uuid= htmlentities($_REQUEST["uuid"]);
$request_type= htmlentities($_REQUEST["request_type"]);

//Connection to database
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


$path = "http://10.0.2.2/Relica/memories/" . $id . "/memory-" . $uuid . ".jpg"; // ...Relica/Memories/12/memory-9029fb70-c584-439f-8617-45e17109ddf5.jpg

switch ($request_type) {
	case '1':
		//Only picture
	      $returnArray=uploadImage();

        if ($returnArray["status"]=="200") {
          $access->memory_save_picture($id, $uuid,$path);
        }
	     
	break;

	case '2':
	//Only text
	$text= htmlentities($_REQUEST["text"]);
	$access->tweet_kaydet_text($id, $uuid, $text);
	$returnArray["status"] = "200";
      	$returnArray["message"] = "Memory saved successfully.";
	break;

	case '3':
	//Both text and picture
    	$text= htmlentities($_REQUEST["text"]);
    	$returnArray=uploadImage();
    
    	if ($returnArray["status"]=="200") {
      	    $access->memory_save_text_picture($id, $uuid, $text, $path);
        }
	break;
}

function uploadImage()
{
       $picture= htmlentities($_REQUEST["c"]);
       $id = htmlentities($_REQUEST["id"]);
       $uuid= htmlentities($_REQUEST["uuid"]);
       $returnArray=array();
	   
       // Crate folder named id for user.
       $folder = "C:/xampp/htdocs/Relica/memories/" . $id;

       // If it does not exist, create
       if (!file_exists($folder)) {
           mkdir($folder, 0777,true);
       }

       
       $resim_yukleme_yolu="memories/" . $id. "/memory-" . $uuid . ".jpg";

       //Upload profile picture
       $result=file_put_contents($resim_yukleme_yolu,base64_decode($picture));

        if ($result) {
            $returnArray["status"] = "200";
            $returnArray["message"] = "Memory image has been successfully uploaded.";	
        }
	else{
	    $returnArray["status"] = "300";
            $returnArray["message"] = "Memory image couldn't upload."; 
        }
        return $returnArray;

}

$access->disconnect();

echo json_encode($returnArray);

 ?>
