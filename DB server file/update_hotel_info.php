<?php

class update_hotel_info {

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
     * returns hotel details
     */
    public function StoreHotelInfo($hotel_name, $hotel_owner, $hotel_local) {
        $stmt = $this->conn->prepare("INSERT INTO HOTEL (NAME, OWNER, HLOCAL) VALUES(?, ?, ?)");
        $stmt->bind_param("sis", $hotel_name, $hotel_owner, $hotel_local);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT NAME, OWNER, HLOCAL FROM HOTEL WHERE NAME = ?");
            $stmt->bind_param("s", $hotel_name);
            $stmt->execute();
            $stmt-> bind_result($token2,$token3,$token4);
            while ( $stmt-> fetch() ) {
               $hotel["hotel_name"] = $token2;
               $hotel["hotel_owner"] = $token3;
               $hotel["hotel_local"] = $token4;
			   }
            $stmt->close();
            return $hotel;
        } else {
          return false;
        }
    }

    /**
     * Get hotel by email and password
     */
    public function VerifyHotelAuthentication($hotel_owner) {

        $stmt = $this->conn->prepare("
            SELECT HOTEL.HID, NAME, OWNER, HLOCAL, COALESCE(AVG(RESERVE.SCORE),0)
            FROM HOTEL LEFT OUTER JOIN RESERVE
            ON HOTEL.HID = RESERVE.HID
            WHERE HOTEL.OWNER = ?
            GROUP BY HOTEL.NAME
        ");
        $hotel = array(); 
        $stmt->bind_param("i", $hotel_owner);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3,$token4,$token5);
            while ( $stmt-> fetch() ) {
                $temp=["hotel_id"=>$token1, "hotel_name"=>$token2, "hotel_local"=>$token4, "hotel_score"=>$token5];
                array_push($hotel,$temp);               
            }
            $stmt->close();
            return $hotel;
        }
    }

    public function GetHotelList($head_cnt, $hotel_local, $start_date, $end_date) {
        $stmt = $this->conn->prepare("
		SELECT HOTEL.HID, HOTEL.NAME, ROOM.VALUE, COALESCE(RESERVE.SCORE,0)
		FROM HOTEL INNER JOIN ROOM
		ON HOTEL.HID = ROOM.HID	
		AND ROOM.VALUE IN(
			SELECT MIN(ROOM.VALUE) FROM HOTEL, ROOM 
			WHERE HOTEL.HID = ROOM.HID
			AND ROOM.RID IN(
				SELECT ROOM.RID
				FROM HOTEL, ROOM
				WHERE HOTEL.HID = ROOM.HID
				AND HOTEL.HLOCAL = ?
				AND ROOM.LIMITCNT >= ?
				AND ROOM.RID NOT IN(
					SELECT RESERVE.RID 
					FROM RESERVE 
					WHERE RESERVE.STARTDATE BETWEEN ? AND ?
					OR RESERVE.ENDDATE BETWEEN ? AND ?
					OR RESERVE.STARTDATE <= ? AND RESERVE.ENDDATE >= ?
				)
			)
			GROUP BY HOTEL.NAME
		)
		LEFT OUTER JOIN RESERVE
		ON HOTEL.HID = RESERVE.HID
		AND RESERVE.SCORE IN(
			SELECT COALESCE(AVG(RESERVE.SCORE),0)
			FROM HOTEL LEFT OUTER JOIN RESERVE
			ON HOTEL.HID = RESERVE.HID
			GROUP BY HOTEL.HID
		)	
        ORDER BY ROOM.VALUE
		");
        $hotel = array(); 
        $stmt->bind_param("sissssss", $hotel_local, $head_cnt, $start_date, $end_date, $start_date, $end_date, $start_date, $end_date);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3,$token4);
            while ( $stmt-> fetch() ) {
                $temp=["hotel_id"=>$token1, "hotel_name"=>$token2, "room_value"=>$token3, "hotel_score"=>$token4];
                array_push($hotel,$temp);               
            }
            $stmt->close();
            return $hotel;
        }
    }

    /**
     * Check hotel is existed or not
     */
    public function CheckExistingHotel($hotel_name) {
        $stmt = $this->conn->prepare("SELECT NAME from HOTEL WHERE NAME = ?");

        $stmt->bind_param("s", $hotel_name);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // hotel existed 
            $stmt->close();
            return true;
        } else {
            // hotel not existed
            $stmt->close();
            return false;
        }
    }

    public function ReserveHotel($member_id, $hotel_id, $room_id, $head_cnt, $start_date, $end_date, $room_value, $res_date) {
        $stmt = $this->conn->prepare("INSERT INTO RESERVE (ID, HID, RID, HEADCNT, STARTDATE, ENDDATE, VALUE, RDATE, TYPE) VALUES(?, ?, ?, ?, ?, ?, ?, ?, 'hotel')");
        $stmt->bind_param("iiiissis", $member_id, $hotel_id, $room_id, $head_cnt, $start_date, $end_date, $room_value, $res_date);
        $result = $stmt->execute();
        $stmt->close();
        
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT ID, HID, RID, HEADCNT, STARTDATE, ENDDATE, VALUE, RDATE FROM RESERVE WHERE ID = ? AND HID = ? AND RID = ? AND HEADCNT = ? AND STARTDATE = ? AND ENDDATE = ? AND VALUE = ? AND RDATE = ?");
            $stmt->bind_param("iiiissis", $member_id, $hotel_id, $room_id, $head_cnt, $start_date, $end_date, $room_value, $res_date);
            $stmt->execute();
            $stmt-> bind_result($token1,$token2,$token3,$token4,$token5,$token6,$token7,$token8);
            while ( $stmt-> fetch() ) {
               $hotel["member_id"] = $token1;
               $hotel["hotel_id"] = $token2;
               $hotel["room_id"] = $token3;
               $hotel["head_cnt"] = $token4;
               $hotel["start_date"] = $token5;
               $hotel["end_date"] = $token6;
               $hotel["room_value"] = $token7;
               $hotel["res_date"] = $token8;
			   }
            $stmt->close();
            return $hotel;
        } else {
          return false;
        }
    }

    public function GetReserveList($member) {
        $stmt = $this->conn->prepare("
		SELECT RESERVE.RESID, RESERVE.TYPE, 
        COALESCE(RESERVE.HID,''), COALESCE(HOTEL.NAME,''), COALESCE(RESERVE.RID,''), COALESCE(ROOM.NAME,''), 
        COALESCE(RESERVE.AID,''), COALESCE(AIRLINE.NAME,''), COALESCE(RESERVE.FID,''),
        RESERVE.HEADCNT, RESERVE.STARTDATE, RESERVE.ENDDATE, RESERVE.RDATE, COALESCE(RESERVE.SCORE,0), RESERVE.VALUE, COALESCE(AIRLINE.CODE,0)
        FROM RESERVE LEFT OUTER JOIN HOTEL
        ON RESERVE.HID = HOTEL.HID
        LEFT OUTER JOIN ROOM
        ON RESERVE.RID = ROOM.RID
        LEFT OUTER JOIN AIRLINE
        ON RESERVE.AID = AIRLINE.AID
        WHERE ID = ?
		");
        $reserve = array(); 
        $stmt->bind_param("i", $member);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3,$token4,$token5,
                                $token6,$token7,$token8,$token9,$token10,
                                $token11,$token12,$token13,$token14,$token15,$token16);
            while ( $stmt-> fetch() ) {
                $temp=["reserve_id"=>$token1, 
                    "product_type"=>$token2, 
                    "hotel_id"=>$token3, 
                    "hotel_name"=>$token4, 
                    "room_id"=>$token5, 
                    "room_name"=>$token6, 
                    "airline_id"=>$token7, 
                    "airline_name"=>$token8, 
                    "flight_id"=>$token9, 
                    "head_cnt"=>$token10, 
                    "start_date"=>$token11, 
                    "end_date"=>$token12, 
                    "reserve_date"=>$token13, 
                    "product_score"=>$token14, 
                    "product_value"=>$token15, 
                    "airline_code"=>$token6];
                array_push($reserve,$temp);               
            }
            $stmt->close();
            return $reserve;
        }
    }

    public function UpdateHotel($hotel_name, $hotel_id, $hotel_local) {
        $stmt = $this->conn->prepare("UPDATE HOTEL SET HOTEL.NAME = ?, HOTEL.HLOCAL = ? WHERE HOTEL.HID = ?");
        $stmt->bind_param("ssi", $hotel_name, $hotel_local, $hotel_id);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
          return true;            
        } else {
          return false;
        }
    }

    public function UpdateScore($reserve_id, $reserve_score) {
        $stmt = $this->conn->prepare("UPDATE RESERVE SET RESERVE.SCORE = ? WHERE RESID = ?");
        $stmt->bind_param("ii", $reserve_score, $reserve_id);
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