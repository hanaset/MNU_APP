<?php
	header("Content-Type: text/html;charset=UTF-8");

	$mysql_username = 'root';
	$mysql_password = 'wjdqls56';
	$mysql_database = 'mnu';

	$num = $_POST['num'];

	$conn = mysqli_connect("114.70.93.130",$mysql_username,$mysql_password,$mysql_database);

	if(!$conn){
		die("Connection failed: ". mysqli_connect_error());
	}

	$sql = "SELECT image From food_menu where num ='".$num."'";
	$result = mysqli_query($conn, $sql);

	$image;

	if($result->num_rows > 0){
		while($row = $result->fetch_assoc()){
			$image = $row["image"];
		}
	}

	$sql = "DELETE From food_menu Where num = '".$num."'";
	$result = mysqli_query($conn, $sql);
	
	unlink("./".$image);

	if($result){
		echo "1";
	}else{
		echo "-1";
	}
	mysqli_close($conn);
?>