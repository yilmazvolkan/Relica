<?php  

$returnArray=array();
if (empty($_REQUEST["id"])) {
    $returnArray["status"] = "203";
    $returnArray["message"] = "Required information is missing.";
    echo json_encode($returnArray);
    return;
}

$id = htmlentities($_REQUEST["id"]);

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


// initialize word
$word = null;

if (!empty($_REQUEST["word"])) {
    $word = htmlentities($_REQUEST["word"]);
}

$users = $access->selectUsers($word, $id);


if (!empty($users)) {
    $returnArray["users"] = $users;
    $returnArray["status"] = "200";
    $returnArray["message"] = "Success";

} else {
	$returnArray["status"] = "204";
  $returnArray["message"] = 'No Records Found';
}

$access->disconnect();

echo json_encode($returnArray);
?>
