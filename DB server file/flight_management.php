<?php
require_once 'update_flight_info.php';
$db = new update_flight_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['airline_id'])) {

    // receiving the post params
    $airline_id = $_POST['airline_id'];

    // get the flight by email and password
    $flight = $db->VerifyFlightAuthentication($airline_id);

    if ($flight != false) {
        // use is found
        $response["error"] = FALSE;
        $response["flight"] = $flight;
        echo json_encode($response);
    } else {
        // flight is not found with the credentials
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