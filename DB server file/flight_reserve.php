<?php

require_once 'update_flight_info.php';
$db = new update_flight_info();

// json response array
$response = array("error" => FALSE);
if (isset($_POST['id']) && isset($_POST['aid']) && isset($_POST['fid']) && isset($_POST['headcnt']) && isset($_POST['startdate']) && 
    isset($_POST['enddate']) && isset($_POST['value']) && isset($_POST['resdate'])) {

    // receiving the post params
    $member_id = $_POST['id'];
    $airline_id = $_POST['aid'];
    $flight_id = $_POST['fid'];
    $head_cnt = $_POST['headcnt'];
    $start_date = $_POST['startdate'];
    $end_date = $_POST['enddate'];
    $flight_value = $_POST['value'];
    $res_date = $_POST['resdate'];

    $flight = $db->ReserveFlight($member_id, $airline_id, $flight_id, $head_cnt, $start_date, $end_date, $flight_value, $res_date);
    $response["error"] = FALSE;
    echo json_encode($response);
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = $_POST['rid'];
    echo json_encode($response);
}
?>