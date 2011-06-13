<html> 
<head> 
<title>Sample Page</title>
<?php 
ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once("ddmanag_config.php");
require_once("db/DataAccess.php");

$data = new DataAccess($donationDB);
?>
</head> 
<body> 

<br />
<?php
$runs = $data->getAllSpeedRuns();

echo "This page desperately needs attention by a real web designer...";

echo "<h3>List of all runs:</h3>";

foreach($runs as $run)
{
  $runId = $run['speedRunId'];
  $runName = $run['name'];
  echo "<a href='game_data.php?game_id=$runId'>$runName</a> <br />";
}
?>
<br />


</body>
</html> 