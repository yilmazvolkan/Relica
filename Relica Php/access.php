<?php
/**
 * Created by PhpStorm.
 */

class access{

    private $host=null;
    private $user=null;
    private $pass=null;
    private $name=null;
    private $conn=null;

    function __construct($dbhost,$dbuser,$dbpass,$dbname)
    {
        $this->host=$dbhost;
        $this->user=$dbuser;
        $this->pass=$dbpass;
        $this->name=$dbname;
    }

    function connect(){
       $this->conn=new mysqli($this->host,$this->user,$this->pass,$this->name);
       $this->conn->set_charset("utf-8");

        if (!$this->conn)
            //echo "Connection failed.";
            return false;

        return true;
    }
    function disconnect(){

        if ($this->conn!=null)
            $this->conn->close();
    }
    // Register of users
    public function registerUser($username, $safe_password, $salt, $mail, $fullname)
    {
        // SQL query
        $sql = "INSERT INTO kisiler SET username=?, password=?, salt=?, mail=?, fullname=?";

        // Preperation for result of query
        $statement = $this->conn->prepare($sql);

        // Throw error
        if (!$statement) {
            throw new Exception($statement->error);
        }

        // Attach values for SQL
        $statement->bind_param("sssss", $username, $safe_password, $salt, $mail, $fullname);

        // Run query
        $returnValue = $statement->execute();

        return $returnValue;

    }

    // Get user infos
    public function selectUser($username)
    {
        $returArray = array();

        // SQL query
        $sql = "SELECT * FROM users WHERE username='".$username."'";

        // Result of SQL query
        $result = $this->conn->query($sql);

        // If there it is not null
        if ($result != null && (mysqli_num_rows($result) >= 1 )) {

            // Store associated row at $row 
            // MYSQLI_ASSOC => Index values are column names at database
            $row = $result->fetch_array(MYSQLI_ASSOC);

            if (!empty($row)) {
                $returArray = $row;
            }

        }

        return $returArray;
    }
    public function saveToken($token, $id)
    {
        // sql query
        $sql = "INSERT INTO emailTokens SET id=?, token=?";

        // preparation for result of query
        $statement = $this->conn->prepare($sql);

        // error
        if (!$statement) {
            throw new Exception($statement->error);
        }

        // attach values for sql 
        $statement->bind_param("is", $id, $token);

        // run query
        $returnValue = $statement->execute();

        return $returnValue;
    }
    
     // Get username by token
    function getUserId($token) {

        $returnArray = array();

        // sql query
        $sql = "SELECT id FROM emailTokens WHERE token = '".$token."'";

        // Preparation of results of query
        $result = $this->conn->query($sql);

        // If result is non-empty
        if ($result != null && (mysqli_num_rows($result) >= 1)) {

            // Transform result to assoc array and save at $row 
            $row = $result->fetch_array(MYSQLI_ASSOC);

            if (!empty($row)) {
                $returnArray = $row;
            }
        }

        return $returnArray;

    }

    //emailonay  durumunu değiştir
    function changeStatusEmail($id,$status) {

        $sql = "UPDATE users SET emailVerification=? WHERE id=?";
        $statement = $this->conn->prepare($sql);

        if (!$statement) {
            throw new Exception($statement->error);
        }

        $statement->bind_param("ii",$status, $id);

        $returnValue = $statement->execute();

        return $returnValue;

    }


    // After email verification, tokon will be deleted
    function deleteToken($token) {

        $sql = "DELETE FROM emailTokens WHERE token=?";
        $statement = $this->conn->prepare($sql);

        if (!$statement) {
            throw new Exception($statement->error);
        }
        $statement->bind_param("s", $token);

        $returnValue = $statement->execute();

        return $returnValue;

    }


    // Profile picture path is saved on database
    function updateProfileFotoPath($path, $id) {

        // sql instruction
        $sql = "UPDATE users SET avatar=? WHERE id=?";

        // preparation for result of query
        $statement = $this->conn->prepare($sql);

        // error case
        if (!$statement) {
            throw new Exception($statement->error);
        }

        // connect parameters to sql
        $statement->bind_param("si", $path, $id);

        // add return
        $returnValue = $statement->execute();

        return $returnValue;

    }


    // Get user infos according to Id
    public function selectUserIdyeGore($id) {

        $returnArray = array();

        // sql instruction
        $sql = "SELECT * FROM kisiler WHERE id='".$id."'";

        //  add outcome to $result
        $result = $this->conn->query($sql);

        // conditions
        if ($result != null && (mysqli_num_rows($result) >= 1 )) {

            // Transform result to assoc array and save at $row 
            $row = $result->fetch_array(MYSQLI_ASSOC);
            
            if (!empty($row)) {
                $returnArray = $row;
            }

        }

        return $returnArray;

    }
    // Insert memory only picture
    public function memory_save_picture($id, $uuid, $path) { //
        
        // sql query
        $sql = "INSERT INTO memories SET id=?, uuid=?, path=?";
        
        // preparation
        $statement = $this->conn->prepare($sql);

        // catch error
        if (!$statement) {
            throw new Exception($statement->error);
        }

        // enter parameters
        $statement->bind_param("iss", $id, $uuid, $path);

        // run query and add result to $returnValue 
        $returnValue = $statement->execute();

        return $returnValue;

    }
    // Insert memory text ve picture
    public function memory_save_text_picture($id, $uuid, $text, $path){  //
        
        // sql query
        $sql = "INSERT INTO memories SET id=?, uuid=?, text=?, path=?";
        
        // preparation
        $statement = $this->conn->prepare($sql);

        // catch error
        if (!$statement) {
            throw new Exception($statement->error);
        }

        // enter parameters
        $statement->bind_param("isss", $id, $uuid, $text, $path);

        // run query and add result to $returnValue
        $returnValue = $statement->execute();

        return $returnValue;

    }

}

?>
