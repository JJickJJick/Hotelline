<?php

class update_room_info {

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
     * Storing new HOTEL
     * returns room details
     */
    public function StoreRoomInfo($room_name, $hotel_id, $limit_cnt, $product_value) {
        $stmt = $this->conn->prepare("INSERT INTO ROOM (NAME, HID, LIMITCNT, ROOM.VALUE) VALUES(?, ?, ?, ?)");
        $stmt->bind_param("siii", $room_name, $hotel_id, $limit_cnt, $product_value);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT NAME, HID, LIMITCNT FROM ROOM WHERE NAME = ?");
            $stmt->bind_param("s", $room_name);
            $stmt->execute();
            $stmt-> bind_result($token2,$token3,$token4);
            while ( $stmt-> fetch() ) {
               $room["room_name"] = $token2;
               $room["hotel_id"] = $token3;  
               $room["limit_cnt"] = $token4;
			   }
            $stmt->close();
            return $room;
        } else {
          return false;
        }
    }

    /**
     * Get room by email and password
     */
    public function VerifyRoomAuthentication($hotel_id) {

        $stmt = $this->conn->prepare("SELECT RID, NAME, LIMITCNT, VALUE FROM ROOM WHERE HID = ?");
        $room = array(); 
        $stmt->bind_param("s", $hotel_id);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3,$token4);
            while ( $stmt-> fetch() ) {
                $temp=["room_id"=>$token1, "room_name"=>$token2, "limit_cnt"=>$token3, "room_value"=>$token4];
                array_push($room,$temp);               
            }
            $stmt->close();
            return $room;
        }
    }

    public function GetRoomList($hotel_id, $head_cnt, $start_date, $end_date) {
        $stmt = $this->conn->prepare("
        SELECT ROOM.RID, ROOM.NAME, ROOM.LIMITCNT, ROOM.VALUE 
		FROM HOTEL INNER JOIN ROOM
		ON HOTEL.HID = ROOM.HID 
		LEFT OUTER JOIN RESERVE     
		ON ROOM.RID = RESERVE.RID
		AND ROOM.RID NOT IN(
			SELECT RESERVE.RID 
			FROM RESERVE 
			WHERE RESERVE.STARTDATE BETWEEN ? AND ?
			OR RESERVE.ENDDATE BETWEEN ? AND ?
			OR RESERVE.STARTDATE <= ? AND RESERVE.ENDDATE >= ?
		)
		WHERE HOTEL.HID = ?
		AND ROOM.LIMITCNT >= ?
		ORDER BY ROOM.VALUE
        ");
        $room = array(); 
        $stmt->bind_param("ssssssii", $start_date, $end_date, $start_date, $end_date, $start_date, $end_date, $hotel_id, $head_cnt);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3,$token4);
            while ( $stmt-> fetch() ) {
                $temp=["room_id"=>$token1, "room_name"=>$token2, "limit_cnt"=>$token3, "room_value"=>$token4];
                array_push($room,$temp);               
            }
            $stmt->close();
            return $room;
        }
    }

    /**
     * Check room is existed or not
     */
    public function CheckExistingRoom($room_name) {
        $stmt = $this->conn->prepare("SELECT NAME from ROOM WHERE NAME = ?");

        $stmt->bind_param("s", $room_name);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // room existed 
            $stmt->close();
            return true;
        } else {
            // room not existed
            $stmt->close();
            return false;
        }
    }

    public function UpdateRoom($room_name, $room_id, $limit_cnt,$product_value) {
        $stmt = $this->conn->prepare("UPDATE ROOM SET ROOM.NAME = ?, ROOM.LIMITCNT = ?, ROOM.VALUE = ? WHERE ROOM.RID = ?");
        $stmt->bind_param("siii", $room_name, $limit_cnt, $product_value, $room_id);
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