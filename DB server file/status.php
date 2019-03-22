<?php
$host="fieldmaus.site:3307";
$user="teamprj";
$password="";
$port="3307";
$con=mysql_connect($host,$user,$password);
if($con) {
	$status="true";
} else {
	$status="false";
}
        $json = json_encode($status);
        echo $json;
?>
<form action="json-target.php" method="post" accept-charset="utf-8"> 
     <input type="hidden" name="json" value="<?php echo $json; ?>" id="json"> 
    <p><input type="submit" value="Continue &rarr;"></p> 
</form> 