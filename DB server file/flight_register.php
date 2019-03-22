<?php

require_once 'update_flight_info.php';
$db = new update_flight_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['airline_id']) && isset($_POST['flight_dept']) && isset($_POST['flight_dest']) && isset($_POST['flight_dept_time']) && isset($_POST['flight_fly_time']) && isset($_POST['flight_limit_cnt'])) {

    // receiving the post params
    $airline_id = $_POST['airline_id'];
    $flight_dept = $_POST['flight_dept'];
    $flight_dest = $_POST['flight_dest'];
    $flight_dept_time = $_POST['flight_dept_time'];
    $flight_fly_time = $_POST['flight_fly_time'];
    $flight_limit_cnt = $_POST['flight_limit_cnt'];
    $flight_value = $_POST['flight_value'];

    $flight = $db->StoreFlightInfo($airline_id, $flight_dept, $flight_dest, $flight_dept_time, $flight_fly_time, $flight_limit_cnt, $flight_value);
    if ($flight) {
        $response["error"] = FALSE;
        echo json_encode($response);
    } else {
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