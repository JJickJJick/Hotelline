<?php

require_once 'update_airline_info.php';
$db = new update_airline_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['airline_name']) && isset($_POST['airline_id']) && isset($_POST['airline_code'])) {

    // receiving the post params
    $airline_name = $_POST['airline_name'];
    $airline_id = $_POST['airline_id'];
    $airline_code = $_POST['airline_code'];

    
        $airline = $db->UpdateAirline($airline_name, $airline_id, $airline_code);
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
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Not enough information";
    echo json_encode($response);
}
?>