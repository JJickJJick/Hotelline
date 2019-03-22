<?php
require_once 'update_room_info.php';
$db = new update_room_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['hotel_id'])) {

    // receiving the post params
    $hotel_id = $_POST['hotel_id'];

    // get the room by email and password
    $room = $db->VerifyRoomAuthentication($hotel_id);

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