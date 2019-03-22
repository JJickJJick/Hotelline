<?php

require_once 'update_hotel_info.php';
$db = new update_hotel_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['hotel_name']) && isset($_POST['hotel_id']) && isset($_POST['hotel_local'])) {

    // receiving the post params
    $hotel_name = $_POST['hotel_name'];
    $hotel_id = $_POST['hotel_id'];
    $hotel_local = $_POST['hotel_local'];

    
        $hotel = $db->UpdateHotel($hotel_name, $hotel_id, $hotel_local);
        if ($hotel) {
            // hotel stored successfully
            $response["error"] = FALSE;
            echo json_encode($response);
        } else {
            // hotel failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unkwown Error";
            echo json_encode($response);
        }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Not enough information";
    echo json_encode($response);
}
?>