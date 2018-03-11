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
}

?>
