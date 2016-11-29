<?php
	header("content-Type: text/html;charset=UTF-8");

	$conn = new mysqli("114.70.93.130","root","wjdqls56","mnu");

	if($conn->connect_error){
		die("Connection failed: ".$conn->connect_error);
	}

	$table = "grade";
	$date = $_POST['date'];
	$location = $_POST['location'];
	$content = $_POST['content'];
	$score = $_POST['score'];
	$day = $_POST['day'];

	$sql = "INSERT INTO ".$table."(date, location, content, score, day) VALUES ('".$date."','".$location."','".$content."','".$score."','".$day."')";

	$result = $conn->query($sql);

	if($result)
		echo "1";
	else
		echo "-1";


	$conn->close();

?>