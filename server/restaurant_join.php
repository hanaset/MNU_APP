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
	$name = $_POST['name'];
	$phone = $_POST['phone'];
	$time = $_POST['time'];
	$delivery = $_POST['delivery'];

	$sql = "INSERT into restaurant values ('".$name."','".$phone."','".$time."','".$delivery."','.','0','null','".$id."','".$pass."')";
	
	$result = mysqli_query($conn, $sql);

	if($result)
		echo "succesed";
	else
		echo "failed";

	mysqli_close($conn);
?>