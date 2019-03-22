<?php
require_once 'update_hotel_info.php';
$db = new update_hotel_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['member_id'])) {

    // receiving the post params
    $member = $_POST['member_id'];

    // get the reserve by email and password
    $reserve = $db->GetReserveList($member);

    if ($reserve != false) {
        // use is found
        $response["error"] = FALSE;
        $response["reserve"] = $reserve;
        echo json_encode($response);
    } else {
        // reserve is not found with the credentials
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