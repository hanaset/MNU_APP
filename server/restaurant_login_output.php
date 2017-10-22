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

	$sql = "SELECT name, notice FROM restaurant Where id ='".$id."'";
	$result = mysqli_query($conn, $sql);

	if(mysqli_num_rows($result) > 0){
		$row = mysqli_fetch_assoc($result);
		echo $row['name'].",".$row['notice'];
	}else{
		echo "failed";
	}

	mysqli_close($conn);
?>