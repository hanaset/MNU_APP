<?php
	header("content-Type: text/html;charset=UTF-8");

	$conn = new mysqli("114.70.93.130","root","wjdqls56","mnu");

	if($conn->connect_error){
		die("Connection failed: ".$conn->connect_error);
	}

	$date = $_POST['date'];
	$day = $_POST['day'];

	$sql = "SELECT * FROM grade WHERE date ='".$date."' && day = '".$day."' && location = 'BTL'" ;

	$result = $conn->query($sql);

	$count = 0;
	$score = 0;

	if($result->num_rows > 0){
		while($row = $result->fetch_assoc()){

			$count++;
			$score = $score + $row["score"];
		}
	}

	if($count != 0){
		echo $score/$count;	
	}else{
		echo "-1";
	}


	echo ",";


	$sql = "SELECT * FROM grade WHERE date ='".$date."' && day = '".$day."' && location = '한울관'" ;

	$result = $conn->query($sql);

	$count = 0;
	$score = 0;
	

	if($result->num_rows > 0){
		while($row = $result->fetch_assoc()){

			$count++;
			$score = $score + $row["score"];
		}
	}

	if($count != 0){
		echo $score/$count;	
	}else{
		echo "-1";
	}



	$conn->close();

?>