<?php

class update_airline_info {

    private $conn;

    // constructor
    function __construct() {
        require_once 'android_login_connect.php';
        // connecting to database
        $db = new android_login_connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {

    }

    /**
     * Storing new AIRLINE
     * returns airline details
     */
    public function StoreAirlineInfo($airline_name, $airline_owner, $airline_code) {
        $stmt = $this->conn->prepare("INSERT INTO AIRLINE (NAME, OWNER, CODE) VALUES(?, ?, ?)");
        $stmt->bind_param("sis", $airline_name, $airline_owner, $airline_code);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT NAME FROM AIRLINE WHERE NAME = ?");
            $stmt->bind_param("s", $airline_name);
            $stmt->execute();
            $stmt-> bind_result($token2);
            while ( $stmt-> fetch() ) {
               $airline["airline_name"] = $token2;
			   }
            $stmt->close();
            return $airline;
        } else {
          return false;
        }
    }

    /**
     * Get airline by email and password
     */
    public function VerifyAirlineAuthentication($member_id) {

        $stmt = $this->conn->prepare("
        SELECT AIRLINE.AID, NAME, AIRLINE.CODE
        FROM AIRLINE
        WHERE AIRLINE.OWNER = ?
        ");
        $airline = array(); 
        $stmt->bind_param("i", $member_id);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3);
            while ( $stmt-> fetch() ) {
                $temp=["airline_id"=>$token1, "airline_name"=>$token2, "airline_code"=>$token3];
                array_push($airline,$temp);               
            }
            $stmt->close();
            return $airline;
        }
    }

    /**
     * Check airline is existed or not
     */
    public function CheckExistingAirline($airline_name, $airline_code) {
        $stmt = $this->conn->prepare("SELECT NAME from AIRLINE WHERE NAME = ? OR CODE = ?");

        $stmt->bind_param("ss", $airline_name, $airline_code);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // airline existed 
            $stmt->close();
            return true;
        } else {
            // airline not existed
            $stmt->close();
            return false;
        }
    }

    public function UpdateAirline($airline_name, $airline_id, $airline_code) {
        $stmt = $this->conn->prepare("UPDATE AIRLINE SET AIRLINE.NAME = ?, AIRLINE.CODE = ? WHERE AIRLINE.AID = ?");
        $stmt->bind_param("ssi", $airline_name, $airline_code, $airline_id);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            return true;
        } else {
          return false;
        }
    }
}

?>