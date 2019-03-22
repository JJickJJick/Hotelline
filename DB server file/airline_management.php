<?php
require_once 'update_airline_info.php';
$db = new update_airline_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['airline_owner'])) {

    // receiving the post params
    $airline_owner = $_POST['airline_owner'];

    // get the airline by email and password
    $airline = $db->VerifyAirlineAuthentication($airline_owner);

    if ($airline != false) {
        // use is found
        $response["error"] = FALSE;
        $response["airline"] = $airline;
        echo json_encode($response);
    } else {
        // airline is not found with the credentials
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