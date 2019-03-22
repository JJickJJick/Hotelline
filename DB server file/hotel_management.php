<?php
require_once 'update_hotel_info.php';
$db = new update_hotel_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['hotel_owner'])) {

    // receiving the post params
    $hotel_owner = $_POST['hotel_owner'];

    // get the hotel by email and password
    $hotel = $db->VerifyHotelAuthentication($hotel_owner);

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