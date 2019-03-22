<?php
require_once 'update_hotel_info.php';
$db = new update_hotel_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['head_cnt']) && isset($_POST['hotel_local']) && isset($_POST['start_date']) && isset($_POST['end_date'])) {

    // receiving the post params
    $head_cnt = $_POST['head_cnt'];
    $hotel_local = $_POST['hotel_local'];
    $start_date = $_POST['start_date'];
    $end_date = $_POST['end_date'];

    // get the hotel by email and password
    $hotel = $db->GetHotelList($head_cnt, $hotel_local, $start_date, $end_date);

    if ($hotel != false) {
        // use is found
        $response["error"] = FALSE;
        $response["hotel"] = $hotel;
        echo json_encode($response);
    } else {
        // hotel is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "No Information in Database";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Parameter missing";
    echo json_encode($response);
}
?>