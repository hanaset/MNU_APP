<?php
	header("content-Type: text/html;charset=UTF-8");

	$conn = new mysqli("114.70.93.130","root","wjdqls56","mnu");

	if($conn->connect_error){
		die("Connection failed: ".$conn->connect_error);
	}

	$date = $_POST['date'];
	$day = $_POST['day'];
	$location = $_POST['location'];

	$sql = "SELECT *FROM grade WHERE date ='".$date."' && day = '".$day."' && location = '".$location."'";

	$result = $conn->query($sql);

	$text = array();

	if($result->num_rows > 0){
		while($row = $result->fetch_assoc()){
			array_push($text, array('content'=>$row["content"], 'score'=>$row["score"]));
		}
	}

	$json = json_encode(array("result"=>$text));

	if($result){
		echo $json;	
	}else{
		echo "-1";
	}
	


	$conn->close();

?>