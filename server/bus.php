<?php
	header("Content-Type: text/html;charset=UTF-8");

	$mysql_username = 'root';
	$mysql_password = 'wjdqls56';
	$mysql_database = 'mnu';

	$conn = mysqli_connect("127.0.0.1",$mysql_username,$mysql_password,$mysql_database);

	if(!$conn){
		die("Connection failed: ". mysqli_connect_error());
	}

	$sql = "SELECT * FROM bus";
	$result = mysqli_query($conn, $sql);
	$text = array();


	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result)){
			array_push($text, array('go_come'=>$row["go_come"],'time'=>$row["time"], 'location'=>$row["location"], 'start'=>$row["start"], 'route'=>$row["route"]));
		}
	

	 $json = json_encode(array("result"=>$text));

	 echo $json;
	}else{
		echo "-1";
	}

	mysqli_close($conn);
?>