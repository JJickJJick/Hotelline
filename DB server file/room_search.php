<?php
require_once 'update_room_info.php';
$db = new update_room_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['hotel_id']) && isset($_POST['head_cnt']) && isset($_POST['start_date']) && isset($_POST['end_date'])) {

    // receiving the post params
    $hotel_id = $_POST['hotel_id'];
    $head_cnt = $_POST['head_cnt'];
    $start_date = $_POST['start_date'];
    $end_date = $_POST['end_date'];

    // get the room by email and password
    $room = $db->GetRoomList($hotel_id, $head_cnt, $start_date, $end_date);

    if ($room != false) {
        // use is found
        $response["error"] = FALSE;
        $response["room"] = $room;
        echo json_encode($response);
    } else {
        // room is not found with the credentials
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