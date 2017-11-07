<?php

header("Content-Type: text/html;charset=UTF-8");

$mysql_username = 'root';
$mysql_password = 'wjdqls56';
$mysql_database = 'mnu';

$target_path = "files/";

$tmp_img = explode("." ,$_FILES['uploadedfile']['name']); 
$id = $tmp_img[2];

$img_name = $tmp_img[0]."_".$id.".".$tmp_img[1];


$target_path = $target_path . basename($img_name);
if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
	echo "1";
} else {
	echo "2";
}

$conn = mysqli_connect("114.70.93.130",$mysql_username,$mysql_password,$mysql_database);

if(!$conn){
	die("Connection failed: ". mysqli_connect_error());
}

$sql = "UPDATE restaurant set image = '".$target_path."' Where id = '".$id."'";
$result = mysqli_query($conn, $sql);


mysqli_close($conn);

?>
