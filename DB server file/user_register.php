<?php

require_once 'update_user_info.php';
$db = new update_user_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['type']) && isset($_POST['tel'])) {

    // receiving the post params
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $type = $_POST['type'];
    $tel = $_POST['tel'];

    // check if user is already existed with the same email
    if ($db->CheckExistingUser($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "이미 존재하는 Email 입니다." . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->StoreUserInfo($name, $email, $password, $type, $tel);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["type"] = $user["type"];
            $response["user"]["tel"] = $user["tel"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown Error";
            echo json_encode($response);
        }
}
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Parameter Missing";
    echo json_encode($response);
}
?>