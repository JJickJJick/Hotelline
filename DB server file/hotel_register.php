<?php

require_once 'update_hotel_info.php';
$db = new update_hotel_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['hotel_name']) && isset($_POST['hotel_owner']) && isset($_POST['hotel_local'])) {

    // receiving the post params
    $hotel_name = $_POST['hotel_name'];
    $hotel_owner = $_POST['hotel_owner'];
    $hotel_local = $_POST['hotel_local'];

    // check if hotel is already existed with the same name
    if ($db->CheckExistingHotel($hotel_name)) {
        // hotel already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Hotel already existed";
        echo json_encode($response);
    } else {
        // create a new hotel
        $hotel = $db->StoreHotelInfo($hotel_name, $hotel_owner, $hotel_local);
        if ($hotel) {
            // hotel stored successfully
            $response["error"] = FALSE;
            $response["hotel"] = $hotel;
            echo json_encode($response);
        } else {
            // hotel failed to store
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