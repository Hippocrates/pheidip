<?php

error_reporting(E_ALL); 
ini_set('display_errors', '1'); 

# By including autorun, and then the actual test files, it will automatically
# create the executable context to run the unit tests
require_once("simpletest/autorun.php");
require_once("test/db/AllDBTests.php");
require_once("test/logic/AllLogicTests.php");

?>