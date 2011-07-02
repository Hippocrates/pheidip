<?php

require_once("ddmanag_config.php");

$donationDB = new PDO("mysql:dbname=$dbname;host=$dbhost", $username, $password);

?>