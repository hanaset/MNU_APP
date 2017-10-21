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

	$sql = "UPDATE restaurant set count = count+1 where name = '".$name."'";
	
	$result = mysqli_query($conn, $sql);

	if($result)
		echo "1";
	else
		echo "-1";

	mysqli_close($conn);
?>