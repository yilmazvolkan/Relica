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
}

?>
