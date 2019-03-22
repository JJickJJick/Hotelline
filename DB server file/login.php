<?php
require_once 'update_user_info.php';
$db = new update_user_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email']) && isset($_POST['password'])) {

    // receiving the post params
    $email = $_POST['email'];
    $password = $_POST['password'];

    // get the user by email and password
    $user = $db->VerifyUserAuthentication($email, $password);

    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["user"] = $user;
        echo json_encode($response);
    } else {
        // user is not found with the credentials
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