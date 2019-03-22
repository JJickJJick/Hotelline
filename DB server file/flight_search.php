<?php
require_once 'update_flight_info.php';
$db = new update_flight_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['flight_dept']) && isset($_POST['flight_dest']) && isset($_POST['dept_date']) && isset($_POST['head_cnt'])) {

    // receiving the post params
    $flight_dept = $_POST['flight_dept'];
    $flight_dest = $_POST['flight_dest'];
    $dept_date = $_POST['dept_date'];
    $head_cnt = $_POST['head_cnt'];

    // get the flight by email and password
    $flight = $db->GetFlightList($flight_dept, $flight_dest, $dept_date, $head_cnt);

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