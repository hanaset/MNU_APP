<?php
	header("Content-Type: text/html;charset=UTF-8");

	$mysql_username = 'root';
	$mysql_password = 'wjdqls56';
	$mysql_database = 'mnu';

	$name = $_POST['name'];

	$conn = mysqli_connect("114.70.93.130",$mysql_username,$mysql_password,$mysql_database);

	if(!$conn){
		die("Connection failed: ". mysqli_connect_error());
	}

	$sql = "SELECT * FROM food_menu Where name = '".$name."' order by price";
	$result = mysqli_query($conn, $sql);
	$text = array();


	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result)){
			array_push($text, array('food_name'=>$row["food_name"],'price'=>$row["price"]));
		}
	
	$json = json_encode(array("result"=>$text));

	 echo $json;
	}
	mysqli_close($conn);
?>