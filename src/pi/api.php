    <?php

    // API to receive IP addresses from the pis

    header("Content-Type: Application/json");

    $method = $_SERVER['REQUEST_METHOD'];
    if ($method == 'GET') {
        header("HTTP/1.0 200 OK");
        $response = array();
        $response['status'] = "success";
        $response['message'] = "pis' IP receiver";
        print json_encode($response);
    } elseif ($method=="POST") {
        $body = explode("_", file_get_contents('php://input'));

        // pi number
        $no = $body[0];

        $file = fopen($no . ".txt", "w");
        foreach ($body as $value){
            fwrite($file, $value);
        }
        header("HTTP/1.0 200 OK");
        $response = array();
        $response['status'] = "success";
        $response['message'] = $no . " ip updated";
        print json_encode($response);
    } else {
    header("http/1.1 405 invalid method");
    }

    ?>
