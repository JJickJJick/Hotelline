<?php

require_once 'update_room_info.php';
$db = new update_room_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['room_name']) && isset($_POST['hotel_id']) && isset($_POST['limit_cnt']) && isset($_POST['product_value'])) {

    // receiving the post params
    $room_name = $_POST['room_name'];
    $hotel_id = $_POST['hotel_id'];
    $limit_cnt = $_POST['limit_cnt'];
    $product_value = $_POST['product_value'];

    // check if room is already existed with the same name
    if ($db->CheckExistingRoom($room_name)) {
        // room already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Room already existed";
        echo json_encode($response);
    } else {
        // create a new room
        $room = $db->StoreRoomInfo($room_name, $hotel_id, $limit_cnt,$product_value);
        if ($room) {
            // room stored successfully
            $response["error"] = FALSE;
            $response["room"] = $room;
            echo json_encode($response);
        } else {
            // room failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unkwown Error";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Not enough information";
    echo json_encode($response);
}
?>