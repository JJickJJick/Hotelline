<?php

require_once 'update_hotel_info.php';
$db = new update_hotel_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['reserve_id']) && isset($_POST['reserve_score'])) {

    // receiving the post params
    $reserve_id = $_POST['reserve_id'];
    $reserve_score = $_POST['reserve_score'];

    $reserve = $db->UpdateScore($reserve_id, $reserve_score);
    if ($reserve) {
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