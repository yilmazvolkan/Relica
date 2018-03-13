<?php 

$returnArray=array();

if (empty($_REQUEST["uuid"])) {
    $returnArray["status"] = "203";
    $returnArray["message"] = "Required information is missing.";
    echo json_encode($returnArray);
    return;
}

$uuid= htmlentities($_REQUEST["uuid"]);

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


$result = $access->deleteMemory($uuid);

if ($result==1){
    $returnArray["status"] = "200";
    $returnArray["message"] = "Memory has been deleted!";

    if(!empty($_REQUEST["path"])){
      
      $path = htmlentities($_REQUEST["path"]);

      // Edit folder path for image

      $path = str_replace("http://10.0.2.2/", "C:/xampp/htdocs/", $path)

      // If delete operation is successful
      if (unlink($path)) {
        $returnArray["message"] = "Memory has been deleted!";
      }
      else{
        $returnArray["status"] = "404";
        $returnArray["message"] = "Memory is not deleted.";
      }

    }
}
else {
  $returnArray["status"] = "404";
  $returnArray["message"] = "Memory is not deleted.";
}


$access->disconnect();

echo json_encode($returnArray);

?>
