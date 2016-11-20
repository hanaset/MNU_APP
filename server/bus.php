<?php
	header("Content-Type: text/html;charset=UTF-8");

	$conn = new mysqli("114.70.93.130", "root", "wjdqls56", "mnu");
// Check connection
	if ($conn->connect_error) {
   		die("Connection failed: " . $conn->connect_error);
	} 



	$sql = "SELECT * FROM bus";

	$result = $conn->query($sql);

	$text = array();

	if($result->num_rows > 0){
		while($row = $result->fetch_assoc()){
			array_push($text, array('go_come'=>$row["go_come"],'time'=>$row["time"], 'location'=>$row["location"], 'start'=>$row["start"], 'route'=>$row["route"]));
		}
	

	 $json = json_encode(array("result"=>$text));

	 echo $json;
	}else{
		echo "-1";
	}

	$conn->close();
?>