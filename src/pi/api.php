<?php
header("Content-Type: Application/json");
function getJson() {
   $jsonStringIn = file_get_contents('php://input');
   $json = array();
   $response = array();
   try {
      $json = json_decode($jsonStringIn,true);
      return $json;
   }
   catch (Exception $e) {
      header("HTTP/1.0 500 Invalid content -> probably invalid JSON format");
      $response['status'] = "fail";
      $response['message'] = $e->getMessage();
      print json_encode($response);
      exit;
   }
}

$method = $_SERVER['REQUEST_METHOD'];
if ($method == 'GET') {
    header("HTTP/1.0 200 OK")
    $response = array();
    $response['status'] = "success";
    $response['message'] = "pis' IP receiver";
    print json_encode($response);
} elseif ($method=="POST") {
   $body = getJson();
   $no = $body['no']
   $file = fopen($no + ".txt", "w");
   fwrite($file, $body['ip'])
   header("HTTP/1.0 200 OK")
    $response = array();
    $response['status'] = "success";
    $response['message'] = $no + " ip updated";
    print json_encode($response);
} else {
   header("http/1.1 405 invalid method");
}



//print($path);

    

 
?>
