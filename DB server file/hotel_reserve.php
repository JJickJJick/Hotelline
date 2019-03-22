<?php

require_once 'update_hotel_info.php';
$db = new update_hotel_info();

// json response array
$response = array("error" => FALSE);
if (isset($_POST['id']) && isset($_POST['hid']) && isset($_POST['rid']) && isset($_POST['headcnt']) && isset($_POST['startdate']) && isset($_POST['enddate']) && isset($_POST['value']) && isset($_POST['resdate'])) {

    // receiving the post params
    $member_id = $_POST['id'];
    $hotel_id = $_POST['hid'];
    $room_id = $_POST['rid'];
    $head_cnt = $_POST['headcnt'];
    $start_date = $_POST['startdate'];
    $end_date = $_POST['enddate'];
    $room_value = $_POST['value'];
    $res_date = $_POST['resdate'];

    // check if hotel is already existed with the same name
    $hotel = $db->ReserveHotel($member_id, $hotel_id, $room_id, $head_cnt, $start_date, $end_date, $room_value, $res_date);
    $response["error"] = FALSE;
    echo json_encode($response);
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = $_POST['rid'];
    echo json_encode($response);
}
?>