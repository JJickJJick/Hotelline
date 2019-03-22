<?php

class update_user_info {

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
     * Storing new user
     * returns user details
     */
    public function StoreUserInfo($name, $email, $password, $type, $tel) {
        $hash = $this->hashFunction($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO MEMBER (NAME, EMAIL, PASSWORD, salt, TYPE, TEL) VALUES(?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssss", $name, $email, $encrypted_password, $salt, $type, $tel);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT NAME, EMAIL, PASSWORD, salt, TYPE, TEL FROM MEMBER WHERE EMAIL = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt-> bind_result($token2,$token3,$token4,$token5,$token6,$token7);
            while ( $stmt-> fetch() ) {
               $user["name"] = $token2;
               $user["email"] = $token3;
               $user["type"] = $token6;
               $user["tel"] = $token7;
            }
            $stmt->close();
            return $user;
        } else {
          return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function VerifyUserAuthentication($email, $password) {

        $stmt = $this->conn->prepare("SELECT ID, NAME, EMAIL, PASSWORD, salt, TYPE, TEL FROM MEMBER WHERE EMAIL = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $stmt-> bind_result($token2,$token3,$token4,$token5,$token6,$token7,$token8);

            while ( $stmt-> fetch() ) {
               $user["id"] = $token2;
               $user["name"] = $token3;
               $user["email"] = $token4;
               $user["encrypted_password"] = $token5;
               $user["salt"] = $token6;
               $user["type"] = $token7;
               $user["tel"] = $token8;
            }

            $stmt->close();

            // verifying user password
            $salt = $token6;
            $encrypted_password = $token5;
            $hash = $this->CheckHashFunction($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function CheckExistingUser($email) {
        $stmt = $this->conn->prepare("SELECT EMAIL from MEMBER WHERE EMAIL = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashFunction($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkHashFunction($salt, $password) {
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
        return $hash;
    }

}

?>