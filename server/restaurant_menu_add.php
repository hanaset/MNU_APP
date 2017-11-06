<?php
	header("Content-Type: text/html;charset=UTF-8");

	$mysql_username = 'root';
	$mysql_password = 'wjdqls56';
	$mysql_database = 'mnu';

	$id = $_POST['id'];
	$food = $_POST['food_name'];
	$price = $_POST['price'];
	$num = $_POST['num'];

	$conn = mysqli_connect("114.70.93.130",$mysql_username,$mysql_password,$mysql_database);

	if(!$conn){
		die("Connection failed: ". mysqli_connect_error());
	}

	$sql = "INSERT INTO food_menu values ('".$num."','".$id."','".$food."','".$price."','')";
	$result = mysqli_query($conn, $sql);

	if($result){
		echo "1";
	}else{
		echo "-1";
	}
	mysqli_close($conn);
?>