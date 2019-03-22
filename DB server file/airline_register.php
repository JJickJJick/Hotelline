<?php

require_once 'update_airline_info.php';
$db = new update_airline_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['airline_name']) && isset($_POST['airline_owner']) && isset($_POST['airline_code'])) {

    // receiving the post params
    $airline_name = $_POST['airline_name'];
    $airline_owner = $_POST['airline_owner'];
    $airline_code = $_POST['airline_code'];

    // check if airline is already existed with the same name
    if ($db->CheckExistingAirline($airline_name, $airline_code)) {
        // airline already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Airline already existed";
        echo json_encode($response);
    } else {
        // create a new airline
        $airline = $db->StoreAirlineInfo($airline_name, $airline_owner, $airline_code);
        if ($airline) {
            // airline stored successfully
            $response["error"] = FALSE;
            echo json_encode($response);
        } else {
            // airline failed to store
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