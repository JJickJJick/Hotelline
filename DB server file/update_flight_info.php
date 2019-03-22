<?php

class update_flight_info {

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
     * returns flight details
     */
    public function StoreFlightInfo($airline_id, $flight_dept, $flight_dest, $flight_dept_time, $flight_fly_time, $flight_limit_cnt, $flight_value) {
        $stmt = $this->conn->prepare("INSERT INTO FLIGHT (AID, DEPTLOCAL, DESTLOCAL, DEPTTIME, FLYTIME, LIMITCNT, VALUE) VALUES (?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("issssii", $airline_id, $flight_dept, $flight_dest, $flight_dept_time, $flight_fly_time, $flight_limit_cnt, $flight_value);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get flight by email and password
     */
    public function VerifyFlightAuthentication($airline_id) {

        $stmt = $this->conn->prepare("
        SELECT FLIGHT.FID, FLIGHT.DEPTLOCAL, FLIGHT.DESTLOCAL, FLIGHT.DEPTTIME, FLIGHT.FLYTIME, COALESCE(AVG(RESERVE.SCORE),0), FLIGHT.VALUE, FLIGHT.LIMITCNT, FLIGHT.FLYTIME
        FROM FLIGHT LEFT OUTER JOIN RESERVE
        ON FLIGHT.FID = RESERVE.FID
        WHERE FLIGHT.AID = ?
        GROUP BY FLIGHT.FID
        ");
        $flight = array(); 
        $stmt->bind_param("i", $airline_id);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3,$token4,$token5,$token6,$token7,$token8,$token9);
            while ( $stmt-> fetch() ) {
                $temp=["flight_id"=>$token1, "flight_dept"=>$token2, "flight_dest"=>$token3, "flight_dept_time"=>$token4, 
                "flight_fly_time"=>$token5, "flight_score"=>$token6, "flight_value"=>$token7, "flight_limit_cnt"=>$token8, "flight_fly_time"=>$token9];
                array_push($flight,$temp);               
            }
            $stmt->close();
            return $flight;
        }
    }

    public function GetFlightList($flight_dept, $flight_dest, $dept_date, $head_cnt) {
        $stmt = $this->conn->prepare("
            SELECT AIRLINE.AID, AIRLINE.NAME, AIRLINE.CODE, FLIGHT.FID, 
            cast(concat( ? , ' ', FLIGHT.DEPTTIME) as datetime),
            DATE_ADD(cast(concat( ? , ' ', FLIGHT.DEPTTIME) as datetime), INTERVAL(HOUR(FLIGHT.FLYTIME)*3600 + MINUTE(FLIGHT.FLYTIME)*60 + SECOND(FLIGHT.FLYTIME))SECOND), 
            FLIGHT.LIMITCNT-COALESCE(SUM(RESERVE.HEADCNT),0) AS RESCNT, FLIGHT.VALUE,
            COALESCE(AVG(SCORE),0)
            FROM FLIGHT INNER JOIN AIRLINE
            ON FLIGHT.AID = AIRLINE.AID
            LEFT OUTER JOIN RESERVE
            ON FLIGHT.FID = RESERVE.FID
			AND Date_Format(RESERVE.STARTDATE,'%Y-%m-%d') = ?
            WHERE FLIGHT.DEPTLOCAL = ?
            AND FLIGHT.DESTLOCAL = ?
            GROUP BY FLIGHT.FID
			HAVING RESCNT >= ?
        ");
        $flight = array(); 
        $stmt->bind_param("sssssi", $dept_date, $dept_date, $dept_date, $flight_dept, $flight_dest, $head_cnt);
        if ($stmt->execute()) {
            $stmt-> bind_result($token1,$token2,$token3,$token4,$token5,$token6,$token7,$token8,$token9);
            while ( $stmt-> fetch() ) {
                $temp=["airline_id"=>$token1, 
                    "airline_name"=>$token2, 
                    "airline_code"=>$token3, 
                    "flight_id"=>$token4,
                    "flight_dept_time"=>$token5,
                    "flight_dest_time"=>$token6,
                    "flight_limit_cnt"=>$token7,
                    "flight_value"=>$token8,
                    "flight_score"=>$token9];
                array_push($flight,$temp);               
            }
            $stmt->close();
            return $flight;
        }
    }

    public function ReserveFlight($member_id, $airline_id, $flight_id, $head_cnt, $start_date, $end_date, $flight_value, $res_date) {
        $stmt = $this->conn->prepare("INSERT INTO RESERVE (ID, AID, FID, HEADCNT, STARTDATE, ENDDATE, VALUE, RDATE, TYPE) VALUES(?, ?, ?, ?, ?, ?, ?, ?, 'flight')");
        $stmt->bind_param("iiiissis", $member_id, $airline_id, $flight_id, $head_cnt, $start_date, $end_date, $flight_value, $res_date);
        $result = $stmt->execute();
        $stmt->close();
        
        // check for successful store
        if ($result) {
            return true;
        } else {
          return false;
        }
    }

    public function UpdateFlight($flight_id, $flight_dept, $flight_dest, $flight_dept_time, $flight_fly_time, $flight_limit_cnt, $flight_value) {
        $stmt = $this->conn->prepare("
        UPDATE FLIGHT SET DEPTLOCAL = ?, DESTLOCAL = ?, DEPTTIME = ?, FLYTIME = ?, LIMITCNT = ?, FLIGHT.VALUE = ? WHERE FID = ?
        ");
        $stmt->bind_param("sssssss", $flight_dept, $flight_dest, $flight_dept_time, $flight_fly_time, $flight_limit_cnt, $flight_value, $flight_id);
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