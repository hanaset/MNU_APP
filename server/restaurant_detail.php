<?php
	header("Content-Type: text/html;charset=UTF-8");

	$mysql_username = 'root';
	$mysql_password = 'wjdqls56';
	$mysql_database = 'mnu';

	$conn = mysqli_connect("114.70.93.130",$mysql_username,$mysql_password,$mysql_database);

	if(!$conn){
		die("Connection failed: ". mysqli_connect_error());
	}

	$name = $_POST['name'];

	$sql = "SELECT * FROM restaurant Where name ='".$name."'";
	$result = mysqli_query($conn, $sql);
	$text = array();


	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result)){
			array_push($text, array('phone'=>$row["phone"],'time'=>$row["time"], 'notice'=>$row["notice"],'image'=>$row["image"]));
		}
	
	$json = json_encode(array("result"=>$text));

	 echo $json;
	}

	mysqli_close($conn);
?>