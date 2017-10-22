<?php
	header("Content-Type: text/html;charset=UTF-8");

	$mysql_username = 'root';
	$mysql_password = 'wjdqls56';
	$mysql_database = 'mnu';

	$conn = mysqli_connect("114.70.93.130",$mysql_username,$mysql_password,$mysql_database);

	if(!$conn){
		die("Connection failed: ". mysqli_connect_error());
	}

	$id = $_POST['id'];
	$pass = $_POST['pass'];

	$sql = "SELECT * FROM restaurant Where id ='".$id."' && password = '".$pass."'";
	$result = mysqli_query($conn, $sql);

	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result)){
			//array_push($text, array('phone'=>$row["phone"],'time'=>$row["time"], 'notice'=>$row["notice"],'image'=>$row["image"]));
			echo "1";
		}
	
	//$json = json_encode(array("result"=>$text));
	}else
		echo "failed";

	mysqli_close($conn);
?>